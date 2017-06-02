package backend;

import backend.FloatPointUnit.UnitType;

public class OperationUnit {
//	int ttlPeriod;
	ReserStation[] stations;
	UnitType operation;	// like "MULD", "ADDD", "LOAD", "STORE"
	/**
	 * @param stationNum is number of stations, like 2 for multiple/divide, 3 for load
	 * @param stationType is one of  "MULD", "ADDD", "LOAD", "STORE"
	 * */
	public OperationUnit(int stationNum, UnitType stationType) {
//		this.ttlPeriod = ttlPeriod;
		operation = stationType;
		stations = new ReserStation[stationNum];
		for(int i = 0; i < stationNum; ++i) {
			stations[i] = new ReserStation(operation.toString() + i);
		}
	}
	
	public ReserStation chooseStation() {
		switch (operation) 
		{
			case LOAD:
			case STORE:
				for(ReserStation st : stations)
				{
					if(!st.isBusy())
						continue;
					return st;
				}
				break;
			case ADD:
			case MULT:
				for(ReserStation st : stations)
				{
					if(!st.isBusy())
						continue;
					if(st.qj != null || st.qk != null)
						continue;
					//RegStates state = st.qi;
					return st;
				}
				break;
			default:
				break;
		}
		return null;
	}
	
	public void issueInstruction(Instruction curr) {
		for(ReserStation station: stations) {
			if(!station.isBusy()) {
				station.issueIn(curr);
				break;
			}
		}
	}
	
}
