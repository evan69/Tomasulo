package backend;

import backend.FloatPointUnit.OP;

public class ReserStation {
	private String name; // sth like MULT1, ADD1
	public boolean busy = false;
	public ReserStation qj, qk;
	public RegStates qi;
	float vj, vk;
	public int a;
	public OP op;
	int cnt = 0; // cnt is together with op

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
		switch (inst.op) {
		case LD:
		case ST: // memory related
			// fix : 对于store指令，除了立即数应该还有一个源寄存器，按照书上来说应该是Qk
			if (qi.isRegToWrite(inst.operand1)) {
				vk = qi.getRegValue(inst.operand1);
				qk = null;
			} else {
				qk = qi.getSourceStation(inst.operand1);
			}
			// fixed see above
			a = inst.immidate;
			break;
		default: // registers only
			doOperand(inst.operand1, inst.operand2);
			break;
		}
		// handle target register
		qi.setSourceStation(inst.target, this);

		this.busy = true;
		// 需要修改为busy
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
		return getName() + "\t" + busy + "\t" + info;
	}

}
