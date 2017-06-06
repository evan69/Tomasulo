package backend;

import backend.FloatPointUnit.OP;

public class ReserStation {
	private String name; // sth like MULT1, ADD1
	public boolean busy = false;
	public ReserStation qj, qk;
	public RegStates qi;
	public float vj, vk;
	public int a;
	public OP op;

	public int[] stages;
	public int stage;			// together with op. if add/sub, {0, 1}; if mult, {0, ..., 9}; if div, {0...39}
	public boolean isExcuting;
	public int currentTime;		// in this stage
	public float result;

	private void doOperand(int operand1, int operand2) {
		if (!qi.isRegToWrite(operand1)) {
			vj = qi.getRegValue(operand1);
			qj = null;
		} else {
			qj = qi.getSourceStation(operand1);
		}

		if (!qi.isRegToWrite(operand2)) {
			vk = qi.getRegValue(operand2);
			qk = null;
		} else {
			qk = qi.getSourceStation(operand2);
		}
	}

	public void issueIn(Instruction inst) {
		// handle source registers
		this.op = inst.op;
		if(inst.op == OP.ST) { // memory related
			// fix : 对于store指令，除了立即数应该还有一个源寄存器，按照书上来说应该是Qk
			this.stages = OperationUnit.MEM_STAGES;
			if (qi.isRegToWrite(inst.operand1)) {
				vk = qi.getRegValue(inst.operand1);
				qk = null;
			} else {
				qk = qi.getSourceStation(inst.operand1);
			}
			// fixed see above
			a = inst.immidate;
		} else if(inst.op == OP.LD) {
			this.stages = OperationUnit.MEM_STAGES;
			a = inst.immidate;
		}
		else {// registers only
			doOperand(inst.operand1, inst.operand2);
			switch (op) {
			case ADDD:
			case SUBD:
				stages = OperationUnit.ADD_STAGES;
				break;
			case MULD:
				stages = OperationUnit.MULT_STAGES;
				break;
			case DIVD:
				stages = OperationUnit.DIV_STAGES;
			default:
				break;
			}
		}
		
		if(inst.op != OP.ST) {	// handle target register
			qi.setSourceStation(inst.target, this);
		}
		this.busy = true;
		// 需要修改为busy
		this.isExcuting = false;
	}

	public ReserStation(String name) {
		this.qi = RegStates.getInstance();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isBusy() {
		return busy;
	}

	@Override
	public String toString() {
		String info;
		if (!isBusy()) {
			info = "";
		} else {
			switch (op) {
			case LD:
			case ST:
				info = Integer.toString(a);
				break;
			default:
				info = "qj: " + (qj == null ? vj : qj.getName()) + "\t" + "qk: " + (qk == null ? vk : qk.getName());
				break;
			}
		}
		return getName() + "\t busy: " + busy + 
				"\t gonna excuting: " + (isExcuting ?("period " + currentTime + " of stage " + stage): "false") +
				"\t" + info;
	}

}
