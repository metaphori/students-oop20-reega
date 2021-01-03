package reega.data.models;

public enum ServiceType {
	ELECTRICITY("electricity", 0),
	GAS("gas",1),
	WATER("water",2),
	PAPER("paper",3),
	GLASS("glass",4),
	PLASTIC("plastic",5),
	MIXED("mixed",6);

	private final String name;
	private final int id;

	ServiceType(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public int getID() {
		return this.id;
	}
}
