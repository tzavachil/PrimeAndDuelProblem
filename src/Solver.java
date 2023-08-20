import java.util.ArrayList;
import java.util.Arrays;

public class Solver {
	private int maxmin;
	private int varCount = 0;
	private double[][] a;
	private ArrayList<Double> b;
	private ArrayList<Double> c;
	private ArrayList<Integer> Eqin;
	private ArrayList<String> var;
	private int[] physicalRestrictions;
	private int[] physicalSymbols;
	private Transformer transformer;
	
	//temporary variables for assistance
	private int tempRows;
	private int tempCol;
	
	
	public Solver() {
		this.b = new ArrayList<>();
		this.c = new ArrayList<>();
		this.Eqin = new ArrayList<>();
		this.var = new ArrayList<>();
		
		this.transformer = new Transformer();
	}

	//sets value for max or min
	public void setMaxmin(int maxmin) {
		this.maxmin = maxmin;
	}
	
	public int get2DTableRows(double[][] table) {
		int rows = 0;
		
		for(double[] row : table) {
			rows ++;
		}
		
		return rows;
	}
	
	//creates an empty table A filled with 0
	public void createTableA(int rows, int col) {
		this.a = new double[rows][col];
		this.tempRows = rows;
		this.tempCol = col;
		
	}
	
	public void extendTableA() {
		//initial dimensions of table A
		int tempRows = get2DTableRows(this.a);
		int tempCol = this.varCount-1;
		
		double[][] tempA = new double[tempRows][tempCol];
		tempA = this.a;
		createTableA(tempRows,(tempCol+1));
		for(int i=0; i<tempRows; i++) {
			for(int j=0; j<tempCol; j++) {
				this.a[i][j] = tempA[i][j];
			}
			this.a[i][tempCol] = 0.0;
		}
	}
	
	//adds numbers on the table A in the 'row' row. Variable is needed to find the column for the number.
	public void addToA(double number, String variable, int row) {
		int col = searchPos(variable);
		
		try {
			a[row][col] = number;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			//new variable on subjects
			extendTableA();
			a[row][col] = number;		
		}
	}

	//adds value on the List Eqin
	public void addToEqin(int value) {
		this.Eqin.add(value);
	}
	
	//adds numbers on the List b
	public void addToB(double number) {
		this.b.add(number);
	}

	//adds the number into the right column (variable) in the List c
	public void addToC(double number, String variable) {
		int pos = searchPos(variable);
		
		this.c.remove(pos);
		this.c.add(pos,number);
	}
	
	//searches the right column for the 'variable' variable in the var List
	public int searchPos(String variable) {
		int pos = -1;
		
		for(String var : this.var) {
			pos++;
			if(var.equals(variable)) {
				break;
			}
		}
		
		return pos;
	}
	
	/* adds values on the var list if there is not already in there. 
	 * Also adds the same amount of 0 in the c list
	 * */
	public void addToVar(String value) {
		if(!var.contains(value)) {
			this.var.add(value);
			this.c.add(0.0);
			this.varCount++;
		}
	}
	
	//return the number of the variable on our problem
	public int getVarCount() {
		return this.varCount;
	}

	//prints out problem's data in tables
	public String printDataOutput() {
		String output;
		
		output = "MinMax=" + this.maxmin + "\n\n";
		output = output + transformToString("c",this.c,0);
		output = output + transformAToString(this.a);
		output = output + transformToString("b",this.b,0);
		output = output + transformToString("Eqin", this.Eqin,1);
		output = output + restrictionToString();
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public String transformToString(String listName, ArrayList<?> aList, int checker) {
		//checker = 0 : input from ArrayList<Double>
		//checker = 1 : input from ArrayList<Integer>
		
		String line = listName + "=[";
		
		switch(checker) {
			case 0 :
				for(Double data : (ArrayList<Double>)aList)
					line = line + " " + data + "\n";
				break;
			case 1 :
				for(Integer data : (ArrayList<Integer>)aList)
					line = line + " " + data + "\n";
				break;
		}
		
		line = line.substring(0, line.length()-1) + "]\n\n"; //the substring is because we don't want the last new line character
		
		return line;
	}
	
	public String transformAToString(double[][] table) {
		String line = "A=[";
		
		for(double[] row : table) {
			String stringRow = "";
			for(double num : row) 
				stringRow = stringRow + " " + num;
			line = line + stringRow + "\n";
		}
		
		line = line.substring(0, line.length()-1) + "]\n\n"; //the substring is because we don't want the last new line character
		
		return line;
	}
	
	//creates the restriction's tables
	public void createRestrictionTables(int col) {
		this.physicalRestrictions = new int[col]; 
		this.physicalSymbols = new int[col];
	}
	
	//adds the number into the right column (variable) in the table physicalRestrictions
	public void addToPhysicalRestrictions(int number, String variable) {
		int pos = searchPos(variable);
		
		physicalRestrictions[pos] = number;
	}
	
	//adds the number into the right column (variable) in the table physicalSymbols
	public void addToPhysicalSymbols(int number, String variable) {
		int pos = searchPos(variable);
		
		physicalSymbols[pos] = number;
	}
	
	//producing a String that contains the restriction section of the final text
	public String restrictionToString() {
		String text = "";
		
		text = text + "Physical Restrictions: " + Arrays.toString(physicalRestrictions);
		text = text + "\nPhysical Symbols: " + Arrays.toString(physicalSymbols);
		text = text + "\n\n";
		
		return text;
	}
	
	//creating and producing the Dual problem from Primal's data
	public Solver transformToDual() {
		Solver dualSolver = new Solver();
		
		dualSolver.maxmin = -this.maxmin; //max/min section
		dualSolver.b = this.c; //b section
		dualSolver.c = this.b; //c section
		
		//variables section
		int count = 1;
		for(Double num : dualSolver.c) {
			dualSolver.var.add("w" + count);
			count++;
		}
		
		//A sections
		dualSolver.createTableA(this.tempCol, this.tempRows);
		for(int i=0; i<this.tempCol; i++) {
			for(int j=0; j<this.tempRows; j++) {
				dualSolver.a[i][j] = this.a[j][i];
			}
		}
		
		//Eqin section
		for(int num : this.physicalSymbols)
			dualSolver.Eqin.add(num);

		//physicalRestrictions sections
		dualSolver.createRestrictionTables(this.Eqin.size());
		int i = 0;
		for(Integer num : this.Eqin) {
			dualSolver.physicalSymbols[i] = num;
			i++;
		}

		return dualSolver;
	}
	
	//prints out the problem on text form
	public String printTextOutput() {
		String output;
		
		output = transformer.transformMaxMin(this.maxmin) + " ";
		output = output + transformer.transformTableC(this.c,this.var) + "\n";
		output = output + "st\n";
		output = output + transformer.transformSubjects(this.a,this.b,this.var,this.Eqin);
		output = output + transformer.transformRestrictions(this.var,this.physicalSymbols,this.physicalRestrictions);
		
		return output;
	}

}
