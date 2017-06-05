package backend;

import backend.FloatPointUnit.OP;

public class Instruction {
	OP op;
	int immidate = -1;
	int target = -1;
	int operand1 = -1, operand2 = -1;
	
	public Instruction(OP op, String tar, String op1, String op2) {
		target = Integer.parseInt(tar.substring(1));
		operand1 = Integer.parseInt(op1.substring(1));
		operand2 = Integer.parseInt(op2.substring(1));
		this.op = op;
	}
	
	public Instruction(OP op, String tar, int immidate) {
		this.op = op;
		this.target = Integer.parseInt(tar.substring(1));
		this.immidate = immidate;
	}
}
