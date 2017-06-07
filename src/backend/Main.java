package backend;

import backend.FloatPointUnit.OP;

public class Main {
	static float regs[] = {1.0f, 1.5f, 4.0f, 100.0f};
	static Instruction[] testInstr = {
		/*
		//RAW测试
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.SUBD, "F1", "F2", "F3")
		*/
		/*
		//WAR测试
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.SUBD, "F4", "F3", "F1"),
		new Instruction(OP.DIVD, "F5", "F0", "F3"),
		new Instruction(OP.ADDD, "F3", "F5", "F1")
		*/
		/*
		//WAW测试
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.SUBD, "F0", "F3", "F2"),
		new Instruction(OP.DIVD, "F5", "F1", "F2"),
		new Instruction(OP.ADDD, "F5", "F3", "F2")
		*/
		/*
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.MULD, "F0", "F1", "F2"),
		new Instruction(OP.DIVD, "F0", "F1", "F2")
		*/
		/*
		new Instruction(OP.ST  , "F0", 0),
		new Instruction(OP.LD  , "F1", 0),
		new Instruction(OP.ST  , "F0", 0),
		new Instruction(OP.LD  , "F2", 0),
		new Instruction(OP.ST  , "F0", 0),
		new Instruction(OP.LD  , "F3", 0),
		new Instruction(OP.ST  , "F0", 0),
		new Instruction(OP.LD  , "F4", 0)
		*/
		new Instruction(OP.ADDD, "F0", "F1", "F2"),
		new Instruction(OP.ST  , "F0", 0),
		new Instruction(OP.SUBD, "F1", "F2", "F3"),
		new Instruction(OP.LD  , "F1", 0)
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
		for(int i = 0; i < 120 ; i++) {
			System.out.println("update " + i);
			cpu.update();
		}
		System.out.println(RegStates.getInstance().toString());
	}
}
