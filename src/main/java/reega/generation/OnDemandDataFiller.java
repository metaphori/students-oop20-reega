package reega.generation;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;

public class OnDemandDataFiller implements DataFiller {

	private static final Long SERVICES_STEPPING = 3_600_000L; // one hour in ms
	private static final Long WASTES_STEPPING = 86_400_000L; // one day in ms
	private static final Long START_DATE = new Date().getTime() - 2_592_600_000L; // 30 days and 10 min ago in ms

	// multi value map: 1 usage simulator (for every contract) many Data to fill
	private final Map<UsageSimulator, Set<Data>> UsageDataMap;
	private final DataController database;
	private final Long currentDate;

	public OnDemandDataFiller(DataController controller, final Collection<Contract> contracts) {
		this.currentDate = new Date().getTime() + 60_000L; // current date + 1 min
		this.database = controller;
		this.UsageDataMap = new HashMap<>();

		for (Contract contract : contracts) {
			this.UsageDataMap.put(new SelectiveUsageSimulator(contract.getServices()), contract.getServices().stream()
					.map(srv -> new Data(contract.getId(), srv)).collect(Collectors.toSet()));
		}
	}

	@Override
	public void fill() {
		// for each entry <UsageSimlator, Data> generate data and submit it
		for (Entry<UsageSimulator, Set<Data>> entry : this.UsageDataMap.entrySet()) {
			try {
				for (Data data : entry.getValue()) {
					this.generateValues(entry.getKey(), data);
					this.database.putUserData(data);
				}
			} catch (SQLException e) {
				System.err.println("could not save data to DB. \nerror: " + e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}

	private void generateValues(UsageSimulator simulator, Data data) throws SQLException {

		Map<Long, Double> simulations = new HashMap<>();
		Long stepping = ServiceType.Categories.SERVICES.contains(data.getType()) ? SERVICES_STEPPING : WASTES_STEPPING;
		Long dataDate = this.database.getLatestData(data.getContractID(), data.getType());
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
