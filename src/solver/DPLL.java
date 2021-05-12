package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DPLL {
	
	
	//private formula set
	//constructor sets
	//run() function actually runs this all
	
	private static boolean runDPLL(FormulaSet set) {
		int literal;
		int counts[], uniq[];
		ArrayList<Integer> unit_clause, unit_vals;
		FormulaSet newset, copy;
		
		System.out.println("Start of function");
		set.toConsole();	

		uniq = countLiteralsUnique(set);
		if (set.hasEmptyClause())
			return false;
		if (set.isConsistent(uniq))
			return true;


		unit_clause = findUnitFormula(set);
		unit_vals = unitValues(unit_clause, set);
		
		newset = runUnitProp(unit_vals, set);

		System.out.println("After Unit Prop ");
		newset.toConsole();
		
		uniq = countLiteralsUnique(newset);
		newset = pureListAssign(uniq, newset);
		System.out.println("After Pure Assign ");
		newset.toConsole();

		counts = countLiterals(newset);
		literal = chooseHighestLiteralCount(counts);
	
		//no independent objects for runDPLL
		copy = newset.copySet();
		
		//return true;
		return runDPLL(eliminateFormulas(literal, newset)) || runDPLL(eliminateFormulas(-literal, copy));
		//if
	}

	
	
	// [ + + + + + + + - - - - - - -] 
	// positive values first
	// negated literals at positive_idx + total_num_vars
	private static int[] countLiteralsUnique(FormulaSet inset) {
		
		int lit_count[] = new int[inset.getVarcount() * 2];
		ArrayList<Formula> set = (ArrayList<Formula>) inset.getFormulas();
		
		for (int i = 0; i < set.size(); i++)
		{
			for (int j = 0; j < set.get(i).getFormula().size(); j++)
			{
				int literal = set.get(i).getFormula().get(j);
				
				//count literals
				if (literal > 0)
					lit_count[literal - 1]++;
				else 
					lit_count[Math.abs(literal) + inset.getVarcount() - 1]++;
			}
		}
		
		return lit_count;
	}

	private static int[] countLiterals(FormulaSet inset) {
		
		int lit_count[] = new int[inset.getVarcount()];
		ArrayList<Formula> set = (ArrayList<Formula>) inset.getFormulas();
		
		for (int i = 0; i < set.size(); i++)
		{
			for (int j = 0; j < set.get(i).getFormula().size(); j++)
			{
				int literal = set.get(i).getFormula().get(j);
				
				//count literals
				//value - 1
				lit_count[Math.abs(literal) - 1]++;
			}
		}
		
		return lit_count;
	}
	

	
	private static FormulaSet eliminateFormulas(int unit, FormulaSet set) {
		FormulaSet newset = new FormulaSet(set.getVarcount());
		
		System.out.println("Choosing " + unit);
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			if (!set.getFormulas().get(i).isSatisfiedBy(unit))
			{
				set.getFormulas().get(i).removeUnit(-unit);
				newset.addFormula(set.getFormulas().get(i));
				//check most recent addition for literal removal
				//newset.getFormulas().get(newset.getFormulas().size() - 1).removeUnit(-unit);
			}
		}
		return newset;
	}
	
	
	private static ArrayList<Integer> findUnitFormula(FormulaSet set) {
		//there is the case of conflicting unit formulas
		Set<Integer> units = new HashSet<Integer>();
		int unit_value;
		
		ArrayList<Integer> unit_idx = new ArrayList<Integer>();
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			//add multiple unit clauses if they are the same sign
			//do not add the negation
			if (set.getFormulas().get(i).getFormula().size() == 1)
			{
				unit_value = set.getFormulas().get(i).getFormula().get(0);
				if (!units.contains(unit_value))
				{
					unit_idx.add(i);
					units.add(unit_value);
					units.add(-unit_value);
				}
			}
		}
		
		return unit_idx;
	}
	
	public static ArrayList<Integer> unitValues(ArrayList<Integer> unit_idx, FormulaSet set) {
		ArrayList<Integer> unit_val = new ArrayList<Integer>();
		for (int i = 0; i < unit_idx.size(); i++)
			unit_val.add(set.getFormulas().get(unit_idx.get(i)).getFormula().get(0));
		
		return unit_val;
	}
	

