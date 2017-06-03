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
	public void update() {
		// TODO
		issueInstruction();
		execute();
		writeBack();
	}
	
	public void issueInstruction() {
		Instruction currentI = instQueue.poll();
		switch (currentI.op) {
		case ADDD:
		case SUBD:
			addUnit.issueInstruction(currentI);
			break;
		case MULD:
		case DIVD:
			multUnit.issueInstruction(currentI);
			break;
		case LD:
			ldUnit.issueInstruction(currentI);
			break;
		case ST:
			stUnit.issueInstruction(currentI);
			break;
		default:
			System.out.println(currentI.op);
			break;
		}
		// TODO : 可能的问题：如果保留站满了怎么办，如果队列为空怎么办？
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
