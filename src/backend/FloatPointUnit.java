package backend;

public class FloatPointUnit {
	public enum OP {
		ADDD, SUBD, MULD, DIVD, LD, ST
	}; 
	//6种运算
	public enum UnitType {
		ADD, MULT, LOAD, STORE
	};
	//4个保留站，每个可能被多种运算共享
	float memory[] = new float[4096];
	OperationUnit addUnit = new OperationUnit(3, UnitType.ADD);
	OperationUnit multUnit = new OperationUnit(2, UnitType.MULT);
	OperationUnit ldUnit = new OperationUnit(3, UnitType.LOAD);
	OperationUnit stUnit = new OperationUnit(3, UnitType.STORE);
	
	public void update() {
		
	}
}