//	
//	private static boolean isUnitVal(int val, ArrayList<Integer> unit_val) {
//		for (int i = 0; i < unit_val.size(); i++)
//			if (val == unit_val.get(i))
//				return true;
//		return false;
//	}
	
	private static FormulaSet runUnitProp(ArrayList<Integer> unit_val, FormulaSet set) {
		
		if (unit_val.size() == 0)
			return set;
		
		FormulaSet newset = new FormulaSet(set);
		System.out.println("Before Prop");
		//newset.toConsole();
		
		for (int i = 0; i < unit_val.size(); i++) {
			newset = unitProp(unit_val.get(i),newset);
			//System.out.println("Prop " + unit_val.get(i));
			//newset.toConsole();
		}

		return newset;
	}
	
	private static FormulaSet unitProp(int unit_val, FormulaSet set) {
		FormulaSet newset = new FormulaSet(set.getVarcount());
		Formula clause;
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			clause = set.getFormulas().get(i);
			//add formulas that are not satisfied OR are the unit clause itself
			if (!clause.isSatisfiedBy(unit_val) || (clause.isSatisfiedBy(unit_val) && clause.getFormula().size() == 1))
			{
				//remove negated values
				clause.removeUnit(-unit_val);
				newset.addFormula(clause);
			}
		}
		
		return newset;
	}
	
	private static ArrayList<Integer> findPure(int[] uniq_count, int varcount) {
		ArrayList<Integer> purelist = new ArrayList<Integer>();
		
		for (int i = 0; i < varcount; i++)
		{
			if (i + varcount < uniq_count.length)
			{
				if (uniq_count[i] > 0 && uniq_count[i + varcount] == 0)
					purelist.add(i+1);
				else if (uniq_count[i] == 0 && uniq_count[i + varcount] > 0)
					purelist.add(-(i+1));
			}
		}
		
		return purelist;
	}
	

	//should  purelist act on the unit variables?
	private static FormulaSet pureListAssign(int[] uniq_count, FormulaSet set) {
		ArrayList<Integer> purelist = findPure(uniq_count, set.getVarcount());
		ArrayList<Formula> forms = (ArrayList<Formula>) set.getFormulas();
		FormulaSet newset = new FormulaSet(set.getVarcount());
		boolean found;
		
		//remove clauses that contain pure literals
		for (int i = 0; i < forms.size(); i++)
		{
			found = false;
			//do not act on unit clauses
			if (forms.get(i).getFormula().size() > 1)
				for (int j = 0; j < purelist.size(); j++)
					if (forms.get(i).isSatisfiedBy(purelist.get(j)))
						found = true;

			if (!found)
				newset.addFormula(forms.get(i));
		}
		
		return newset;
	}
	
	
	//and if not already chosen?
	//[0, 2, 3, 2, 1] lit count looks like this
	//this means that 3 should be assigned, with a count of 3 occurrences
	private static int chooseHighestLiteralCount(int lit_count[]) {
		int highest = 0;
		int val = 0;
		int num_vars = lit_count.length;
		
		for (int i = 0; i < lit_count.length; i++)
		{
			//if (lit_count[i] > highest && !sol.isValueAssigned(i+1))
			if (lit_count[i] > highest)
			{
				highest = lit_count[i];
				val = i+1;
			}
		}	
		//returning the value of the literal
		return val;
	}
	
	
	public boolean solve(ArrayList<ArrayList<Integer>> dimacs) {
		
		//dimacs find variables
		FormulaSet forms = new FormulaSet(20);

		forms.setFormulas(dimacs);
		forms.toConsole();

		boolean sat = DPLL.runDPLL(forms);
		System.out.println(sat);
		return sat;
	}
	
//	public void testCopy(ArrayList<ArrayList<Integer>> dimacs) {
//		FormulaSet forms = new FormulaSet(dimacs, 5);
//		forms.toConsole();
//		
//		FormulaSet copy = new FormulaSet(5);
//		
////		for (int i = 0; i < forms.getFormulas().size(); i++) 
////		{
////			Formula f = new Formula();
////			for (int j = 0; j < forms.getFormulas().get(i).getFormula().size(); j++)
////			{
////				f.addValue(forms.getFormulas().get(i).getFormula().get(j));
////			}
////			copy.addFormula(f);
////		}
//		
//		copy = forms.copySet();
//		
//		System.out.println("Remove form copy");
//		copy.getFormulas().get(1).getFormula().remove(1);
//		copy.toConsole();
//		
//		System.out.println("Original");
//		forms.toConsole();
//		
//	}

	public static void main(String[] args) {
		
		int num_vars = 4;
		
		int set[][] = {
				{1, 2, 3},
				{1,4},
				{-2, -3},
				{-1, -3},
		};
		

		


		FormulaSet forms = new FormulaSet(num_vars);
		FormulaSet newset = new FormulaSet(num_vars);
		

		boolean sat = DPLL.runDPLL(forms);
		System.out.println(sat);
	}

}
