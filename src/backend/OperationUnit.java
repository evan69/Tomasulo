package backend;

import backend.FloatPointUnit.OP;
import backend.FloatPointUnit.UnitType;
import javafx.stage.Stage;

public class OperationUnit {
//	int ttlPeriod;
	ReserStation[] stations;
	UnitType operation;	// like "MULD", "ADDD", "LOAD", "STORE"
	//ReserStation currentExec;
	//int currentTime;
	//float result;
	public static final int[] MULT_STAGES = {1, 2, 2, 2, 2, 1};
	public static final int[] DIV_STAGES = {40};
	public static final int[] MEM_STAGES = {1, 1};
	public static final int[] ADD_STAGES = {1, 1};
	private boolean[] used;
	int hi = 0;
	int lo = 0;
	// queue for LOAD / STORE
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
		switch (operation) {
		case ADD:
		case LOAD:
		case STORE:
			used = new boolean[2];
			break;
		case MULT:
			used = new boolean[6];
			break;
		default:
			break;
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
				assert st.vk != 0.0;
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

	//LOAD / STORE 检测队列是否满
	public boolean checkFull() {
		if(hi == lo && stations[lo].busy == true) {
			//已经满了
			return true;
		}
		return false;
	}
	
	//LOAD / STORE 检测队列是否空
	public boolean checkEmpty() {
		if(hi == lo && stations[lo].busy == false) {
			//已经空了
			return true;
		}
		return false;
	}
	
	//选择一个保留站执行
	private ReserStation chooseStation() {
		switch (operation) 
		{
			case LOAD:
			case STORE:
				/*
				for(ReserStation st : stations)
				{
					if(!st.isBusy())
						continue;
					return st;
				}
				*/
				if(!checkEmpty())
					return stations[lo];
				// TODO : 需要将访存部件保留站修改为队列结构，获取头部
				break;
			case ADD:
			case MULT:
				if(isExcutingDivide() || used[0])
					break;
				//正在执行除法，则无法选取执行
				for(ReserStation st : stations) {
					if(!st.isBusy())
						continue;
					if(st.isExcuting)
						continue;
					//如果正在执行，也不选
					if(st.op == OP.DIVD && isExcutingInstr())
						continue;
					//如果有指令在执行，则不能开始执行除法
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
		for(ReserStation st : stations) {
			if(st.isBusy() && st.isExcuting) {
				//正在执行
				if(st.currentTime > 0) {
					--st.currentTime;
				}
				if(st.currentTime == 0) {	// finished this stage
					if(st.stage < st.stages.length - 1 && !used[st.stage + 1]) {
						used[st.stage] = false;	// give up the previous stage
						++st.stage;
						used[st.stage] = true;	// occupy the next stage
						st.currentTime = st.stages[st.stage];
					} else if(st.stage == st.stages.length - 1) {
						++st.stage;	// execution finished
					}
//					++st.stage;
//					if(st.stage < st.stages.length) {
//						st.currentTime = st.stages[st.stage];
//					}
				}
			}
		}
	}
	
	//是否有除法在执行
	private boolean isExcutingDivide() {
		if(this.operation == UnitType.MULT) {
			for(ReserStation st : stations) {
				if(st.isBusy() && st.isExcuting && st.op == OP.DIVD) {
					return true;
				}
			}
		}
		return false;
	}
	
	//是否有指令在执行
	private boolean isExcutingInstr() {
		for(ReserStation st : stations) {
			if(st.isBusy() && st.isExcuting) {
				return true;
			}
		}
		return false;
	}
	
	//选择一条指令准备执行
	public void startNewExec() {
		//选取一个新的指令开始执行
		ReserStation st = chooseStation();
		if(st == null)
			return;
		st.isExcuting = true;
		if(st.op != OP.DIVD) {
			used[0] = true;
		}
		st.stage = 0;
		st.currentTime = st.stages[st.stage]; 
		st.result = getResult(st);
		System.out.println("choose nex exec " + st.result);
	}

	public void writeBack() {
		for(ReserStation st : stations) {
			if(!st.isBusy() || !st.isExcuting) {
				continue;
			}
			if(st.stage < st.stages.length)	// execution not finished
				return;
			if(st.op != OP.ST) {
				//不是store指令
				CDB cdb = CDB.getInstance();
				cdb.broadcast(st, st.result);
				//交给CDB
				st.isExcuting = false;
				st.busy = false;
				if(st.op == OP.LD) {
					//lo++;
					lo = (lo + 1) % stations.length;
				}
			}
			else {
				//是store指令，应在写回阶段写mem
				if(st.qk == null) {
					FloatPointUnit.memory[st.a] = st.vk;
					st.isExcuting = false;
					st.busy = false;
					st = null;
					//lo++;
					lo = (lo + 1) % stations.length;
				}
			}
		}
		
	}
	
	public boolean issueInstruction(Instruction curr) {
		if(operation == UnitType.ADD || operation == UnitType.MULT) {
			for(ReserStation station: stations) {
				if(!station.isBusy()) {
					station.issueIn(curr);
					return true;//成功加入到保留站中
					//break;
				}
			}
		}
		else {
			if(checkFull()) {
				//已经满了
				return false;
			}
			ReserStation station = stations[hi];
			station.issueIn(curr);
			hi = (hi + 1) % stations.length;
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String res = "unitType: " + operation.toString() + "\n";
		if(stations != null) {
			for(ReserStation st: stations) {
				if(st.isExcuting) {
					res += "> " + st + "\n";
				} else {
					res += st + "\n";
				}
			}
		}
		return res;
	}
	
	public String showContent() {
		String res = "";
		switch(operation) {
		case ADD:
		case MULT:
			if(stations != null) {
				for(ReserStation st : stations) {
					res += st.getName() + "\t";
					if(st.isBusy()) {
						res += "yes\t";
						res += st.op == null ? "--" :st.op.toString() + "\t";
						res += (st.qj == null ? " " : st.qj.getName()) + "\t";
						res += (st.qk == null ? " " : st.qk.getName()) + "\t";
						res += (st.qj == null ? st.vj : " ") + "\t";
						res += (st.qj == null ? st.vk : " ") + "\n";
					} else {
						res += "no\t \t \t \t \t \n";
					}
				}
			}
			break;
		case LOAD:
			if(stations != null) {
				for(ReserStation st : stations) {
					res += st.getName() + "\t";
					if(st.isBusy()) {
						res += "yes\t";
						res += st.a + "\n";
					} else {
						res += "no\t \n";
					}
				}
			}
			break;
		case STORE:
			if(stations != null) {
				for(ReserStation st : stations) {
					res += st.getName() + "\t";
					if(st.isBusy()) {
						res += "yes\t" + st.a + "\t";
						res += (st.qk == null ? st.vk : st.qk.getName()) + "\n";
					} else {
						res += "no\t \t \n";
					}
				}
			}
		}
		return res;
	}
	
}
