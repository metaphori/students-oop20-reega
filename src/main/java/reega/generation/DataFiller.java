package reega.generation;

public interface DataFiller {

	/**
	 * Fills the given database with utilization data for every given contract
	 * starting from the latest time stamp in the database to the current time stamp
	 * for every service of every contract, following these cadences:
	 * - for water, electric energy and gas: new data every hour.
	 * - for plastic, glass, paper and mixed wastes: new data every day.
	 * 
	 * If no starting time is to be found in the database data will be generated
	 * for the last month.
	 */
	public void fill();
}
