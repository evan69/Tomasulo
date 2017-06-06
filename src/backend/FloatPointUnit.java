package backend;

import java.util.LinkedList;
import java.util.Queue;

public class FloatPointUnit {
	public enum OP {
		ADDD, SUBD, MULD, DIVD, LD, ST
	}; 
	//6种运算
	public enum UnitType {
		ADD, MULT, LOAD, STORE
	};
	//4个保留站，每个可能被多种运算共享
	static float memory[] = new float[4096];
	OperationUnit addUnit = new OperationUnit(3, UnitType.ADD);
	OperationUnit multUnit = new OperationUnit(2, UnitType.MULT);
	OperationUnit ldUnit = new OperationUnit(3, UnitType.LOAD);
	OperationUnit stUnit = new OperationUnit(3, UnitType.STORE);
	Queue<Instruction> instQueue = new LinkedList<Instruction>();
	CDB cdb = CDB.getInstance();
	public FloatPointUnit() {
		cdb.setFPU(this);
	}
	RegStates regs = RegStates.getInstance();
	
	public void display() {
		System.out.println(addUnit);
		System.out.println(multUnit);
		System.out.println(ldUnit);
		System.out.println(stUnit);
		System.out.println("\n\n\n");
	}
	
	public String showReserStations() {
		return addUnit.showContent() + multUnit.showContent() + ldUnit.showContent() + stUnit.showContent();
	}
	
	public void update() {
		// TODO
		//疑问：一周期能进入多条指令还是一条
		issueInstruction();
		execute();
		writeBack();
		startNewExec();//added choose instr here
		display();
		System.out.println("update finished");
	}
	
	public void setReg(int ind, float v) {
		regs.setReg(ind, v);
	}
	
	public String getRegInfo() {
		return regs.toString();
	}
	
	public void addInstruction(Instruction instruction) {
		instQueue.add(instruction);
	}
	
	public void issueInstruction() {
		//Instruction currentI = instQueue.poll();
		Instruction currentI = instQueue.peek();
		if(currentI == null)
			return;
		//指令序列里面没有指令，直接结束
		
		boolean issueInResult = false;
		//对应的部件是否有空的保留站，或者说是否成功加到保留站里
		
		switch (currentI.op) {
		case ADDD:
		case SUBD:
			issueInResult = addUnit.issueInstruction(currentI);
			break;
		case MULD:
		case DIVD:
			issueInResult = multUnit.issueInstruction(currentI);
			break;
		case LD:
			issueInResult = ldUnit.issueInstruction(currentI);
			break;
		case ST:
			issueInResult = stUnit.issueInstruction(currentI);
			break;
		default:
			System.out.println(currentI.op);
			break;
		}
		//System.out.println(issueInResult);
		//如果保留站有空位，成功加入，则要从指令队列中去除一条指令
		if(issueInResult == true) {
			instQueue.poll();
		}
		
		// fixed : 可能的问题：如果保留站满了怎么办，如果队列为空怎么办？
	}
	
	private void startNewExec() {
		addUnit.startNewExec();
		multUnit.startNewExec();
		ldUnit.startNewExec();
		stUnit.startNewExec();
	}
	
	private void execute() {
		addUnit.execute();
		multUnit.execute();
		ldUnit.execute();
		stUnit.execute();
	}
	
	private void writeBack() {
		addUnit.writeBack();
		multUnit.writeBack();
		ldUnit.writeBack();
		stUnit.writeBack();
	}
}
