package backend;

public class OperationUnit {
//	int ttlPeriod;
	ReserStation[] stations;
	String operation;	// like "MULD", "ADDD", "LOAD", "STORE"
	/**
	 * @param stationNum is number of stations, like 2 for multiple/divide, 3 for load
	 * @param stationType is one of  "MULD", "ADDD", "LOAD", "STORE"
	 * */
	public OperationUnit(int stationNum, String stationType) {
//		this.ttlPeriod = ttlPeriod;
		operation = stationType;
		stations = new ReserStation[stationNum];
		for(int i = 0; i < stationNum; ++i) {
			stations[i] = new ReserStation(operation + i);
		}
	}
}
