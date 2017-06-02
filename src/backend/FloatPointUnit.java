package backend;

public class FloatPointUnit {
	public enum OP {
		ADDD, SUBD, MULD, DIVD, LD, ST
	};
	float memory[] = new float[4096];
	OperationUnit addUnit = new OperationUnit(3, "ADD");
	OperationUnit multUnit = new OperationUnit(2, "MULT");
	OperationUnit ldUnit = new OperationUnit(3, "LOAD");
	OperationUnit stUnit = new OperationUnit(3, "STORE");
}
