
public class Parser {
	private Checker checker;
	private Splitter splitter;
	private boolean flag;
	
	public Parser() {
		flag = false;
		this.checker = new Checker();
		this.splitter = new Splitter();
	}
	
	//gets the first line and processes it
	public boolean getFirstLine(String maxmin, Solver solver) {
		boolean flagMaxMin = checker.checkMaxMin(maxmin.substring(0,3));
		if(flagMaxMin) {
			if(maxmin.substring(0,3).equalsIgnoreCase("max")) {
				solver.setMaxmin(1);
			}
			else
				solver.setMaxmin(-1);
		}
		
		boolean flagEquation = splitter.splitFirstRow(maxmin.substring(4,maxmin.length()),solver);
		
		this.flag = (flagMaxMin && flagEquation);
		
		return this.flag;
	}
	
	//gets the second line and sends it into the checker
	public boolean getSecondLine(String st) {		
		this.flag = checker.checkSt(st);
		return flag;
	}
	
	//gets the subject's lines and processes them
	public boolean getLine(String line,Solver solver,int row) {
		
		boolean flag = splitter.splitSubjects(line,solver,row);
		
		return flag;
	}
	
	//gets the restriction's line and processes them
	public boolean getRestrictionLine(String line,Solver solver,int col) {
		boolean flag = splitter.splitRestrictions(line,solver,col);
		
		return flag;
	}
	
}
