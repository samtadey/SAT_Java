package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * @author sam_t
 * 
 * 
 * 
**/
public class DPLL {
	
	private static boolean runDPLL(FormulaSet set, ArrayList<Integer> soln) {
		int literal;
		int counts[], uniq[];
		ArrayList<Integer> unit_clause, unit_vals;
		FormulaSet newset, copy;
		
		System.out.println("Start of function");
		set.toConsole();	

		uniq = set.countLiteralsUnique();
		if (set.hasEmptyClause())
			return false;
		if (set.isConsistent(uniq))
		{
			//add remaining to soln
			//will add duplicates at end, this won't matter if/when the implementation of Formula/FormulaSet is changed 
			//to contain sets instead of ArrayLists
			for (Formula f : set.getFormulas())
				for (int i : f.getFormula())
					soln.add(i);
			
			return true;
		}
		
		unit_clause = findUnitFormula(set);
		unit_vals = unitValues(unit_clause, set);
		
		newset = runUnitProp(unit_vals, set, soln);

		System.out.println("After Unit Prop ");
		newset.toConsole();
		
		uniq = newset.countLiteralsUnique();
		newset = pureListAssign(uniq, newset, soln);
		System.out.println("After Pure Assign ");
		newset.toConsole();

		
		uniq = newset.countLiteralsUnique();
		
		if (newset.hasEmptyClause())
			return false;
		if (newset.isConsistent(uniq))
		{
			for (Formula f : newset.getFormulas())
				for (int i : f.getFormula())
					soln.add(i);
			
			return true;
		}
			
		
		counts = newset.countLiterals();
		literal = chooseHighestLiteralCount(counts);
	
		//unit prop and pure lit could solve the problem
		
		//no independent objects for runDPLL
		//copy = newset.copySet();
		
		//return true;
		//return runDPLL(eliminateFormulas(literal, newset, soln), soln) || runDPLL(eliminateFormulas(-literal, copy, soln), soln);
		return runDPLL(eliminateFormulas(literal, newset, soln), soln) || runDPLL(eliminateFormulas(-literal, newset, soln), soln);
	}
	
	
	/*
	 * orig
	 */
//	private static FormulaSet eliminateFormulas(int unit, FormulaSet set, ArrayList<Integer> soln) {
//		FormulaSet newset = new FormulaSet(set.getVarcount());
//		soln.add(unit);
//		
//		System.out.println("Choosing " + unit);
//		
//		for (int i = 0; i < set.getFormulas().size(); i++)
//		{
//			if (!set.getFormulas().get(i).isSatisfiedBy(unit))
//			{
//				set.getFormulas().get(i).removeUnit(-unit);
//				newset.addFormula(set.getFormulas().get(i));
//				//check most recent addition for literal removal
//				//newset.getFormulas().get(newset.getFormulas().size() - 1).removeUnit(-unit);
//			}
//		}
//		return newset;
//	}
	
	
	private static FormulaSet eliminateFormulas(int unit, FormulaSet set, ArrayList<Integer> soln) {
		FormulaSet newset = new FormulaSet(set.getVarcount());
		Formula form, updform;

		soln.add(unit);
		
		System.out.println("Choosing " + unit);
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			form = set.getFormulas().get(i);
			//did not find literal, therefore must be added to the new set
			if (!form.isSatisfiedBy(unit))
			{
				//if find negation we need to create a new formula without that literal
				if (form.isSatisfiedBy(-unit))
				{
					updform = form.addAllBut(-unit);
					newset.addFormula(updform);
				}	
				else
				{
					newset.addFormula(form);
				}
			}
		}
		return newset;
	}
	
	//does removing the unit clause itself matter?
	private static FormulaSet unitProp(int unit, FormulaSet set) {
		FormulaSet newset = new FormulaSet(set.getVarcount());
		Formula clause, updform;
		
		for (int i = 0; i < set.getFormulas().size(); i++)
		{
			clause = set.getFormulas().get(i);
			
			//add formulas that are not satisfied OR are the unit clause itself
			if (!clause.isSatisfiedBy(unit))
			{
				//if find negation we need to create a new formula without that literal
				if (clause.isSatisfiedBy(-unit))
				{
					updform = clause.addAllBut(-unit);
					newset.addFormula(updform);
				}	
				else
				{
					newset.addFormula(clause);
				}
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
	
	
	private static FormulaSet runUnitProp(ArrayList<Integer> unit_val, FormulaSet set, ArrayList<Integer> soln) {
		
		if (unit_val.size() == 0)
			return set;
		
		FormulaSet newset = new FormulaSet(set);
		System.out.println("Before Prop");
		//newset.toConsole();
		
		//add unit clauses to solution list
		for (int i = 0; i < unit_val.size(); i++)
			soln.add(unit_val.get(i));
		
		
		for (int i = 0; i < unit_val.size(); i++) {
			newset = unitProp(unit_val.get(i),newset);
			//System.out.println("Prop " + unit_val.get(i));
			//newset.toConsole();
		}

		return newset;
	}
	
//	private static FormulaSet unitProp(int unit_val, FormulaSet set) {
//		FormulaSet newset = new FormulaSet(set.getVarcount());
//		Formula clause;
//		
//		for (int i = 0; i < set.getFormulas().size(); i++)
//		{
//			clause = set.getFormulas().get(i);
//			//add formulas that are not satisfied OR are the unit clause itself
//			if (!clause.isSatisfiedBy(unit_val) || (clause.isSatisfiedBy(unit_val) && clause.getFormula().size() == 1))
//			{
//				//remove negated values
//				clause.removeUnit(-unit_val);
//				newset.addFormula(clause);
//			}
//		}
//		
//		return newset;
//	}
	
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
	private static FormulaSet pureListAssign(int[] uniq_count, FormulaSet set, ArrayList<Integer> soln) {
		ArrayList<Integer> purelist = findPure(uniq_count, set.getVarcount());
		ArrayList<Formula> forms = (ArrayList<Formula>) set.getFormulas();
		FormulaSet newset = new FormulaSet(set.getVarcount());
		boolean found;
		
		//add pure literals to solution list
		for (int i = 0; i < purelist.size(); i++)
			soln.add(purelist.get(i));
		
		//remove clauses that contain pure literals
		for (int i = 0; i < forms.size(); i++)
		{
			found = false;
			
			for (int j = 0; j < purelist.size() && !found; j++)
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
	//returns 0 on empty set
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
	
	private static Formula generateBlockingClause(int[] solution) {
		Formula bc = new Formula();
		
		//if literal is not 0 -> matters in the assignment
		//add the negation to the new blocking clause
		for (int i = 0; i < solution.length; i++)
			if (solution[i] != 0)
				bc.addValue(-solution[i]);
		return bc;
	}
	
	public boolean solve(ArrayList<ArrayList<Integer>> dimacs, int num_vars) {
		
		//dimacs find variables
		System.out.println(num_vars + " vars");
		FormulaSet forms = new FormulaSet(num_vars);
		Formula blocking;
		ArrayList<Integer> solution = new ArrayList<Integer>();

		forms.setFormulas(dimacs);
		forms.toConsole();

		boolean sat = DPLL.runDPLL(forms, solution);
		System.out.println(sat);
		
		//print solution
		if (sat)
		{
			int[] soln = DPLL.finalSoln(solution, forms.getVarcount());
			for (int i = 0; i < soln.length; i++)
				System.out.print(soln[i] + " ");
			System.out.println(" ");
		}
		
		return sat;
	}
	
	public ArrayList<ArrayList<Integer>> allSolve(ArrayList<ArrayList<Integer>> dimacs, int num_vars) {
		
		//dimacs find variables
		System.out.println(num_vars + " vars");
		FormulaSet forms, copy;
		Formula blocking;
		ArrayList<ArrayList<Integer>> allsol = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> solution = new ArrayList<Integer>();
		boolean sat;
		
		forms = new FormulaSet(num_vars);
		forms.setFormulas(dimacs);
		forms.toConsole();
		
		//since removal destroys original object
		//copy = forms.copySet();

		do {
			
			//sat = DPLL.runDPLL(copy, solution);
			sat = DPLL.runDPLL(forms, solution);
			System.out.println(sat);
			//print solution
			if (sat)
			{
				int[] soln = DPLL.finalSoln(solution, forms.getVarcount());
				for (int i = 0; i < soln.length; i++)
					System.out.print(soln[i] + " ");
				System.out.println(" ");
				
				blocking = generateBlockingClause(soln);
				allsol.add(convert(soln));
				
				//forms = new FormulaSet(num_vars);
				forms.addFormula(blocking);
				System.out.println("After Dimacs");
				forms.toConsole();
				
				//recopy the original set with the blocking clause
				//copy = forms.copySet();
				
				//zero solution array
				solution.clear();
			}
			
		} while (sat);
		
		
		return allsol;
		
	}
	
	private static ArrayList<Integer> convert(int[] arr) {
		ArrayList<Integer> list = new ArrayList<Integer>(arr.length);
		
		for (int i = 0; i < arr.length; i++)
			list.add(arr[i]);
		return list;
	}
	
//	public void allSolve(ArrayList<ArrayList<Integer>> dimacs, int num_vars) {
//		boolean sat = true;
//		FormulaSet forms = new FormulaSet(num_vars);
//		forms.setFormulas(dimacs);
//		forms.toConsole();
//		
//		while (sat)
//		{
//			sat = solve(dimacs, num_vars);
//			//remake original set
//			//will not have to do this once removal is gone
//			forms = new FormulaSet(num_vars);
//			forms.setFormulas(dimacs);
//		}
//	}
	
	private static int[] finalSoln(ArrayList<Integer> soln, int vars) throws IndexOutOfBoundsException {
		int[] assign = new int[vars];
		boolean[] found = new boolean[vars];
		Arrays.fill(found, false);
		Arrays.fill(assign, 0);
		
		//reverse
		for (int i = soln.size() - 1 ; i >= 0; i--)
		{
			if (!found[Math.abs(soln.get(i)) - 1])
			{
				assign[Math.abs(soln.get(i)) - 1] = soln.get(i);
				found[Math.abs(soln.get(i)) - 1] = true;
			}
		}
			
		return assign;
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
		
//		int num_vars = 4;
//		
//		int set[][] = {
//				{1, 2, 3},
//				{1,4},
//				{-2, -3},
//				{-1, -3},
//		};
//		
//
//		
//
//
//		FormulaSet forms = new FormulaSet(num_vars);
//		FormulaSet newset = new FormulaSet(num_vars);
//		
//
//		boolean sat = DPLL.runDPLL(forms);
//		System.out.println(sat);
	}

}
