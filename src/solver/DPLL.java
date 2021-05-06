package solver;

import java.util.ArrayList;

public class DPLL {
	
	
	//private formula set
	//constructor sets
	//run() function actually runs this all
	
	private static boolean runDPLL(FormulaSet set) {
		int literal;
		int counts[];
		counts = countLiterals(set);
		//if set of formulas are consistent
		if (set.isConsistent(counts))
			return true;
		if (set.hasEmptyClause())
			return false;
		
		//one possible value for each remaining variable
		//return true;
		//if one formula empty
		//return false;
		//find unit clauses
		//for every unit clause, run propagate
		//pure literal logic
		

		literal = chooseHighestLiteralCount(counts);
		System.out.println("Choosing " + literal);
		return runDPLL(eliminateFormulas(literal, set)) || runDPLL(eliminateFormulas(-literal, set));
		//if
	}
	
	private static ArrayList<Integer> findLoneUnits(FormulaSet set) {
		ArrayList<Integer> units = new ArrayList<Integer>();
		
		for (int i = 0; i < set.getFormulas().size(); i++)
			if (set.getFormulas().get(i).getFormula().size() == 1)
				units.add(set.getFormulas().get(i).getFormula().get(0));
		
		return units;
	}
	
	
	// [ + + + + + + + - - - - - - -] 
	// positive values first
	// negated literals at positive_idx + total_num_vars
	private static int[] countLiterals(FormulaSet inset) {
		
		int lit_count[] = new int[inset.getVarcount() * 2];
		ArrayList<Formula> set = (ArrayList<Formula>) inset.getFormulas();
		
		for (int i = 0; i < set.size(); i++)
		{
			for (int j = 0; j < set.get(i).getFormula().size(); j++)
			{
				int literal = set.get(i).getFormula().get(j);
				
				//count literals
				//positive literals are placed in the index "value - 1"
				//negated literals are played in the index "value + total variables - 1"
				if (literal > 0)
					lit_count[literal - 1]++;
				else
					lit_count[Math.abs(literal) + inset.getVarcount() - 1]++;
			}
		}
		
		return lit_count;
	}
	
//	private static ArrayList<Integer> findPureLiterals(int lit_count[], FormulaSet set) {
//		
//	}
	
	private static FormulaSet eliminateFormulas(int unit, FormulaSet set) {
		FormulaSet newset = new FormulaSet(set.getVarcount());
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			if (!set.getFormulas().get(i).isSatisfiedBy(unit))
				newset.addFormula(set.getFormulas().get(i));
		}
		System.out.println(" ");
		newset.toConsole();
		//System.out.println();
		return newset;
	}
	
	
	private static int chooseHighestLiteralCount(int lit_count[]) {
		int highest = 0;
		int idx = 0;
		int num_vars = (lit_count.length + 1) / 2;
		
		for (int i = 0; i < lit_count.length; i++)
		{
			if (lit_count[i] > highest)
			{
				highest = lit_count[i];
				idx = i;
			}
		}	
		//in the negated portion of the array
		if (idx >= num_vars)
			return -(idx - num_vars + 1);
		return idx + 1;
	}
	

	public static void main(String[] args) {
		
		int num_vars = 5;
		
		int set[][] = {
				{-5, -1, 3},
				{-4, 5, 2},
				{-4, -2},
				{-2, -5}
		};

		FormulaSet forms = new FormulaSet(num_vars);
		FormulaSet newset = new FormulaSet(num_vars);
		forms.setFormulas(set);
		forms.toConsole();
//
//		//here we found a unit prop
//		newset = DPLL.eliminateFormulas(1, forms);
//		System.out.println();
//		newset.toConsole();
//		//here we found a pure literal
//		newset = DPLL.eliminateFormulas(5, newset);
//		System.out.println();
//		newset.toConsole();
//		
//		
//		ArrayList<Integer> units = DPLL.findLoneUnits(forms);
//		System.out.println();
//		for (int i = 0; i < units.size(); i++)
//			System.out.print(units.get(i) + " ");
//		
//		System.out.println();
//		int[] lits = DPLL.countLiterals(forms);
//		forms.toConsole();
//		System.out.println();
////		for (int i = 0; i < lits.length; i++)
////			System.out.println(i+1 + ": " + lits[i]);
//		System.out.println(DPLL.chooseHighestLiteralCount(lits));
		
		
		boolean sat = DPLL.runDPLL(forms);
		System.out.println(sat);
	}

}
