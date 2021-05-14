/**
 * 
 */
package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sam_t
 *
 * The FormulaSet contains a set of Formula objects. 
 */
public class FormulaSet {
	private List<Formula> formulas;
	private int varcount;
	
	/*
	 * FormulaSet constructor
	 * @members
	 *  	default formula list
	 *  	variable count = method arguemnt num_vars
	 */
	public FormulaSet(int num_vars) {
		this.formulas = new ArrayList<Formula>();
		this.varcount = num_vars;
	}
	
	/*
	 * FormulaSet constructor
	 * Creates a new reference to a FormulaSet object
	 * @members
	 * 		formulas = set formulas
	 * 		varcount = set varcount
	 */
	public FormulaSet(FormulaSet set) {
		this.formulas = set.formulas;
		this.varcount = set.varcount;
	}

	/*
	 * Getter for the formulas member variable
	 * @return
	 * 	formulas as a List of Formula
	 */
	public List<Formula> getFormulas() {
		return formulas;
	}

	/*
	 * Setter for the formulas member variable
	 * @params
	 * 	formulas as a list of Formula
	 */
	public void setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
	}
	
	/*
	 * Adds a Formula object to the Set of Formulas
	 * @params
	 * 	formula as a Formula
	 */
	public void addFormula(Formula formula) {
		this.formulas.add(formula);
	}
	
	/*
	 * remove
	 */
	public void removeFormula(int idx) throws IndexOutOfBoundsException {
		this.formulas.remove(idx);
	}
	
	/*
	 * Getter for the varcount member variable
	 * @returns
	 * 	varcount as an int
	 */
	public int getVarcount() {
		return varcount;
	}

	/*
	 * Setter for the varcount member variable
	 * @params
	 * 	varcount as an int
	 */
	public void setVarcount(int varcount) {
		this.varcount = varcount;
	}
	
	/*
	 * Copy FormulaSet members to new object
	 */
	public FormulaSet copySet() {
		FormulaSet copy = new FormulaSet(this.varcount);
				
		for (int i = 0; i < this.getFormulas().size(); i++) 
		{
			Formula f = new Formula();
			for (int j = 0; j < this.getFormulas().get(i).getFormula().size(); j++)
			{
				f.addValue(this.getFormulas().get(i).getFormula().get(j));
			}
			copy.addFormula(f);
		}
		
		return copy;
	}

	/*
	 * Setter for the formuulas member variable
	 * This function creates new Formula objects and adds them to the existing FormulaSet object
	 * 
	 * @params
	 * 	formulas as an ArrayList of Arraylist of Integer eg. [[-1, 2, 3], [1, -3]]
	 */
	public void setFormulas(ArrayList<ArrayList<Integer>> formulas) {
		Formula form;
		for (int i = 0; i < formulas.size(); i++)
		{
			form = new Formula();
			for (int j = 0; j < formulas.get(i).size(); j++)
			{
				form.addValue(formulas.get(i).get(j));
			}
			this.formulas.add(form);
		}
	}
	
	/*
	 * Determines if a FormulaSet is consistent
	 * Consistency is defined as literal purity for all remaining literals in the FormulaSet
	 * Method should be used after finding the unique literal count using countLiteralsUnique()
	 * 
	 * @params
	 * 	counts as an integer array of unique literal counts
	 * 
	 * @returns
	 * 	boolean result
	 */
	/*
	 * Improvements - if unique counts are only used for this function, there is no need to count the entire FormulaSet. Just until lack of consistency
	 */
	public boolean isConsistent(int[] counts) {
		for (int i = 0; i < this.varcount; i++)
		{
			if (i + varcount < counts.length)
			{
				if (counts[i] > 0 && counts[i + varcount] > 0)
					return false;
			}
		}
		return true;
	}
	
	/*
	 * Determines if the FormulaSet contains an empty Formula
	 * Empty is defined as a Formula containing no literals, or with size 0
	 * 
	 * @returns 
	 *  boolean result
	 */
	public boolean hasEmptyClause() {
		for (int i = 0; i < this.formulas.size(); i++)
			if (this.formulas.get(i).getFormula().size() < 1)
				return true;
		return false;
	}
	
	/*
	 * Counts all of the unique literals in the FormulaSet.
	 * 
	 * @returns
	 * 	The unique literal count for the number of variables in the FormulaSet
	 * 	The int[] will be of size variables*2 
	 *  eg of array produced. The integers in the example indicate the location of varaible counts. 
	 *  [1,2,3,4,5,-1,-2,-3,-4,-5]
	 */
	public int[] countLiteralsUnique() {
		
		int lit_count[] = new int[this.varcount* 2];
		ArrayList<Formula> set = (ArrayList<Formula>) this.getFormulas();
		
		for (int i = 0; i < set.size(); i++)
		{
			for (int j = 0; j < set.get(i).getFormula().size(); j++)
			{
				int literal = set.get(i).getFormula().get(j);
				
				//count literals
				if (literal > 0)
					lit_count[literal - 1]++;
				else 
					lit_count[Math.abs(literal) + this.varcount - 1]++;
			}
		}
		
		return lit_count;
	}

	/*
	 * Counts all of the literals in the FormulaSet
	 * 
	 * @returns 
	 * 	integer array with total count for literals in the formula
	 */
	public int[] countLiterals() {
		
		int lit_count[] = new int[this.varcount];
		ArrayList<Formula> set = (ArrayList<Formula>) this.formulas;
		
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

	
	/*
	 * Prints the contents of the FormulaSet to the console in a readable format.
	 */
	public void toConsole() {
		for (int i = 0; i < this.formulas.size(); i++)
		{
			System.out.print("[");
			for (int j = 0; j < this.formulas.get(i).getFormula().size(); j++)
				System.out.print(this.formulas.get(i).getFormula().get(j) + ",");
			System.out.println("]");
		}
	}


	
	
	
	
	
	
	
	
}
