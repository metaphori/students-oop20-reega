package reega.generation;

import java.io.IOException;
import java.time.Duration;
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
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
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
    private final Map<UsageSimulator, Set<Data>> usageDataMap;
    private final DataController database;
    private final Long currentDate;

    public OnDemandDataFiller() throws IOException {
        this.currentDate = new Date().getTime() + Duration.ofMinutes(1).toMillis(); // current date + 1 min
        this.database = DataControllerFactory.getDefaultDataController(null);
        this.usageDataMap = new HashMap<>();
        this.addContracts(ContractControllerFactory.getDefaultDataController(null).getAllContracts());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill() {
        // for each entry <UsageSimlator, Set<Data>> generate data and submit it
        for (final Entry<UsageSimulator, Set<Data>> entry : this.usageDataMap.entrySet()) {
            try {
                for (final Data data : entry.getValue()) {
                    this.generateValues(entry.getKey(), data);
                    this.database.putUserData(data);
                }
            } catch (final IOException e) {
                OnDemandDataFiller.LOGGER.error("could not save generated data to local DB.", e);
                break;
            }
        }
    }

    /**
     * Add contracts to the current contracts.
     *
     * @param contracts contracts that needs to be added
     */
    public final void addContracts(final List<Contract> contracts) {
        for (final Contract contract : contracts) {
            final List<DataType> dataTypes = contract.getServices()
                    .stream()
                    .flatMap(srv -> DataType.getDataTypesByService(srv).stream())
                    .collect(Collectors.toList());
            this.usageDataMap.put(new SelectiveUsageSimulator(dataTypes),
                    dataTypes.stream().map(data -> new Data(contract.getId(), data)).collect(Collectors.toSet()));
        }
    }

    /**
     * Generate the records for populating <code>data</code>.
     *
     * @param simulator {@link UsageSimulator} used for generating data
     * @param data      data class to populate with records
     * @throws IOException
     */
    private void generateValues(final UsageSimulator simulator, final Data data) throws IOException {

        final Map<Long, Double> simulations = new HashMap<>();
        final Long stepping = data.getType().getServiceType() == ServiceType.GARBAGE
                ? OnDemandDataFiller.GARBAGE_STEPPING
                : OnDemandDataFiller.SERVICES_STEPPING;
        Long dataDate = this.database.getLatestData(data.getContractID(), data.getType());
        if (dataDate == null || dataDate == 0L) {
            dataDate = OnDemandDataFiller.START_DATE;
        } else {
            dataDate += stepping;
        }
        while (dataDate <= this.currentDate) {
            simulations.put(dataDate, simulator.getUsage(data.getType()).get());
            dataDate += stepping;
        }
        data.addRecords(simulations);
    }

}
