package backend;

public class RegStates {
	private static RegStates instance;
	ReserStation[] sourceStations;
	float regValues[];
	static final int REG_NUM = 11;
	
	private RegStates(int num) {
		sourceStations = new ReserStation[num];
		regValues = new float[num];
	}
	
	public static RegStates getInstance() {
		if(instance == null) {
			instance = new RegStates(REG_NUM);
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
	
	public void setSourceStation(int ind, ReserStation src) {
		sourceStations[ind] = src;
	}
	
}
