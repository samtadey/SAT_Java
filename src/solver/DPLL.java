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
		ArrayList<Integer> unit_clause;
		FormulaSet newset;
		
		System.out.println("Start of function");
		set.toConsole();
		

		uniq = countLiteralsUnique(set);
		//if set of formulas are consistent
		//if consistency is found we can add those variables to the assignment?
		if (set.isConsistent(uniq))
			return true;
		if (set.hasEmptyClause())
			return false;

		unit_clause = findUnitFormula(set);
		newset = unitProp(unit_clause, set);
		System.out.println("After Unit Prop ");
		newset.toConsole();
		
		uniq = countLiteralsUnique(newset);
		newset = pureListAssign(uniq, newset);
		System.out.println("After Pure Assign ");
		newset.toConsole();

		counts = countLiterals(newset);
		literal = chooseHighestLiteralCount(counts);
	
		//assign to assignments

		
		//return true;
		return runDPLL(eliminateFormulas(literal, newset)) || runDPLL(eliminateFormulas(-literal, newset));
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
				newset.addFormula(set.getFormulas().get(i));
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
	
	private static FormulaSet unitProp(ArrayList<Integer> unit_idx, FormulaSet set) {
		//nothing to do
		if (unit_idx.size() < 1)
			return set;
		
		FormulaSet newset = new FormulaSet(set.getVarcount());
		ArrayList<Formula> forms = (ArrayList<Formula>) set.getFormulas();
		Formula clause;
		int unit, form_idx;
		
		for (int u = 0; u < unit_idx.size(); u++)
		{
			form_idx = unit_idx.get(u);
			//retrieve the unit formula eg {2}
			unit = set.getFormulas().get(form_idx).getFormula().get(0);
			//remove clauses with matching literal OTHER than the unit clause
			for (int i = 0; i < set.getFormulas().size(); i++)
			{
				clause = forms.get(i);
				if (i == unit_idx.get(u) || !clause.isSatisfiedBy(unit))
				{
					//remove literals containing negation before adding
					clause.removeUnit(-unit);
					newset.addFormula(clause);
				}
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
			for (int j = 0; j < purelist.size(); j++)
			{
				if (forms.get(i).isSatisfiedBy(purelist.get(j)))
					found = true;
					
			}
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
		FormulaSet forms = new FormulaSet(5);

		forms.setFormulas(dimacs);
		forms.toConsole();

		boolean sat = DPLL.runDPLL(forms);
		System.out.println(sat);
		return sat;
	}

	public static void main(String[] args) {
		
		int num_vars =4;
		
		int set[][] = {
				{1, 2, 3},
				{1,4},
				{-2, -3},
				{-1, -3},
		};
		
//		int set[][]  = {
//		{-10, 8, -2},
//		{4, -7, -6},
//		{9, -2},
//		{4, -5, -9},
//		{4, -5, 7},
//		{4, 3},
//		{3, -10, -2},
//		{2, 9, -6},
//		{7, -6, -1},
//		{-4, 8, 2},
//		{-4, 8, 2},
//		{9, -5, 4},
//		{3, 1, 10},
//		{2, 4, -5},
//		{-7, -1, -8},
//		{2, 5, -7},
//		{1,-6, 2},
//		{10, -5, -4},
//		{-6, 7},
//		{-6, -4, 6},
//		{1, -9},
//		{7, -2, -9},
//		{6, 1},
//		{-3, -1, 6},
//		{5, 8, 7},
//		{-10, -2, 4},
//		{-6, 3, -10},
//		{-6, 3, -10},
//		{3, -1, 10},
//		{-10, -7, 2},
//		{2, -7, -10},
//		{-4, 2, 7},
//		{2, 9, -6},
//		{1, -1, -7},
//		{1 ,-8, 6},
//		{-3, 7},
//		{-6, -7, 1},
//		{-4, 5, 9},
//		{10, -3, -7},
//		{1, 10, -10},
//		{-3, 10, 6},
//		{-3, -1},
//		};

		
		
		
		
		
		

		FormulaSet forms = new FormulaSet(num_vars);
		FormulaSet newset = new FormulaSet(num_vars);
		forms.setFormulas(set);
//		forms.toConsole();
		
//		Assignment a1 = new Assignment(num_vars);
//		for (int i = 0; i < a1.getSolution().length; i++)
//			System.out.println(a1.getSolution()[i]);
		
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
		System.out.println();
//		int[] lits = DPLL.countLiterals(forms);
//		int[] ulits = DPLL.countLiteralsUnique(forms);
//		forms.toConsole();
//		System.out.println();
//		for (int i = 0; i < lits.length; i++)
//			System.out.println(i+1 + ": " + lits[i]);
//		System.out.println(DPLL.chooseHighestLiteralCount(lits, a1));
//		
//		System.out.println();
//		for (int i = 0; i < ulits.length; i++)
//			System.out.println(i+1 + ": " + ulits[i]);
//		System.out.println();
//		ArrayList<Integer> a = new ArrayList<Integer>();
//		a.add(7);
//
//		newset = unitProp(a, forms);
//		newset.toConsole();
//		
//		int[] test = countLiteralsUnique(newset);
//		for (int i = 0; i < test.length; i++)
//			System.out.println(i+1 + ": " + test[i]);	
		
		System.out.println("");
		//ArrayList<Integer> pure = findPure(test, newset.getVarcount());
		//for (int i = 0; i < pure.size(); i++)
			//System.out.println(pure.get(i));
		
//		FormulaSet nopure = pureListAssign(test, newset);
//		nopure.toConsole();
//		System.out.println(" ");
//		System.out.println(nopure.getFormulas().size());
		
		boolean sat = DPLL.runDPLL(forms);
		System.out.println(sat);
	}

}
