package reega.generation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.ServiceType;

public class OnDemandDataFiller implements DataFiller {

    private static final Long SERVICES_STEPPING = 3_600_000L; // one hour in ms
    private static final Long GARBAGE_STEPPING = 86_400_000L; // one day in ms
    private static final Long START_DATE = new Date().getTime() - 2_592_600_000L; // 30 days and 10 min ago in ms

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFiller.class);

    // multi value map: 1 usage simulator (for every contract) many Data to fill
    private final Map<UsageSimulator, Set<Data>> UsageDataMap;
    private final DataController database;
    private final Long currentDate;

    public OnDemandDataFiller(DataController controller, final Collection<Contract> contracts) {
        this.currentDate = new Date().getTime() + 60_000L; // current date + 1 min
        this.database = controller;
        this.UsageDataMap = new HashMap<>();

        for (Contract contract : contracts) {
            List<DataType> dataTypes = contract.getServices()
                    .stream()
                    .flatMap(srv -> DataType.getDataTypesByService(srv).stream())
                    .collect(Collectors.toList());
            this.UsageDataMap.put(new SelectiveUsageSimulator(dataTypes),
                    dataTypes.stream().map(data -> new Data(contract.getId(), data)).collect(Collectors.toSet()));
        }
    }

    @Override
    public void fill() {
        // for each entry <UsageSimlator, Set<Data>> generate data and submit it
        for (Entry<UsageSimulator, Set<Data>> entry : this.UsageDataMap.entrySet()) {
            try {
                for (Data data : entry.getValue()) {
                    this.generateValues(entry.getKey(), data);
                    this.database.putUserData(data);
                }
            } catch (SQLException e) {
                LOGGER.error("could not save generated data to remote DB.", e);
                break;
            } catch (IOException e) {
                LOGGER.error("could not save generated data to local DB.", e);
                break;
            }
        }
    }

    private void generateValues(UsageSimulator simulator, Data data) throws SQLException, IOException {

        Map<Long, Double> simulations = new HashMap<>();
        Long stepping = data.getType().getServiceType() == ServiceType.GARBAGE ? GARBAGE_STEPPING : SERVICES_STEPPING;
        Long dataDate = this.database.getLatestData(data.getContractID(), data.getType().getServiceType());
        if (dataDate == null) {
            dataDate = START_DATE;
        } else {
            dataDate += stepping;
        }
        while (dataDate <= currentDate) {
            simulations.put(dataDate, simulator.getUsage(data.getType()).get());
            dataDate += stepping;
        }
        data.addRecords(simulations);
    }

}
