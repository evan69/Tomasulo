package backend;

import backend.FloatPointUnit.OP;
import backend.FloatPointUnit.UnitType;

public class OperationUnit {
//	int ttlPeriod;
	ReserStation[] stations;
	UnitType operation;	// like "MULD", "ADDD", "LOAD", "STORE"
	ReserStation currentExec;
	int currentTime;
	float result;
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
	
	//获取每个指令对应的执行周期数
	private int getExecTime(OP op) {
		switch (op)
		{
			case ADDD:
			case SUBD:
				return 2;
			case MULD:
				return 10;
			case DIVD:
				return 40;
			case LD:
			case ST:
				return 2;
			default:
				return -1;
		}
	}
	
	//执行阶段被调用，获取计算结果（除store返回0不代表计算结果）
	private float getResult(ReserStation st) {
		switch (st.op)
		{
			case ADDD:
				return st.vj + st.vk;
			case SUBD:
				return st.vj - st.vk;
			case MULD:
				return st.vj * st.vk;
			case DIVD:
				return st.vj / st.vk;
				//TODO : 可能的除0错误？
			case LD:
				return FloatPointUnit.memory[st.a];
			case ST:
				//FloatPointUnit.memory[st.a] = st.vj;
				//store指令在执行阶段不做任何事
				return 0;
			default:
				return 0;
		}
	}
	
	//选择一个保留站执行
	private ReserStation chooseStation() {
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
				// TODO : 需要将访存部件保留站修改为队列结构，获取头部
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
	
	//执行
	public void execute() {
		if(currentExec != null)
			//exec current instr
		{
			if(currentTime > 0)
				currentTime--;
		}
		/*
		else
			//choose another instr to exec
		{
			ReserStation st = chooseStation();
			if(st == null)
				return;
			currentExec = st;
			currentTime = getExecTime(st.getOP());
			result = getResult(st);
		}
		*/
	}
	
	public void writeBack() {
		if(currentTime > 0)
			return;
		if(currentExec.op != OP.ST) {
			//不是store指令
			CDB cdb = CDB.getInstance();
			cdb.broadcast(currentExec, result);
			//交给CDB
		}
		else {
			//是store指令，应在写回阶段写mem
			if(currentExec.qk == null) {
				FloatPointUnit.memory[currentExec.a] = currentExec.vk;
				currentExec.busy = false;
			}
		}
		
		//选取一个新的指令开始执行
		ReserStation st = chooseStation();
		if(st == null)
			return;
		currentExec = st;
		currentTime = getExecTime(st.op);
		result = getResult(st);
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
