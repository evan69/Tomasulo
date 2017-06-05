package backend;

public class CDB {
	float result;
	ReserStation broadcaster;
	FloatPointUnit fpu;
	private static CDB instance;
	private CDB() {}
	
	public static CDB getInstance() {
		if(instance == null) {
			instance = new CDB();
		}
		return instance;
	}
	
	private void broadcast2OperUnit(OperationUnit u) {
		for(ReserStation station: u.stations) {
			if(station.isBusy()) {
				if(broadcaster.equals(station.qj)) {
					station.qj = null;
					station.vj = result;
				}
				if(broadcaster.equals(station.qk)) {
					station.qk = null;
					station.vk = result;
				}
			}
		}
	}
	
	public void setFPU(FloatPointUnit f) {
		fpu = f;
	}
	
	/**
	 * @param <code>broadcaster</code> is the reservation station who has calculated the <code>result</code>
	 * should only be called once in one <code>fpu.update()</code> 
	 * */
	public void broadcast(ReserStation broadcaster, float result) {
		System.out.println("station " + broadcaster.getName() + " boradcast " + result);
		this.broadcaster = broadcaster;
		this.result = result;
		broadcast2OperUnit(fpu.addUnit);
		broadcast2OperUnit(fpu.multUnit);
		RegStates rs = RegStates.getInstance();
		for(int i = 0; i < RegStates.REG_NUM; ++i) {
			if(broadcaster.equals(rs.getSourceStation(i))) {
				rs.setSourceStation(i, null);
				rs.setReg(i, result);
				break;
			}
		}
	}
	
}
