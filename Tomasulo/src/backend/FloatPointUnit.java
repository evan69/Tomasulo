package backend;

import java.util.LinkedList;
import java.util.Queue;

public class FloatPointUnit {
	public enum OP {
		ADDD, SUBD, MULD, DIVD, LD, ST
	}; 
	//6绉嶈繍绠�
	public enum UnitType {
		ADD, MULT, LOAD, STORE
	};
	//4涓繚鐣欑珯锛屾瘡涓彲鑳借澶氱杩愮畻鍏变韩
	public static float memory[] = new float[4096];
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
	
	
	public String show() {
		return "" + addUnit + multUnit + ldUnit + stUnit;
	}
	
	public void update() {
		// TODO
		//鐤戦棶锛氫竴鍛ㄦ湡鑳借繘鍏ュ鏉℃寚浠よ繕鏄竴鏉�
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
	
	public void addInstruction(Instruction instruction) {
		instQueue.add(instruction);
	}
	
	public void issueInstruction() {
		//Instruction currentI = instQueue.poll();
		Instruction currentI = instQueue.peek();
		if(currentI == null)
			return;
		//鎸囦护搴忓垪閲岄潰娌℃湁鎸囦护锛岀洿鎺ョ粨鏉�
		
		boolean issueInResult = false;
		//瀵瑰簲鐨勯儴浠舵槸鍚︽湁绌虹殑淇濈暀绔欙紝鎴栬�呰鏄惁鎴愬姛鍔犲埌淇濈暀绔欓噷
		
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
		//濡傛灉淇濈暀绔欐湁绌轰綅锛屾垚鍔熷姞鍏ワ紝鍒欒浠庢寚浠ら槦鍒椾腑鍘婚櫎涓�鏉℃寚浠�
		if(issueInResult == true) {
			instQueue.poll();
		}
		
		// fixed : 鍙兘鐨勯棶棰橈細濡傛灉淇濈暀绔欐弧浜嗘�庝箞鍔烇紝濡傛灉闃熷垪涓虹┖鎬庝箞鍔烇紵
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
