package backend;

import backend.FloatPointUnit.OP;

public class Main {
	static float regs[] = {1.0f, 1.5f, 4.0f};
	static Instruction[] testInstr = {
//		new Instruction(OP.ST, "F0", 0),
//		new Instruction(OP.LD, "F1", 0),
		new Instruction(OP.ADDD, "F2", "F1", "F1"),
		new Instruction(OP.DIVD, "F3", "F2", "F1"),
		new Instruction(OP.SUBD, "F4", "F2", "F3")
	};
	public static void main(String[] args) {
		System.out.println("test");
		
		
		FloatPointUnit cpu = new FloatPointUnit();
		for(int i = 0; i < regs.length; ++i) {
			cpu.setReg(i, regs[i]);
		}
		for(int i = 0; i < testInstr.length; ++i) {
			cpu.addInstruction(testInstr[i]);
		}
		while(true)
			cpu.update();
	}
}
