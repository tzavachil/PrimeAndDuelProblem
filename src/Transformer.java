import java.util.ArrayList;
import java.util.Arrays;

public class Transformer {
	
	public Transformer() {
		
	}
	
	//producing max-min String from Primal's data (int to String)
	public String transformMaxMin(int maxmin) {
		String text = "";
		
		switch(maxmin) {
			case 1 :
				text = "max";
				break;
			case -1 :
				text = "min";
				break;
			default :
				text = "Error on Max/Min variable";
				break;
		}
		
		return text;
	}
	
	//returning a String with a number and its sign (adding the "+" sign if number > 0)
	public String transformNumber(double num) {
		String number = "";
		
		if(num > 0)
			number = "+" + num;
		else if(num < 0)
			number = num + "";
		else
			number = "";
		
		return number;
	}
	
	/*producing a String with a sign from int number : 
	 * -1 : <=
	 *  0 :  =
	 *  1 : >=
	 */
	public String transformSymbol(int num) {
		String symbol = "";
		
		switch(num) {
			case -1 :
				symbol = "<=";
				break;
			case 0 :
				symbol = "=";
				break;
			case 1 :
				symbol = ">=";
				break;				
		}
		
		return symbol + " ";
	}
	
	//producing a String from Primal's table C (ArrayList<Double> to String)
	public String transformTableC(ArrayList<Double> c, ArrayList<String> var) {
		String output = "";
		
		int i = 0;
		for(double num : c) {
			output = output + this.transformNumber(num) + var.get(i) + " ";
			i++;
		}
		
		return output;
	}
	
	//producing a String from Primal's subjects (combine table a,b,var and Eqin to String)
	public String transformSubjects(double[][] a, ArrayList<Double> b, ArrayList<String> var, ArrayList<Integer> Eqin) {
		String output = "";
		
		int i = 0;
		for(double[] row : a) {
			int j = 0;
			for(double col : row) {
				if(!this.transformNumber(col).equals(""))
					output = output + this.transformNumber(col) + var.get(j) + " ";
				j++;
			}
			output = output + this.transformSymbol(Eqin.get(i)) + b.get(i);
			output = output + "\n";
			i++;
		}
		
		return output;
	}
	
	//producing a String from Primal's restrictions (combine var,physicalSymbols,physicalRestrictions to String)
	public String transformRestrictions(ArrayList<String> var, int[] physicalSymbols, int[] physicalRestrictions) {
		String output = "";
		
		int i = 0;
		for(String variable : var) {
			output = output + variable;
			if(physicalSymbols[i] != 0)
				output = output + " " + this.transformSymbol(physicalSymbols[i]) + physicalRestrictions[i] + ", ";
			else
				output = output + " free, ";
			i++;
		}
		
		output = output.substring(0, output.length()-2);
		
		return output;
	}

}
