package backend;

public class RegStates {
	private static RegStates instance;
	ReserStation[] sourceStations;
	float regValues[];
	static final int REG_NUM = 11;
	@Override
	public String toString() {
		String ret = "";
		for(int i = 0; i < regValues.length; ++i) {
			ret += i + "\t";
			ret += regValues[i] + "\t";
			ret += sourceStations[i].getName() + "\n";
		}
//		ret += 1 + "\t" + regValues[1] + "\t" + sourceStations[1].getName() + "\n";
//		ret += 2 + "\t" + regValues[2] + "\t" + sourceStations[2].getName() + "\n";
//		ret += 3 + "\t" + regValues[3] + "\t" + sourceStations[3].getName() + "\n";
//		ret += 4 + "\t" + regValues[4] + "\t" + sourceStations[4].getName() + "\n";
//		ret += 5 + "\t" + regValues[5] + "\t" + sourceStations[5].getName() + "\n";
		return ret;
	}
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
	
	public void setReg(int ind, float result) {
		regValues[ind] = result;
	}
	
}
