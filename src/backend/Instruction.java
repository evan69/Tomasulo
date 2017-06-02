package backend;

import backend.FloatPointUnit.OP;

public class Instruction {
	OP op;
	int immidate;
	int operan1, operan2;
	
	public Instruction(OP op, String op1, String op2) {
		operan1 = Integer.parseInt(op1.substring(1));
		operan2 = Integer.parseInt(op2.substring(1));
		this.op = op;
	}
	
	public Instruction(OP op, int immidate) {
		this.op = op;
		this.immidate = immidate;
	}
}
