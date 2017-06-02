package backend;

import backend.FloatPointUnit.OP;

public class ReserStation {
	private String name;	// sth like MULT1, ADD1
	private boolean busy = false;
	public ReserStation qj, qk;
	public RegStates qi;
	private float vj, vk;
	private int a;
	private OP op;
	int cnt = 0;			// cnt is together with op
	
	private void doOperand(int operand1, int operand2) {
		if(qi.isRegToWrite(operand1)) {
			vj = qi.getRegValue(operand1);
			qj = null;
		} else {
			qj = qi.getSourceStation(operand1);
		}
		
		if(qi.isRegToWrite(operand2)) {
			vk = qi.getRegValue(operand2);
			qk = null;
		} else {
			qk = qi.getSourceStation(operand2); 
		}
	}
	
	public void issueIn(Instruction inst) {
		// handle source registers
		switch (inst.op) {
		case LD:
		case ST:	// memory related
			a = inst.immidate;
			break;
		default:	// registers only
			doOperand(inst.operand1, inst.operand2);
			break;
		}
		// handle target register
		qi.setSourceStation(inst.target, this);
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

}
