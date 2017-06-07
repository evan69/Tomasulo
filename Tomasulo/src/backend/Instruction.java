
package backend;

import backend.FloatPointUnit.OP;

public class Instruction {
	OP op;
	int immidate = -1;
	int target = -1;
	int operand1 = -1, operand2 = -1;
	
	boolean issued = false;
	int executation = 0;
	boolean writtenBack = false;
	
	public int getInstStatus() {
		if(!issued) {	// not issued yet
			return -1;
		} else if(writtenBack) {	// already finished
			return -2;
		}
		return executation;
	}
	
	public Instruction(OP op, String tar, String op1, String op2) {
		target = Integer.parseInt(tar.substring(1));
		operand1 = Integer.parseInt(op1.substring(1));
		operand2 = Integer.parseInt(op2.substring(1));
		this.op = op;
	}
	
	public Instruction(OP op, String tar, int immidate) {
		this.op = op;
		if(op == OP.LD) {
			this.target = Integer.parseInt(tar.substring(1));
		} else if(op == OP.ST) {
			this.operand1 = Integer.parseInt(tar.substring(1));
		} else {
			assert false;
		}
		
		this.immidate = immidate;
	}
	
	@Override
	public String toString() {
		return op.toString();
	}
}

