package backend;

public class RegStates {
	private static RegStates instance;
	ReserStation[] sourceStations;
	float regValues[];
	private RegStates(int num) {
		sourceStations = new ReserStation[num];
		regValues = new float[num];
	}
	
	public static RegStates getInstance(int num) {
		if(instance == null) {
			instance = new RegStates(num);
		}
		return instance;
	}
	
	public boolean isRegToWrite(int ind) {
		return sourceStations[ind] != null;
	}
	
	public float getRegValue(int ind) {
		return regValues[ind];
	}
	
	public ReserStation getSourceStation(int ind) {
		return sourceStations[ind];
	}
}
