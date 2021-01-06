package reega.data.models;

import java.util.List;

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
	
	public static class Categories {
		
		static {
			SERVICES = List.of(WATER, ELECTRICITY, GAS);
			WASTES = List.of(PAPER, PLASTIC, GAS, MIXED);
		}
		
		public static final List<ServiceType> SERVICES;
		public static final List<ServiceType> WASTES;
		
		private Categories() {}
	}
}
