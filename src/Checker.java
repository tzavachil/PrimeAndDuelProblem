
public class Checker {
	
	public Checker() {
		
	}
	
	//checking if a string equals 'max' or 'min' and return a boolean value
	public boolean checkMaxMin(String maxmin) {
		boolean flag = ((maxmin.equalsIgnoreCase("max")) || (maxmin.equalsIgnoreCase("min")));
				
		return flag;
	}
	
	//checking if a string equals 'st' or 's.t.' or 'subject' and return a boolean value
	public boolean checkSt(String st) {
		boolean flag = ((st.equalsIgnoreCase("st")) || (st.equalsIgnoreCase("s.t.")) || (st.equalsIgnoreCase("subject")));
		return flag;
	}
	
	//checking the content of the string and returns a integer depending on where it needs to be stored
	public int checkForPos(String data) {
		//pos 0 : data --> table Eqin
		//pos 1 : data --> table A
		//pos 2 : data --> table b
		
		int pos = -1;
		
		if(isSymbol(data))
			pos = 0;
		else if(data.matches("[+-][0-9]*[a-zA-Z]+[0-9]*"))
			pos = 1;
		else
			pos = 2;
		
		return pos;
	}
	
	//checking if a string is one the '<=','=','>=' symbols
	public boolean isSymbol(String data) {
		return ((data.equals("<=")) || (data.equals("=")) || (data.equals(">=")));
	}
	
	//checking the right format of the line
	public int checkLine(String line) {
		//return flag = 1 : line --> subjects
		//return flag = 2 : line --> physical restrictions
		//return flag = 3 : line --> end
		
		int flag;
		
		line = line.replaceAll("\\s","");
		
		String subjectFormat = "([-+]*[0-9]*[a-zA-Z]+[0-9]*)+[<>=]+[+-]*[0-9]+[,]?";
		String restrictionFormat = "[a-zA-z]+[j][<>=]+\\d+([,]\\([j][=]((\\d+[,]?)+||(\\d+\\.+\\d+))\\))?";
		
		if(line.matches(subjectFormat))
			flag = 1;
		else if(line.matches(restrictionFormat))
			flag = 2;
		else if(line.equalsIgnoreCase("end"))
			flag = 3;
		else
			flag = 0;
		
		
		return flag;
	}
	
	//transform a string to an integer
	public int symbolToString(String symbol) {
		//return -1 --> <=
		//return  0 -->  =
		//return  1 --> >=
		//return  2 --> error
		
		int num;
		if(symbol.equals("<="))
			num = -1;
		else if(symbol.equals("=")) 
			num = 0;
		else if(symbol.equals(">="))
			num = 1;
		else
			num = 2;
		
		return num;
	}
}
