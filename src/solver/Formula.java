package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sam_t
 * 
 * The Formula class is a representation of a propositional formula, converted into integer form. A formula consists
 * of a set of these integer values
 *
 * Member Variables
 * 	Values - An ArrayList of Integers
**/
public class Formula {
	
	private List<Integer> values;
	
	/*
	 * Default Constructor - Instantiates members
	 */
	Formula() {
		values = new ArrayList<Integer>(); 
	}
	
	/*
	 * Set the value member variable to the method argument
	 * 
	 * @params
	 * 	values as a List<Integer> 	
	 */
	public void setFormula(List<Integer> values) {
		this.values = values;
	}
	
	/*
	 * Getter for the values member
	 * 
	 * @return
	 * 	values as an Integer list
	 */
	public List<Integer> getFormula() {
		return values;
	}
	
	/*
	 * Add a value to the Formula
	 * 
	 * @params
	 *  int val to add
	 */
	public void addValue(int val) {
		this.values.add(val);
	}
	
	public Formula addAllBut(int val) {
		Formula newform = new Formula();
		for (int i = 0; i < this.getFormula().size(); i++)
			if (this.getFormula().get(i) != val)
				newform.addValue(this.getFormula().get(i));
		return newform;
	}

	/*
	 * Determines the satisfiability of the Formula
	 * Checks the formula against the method argument. If a match is found
	 * the method returns true, if not found false.
	 * 
	 * @params
	 * 	literal as an int
	 * 
	 * @return
	 * 	boolean
	 */
	public boolean isSatisfiedBy(int literal) {
		for (int i = 0; i < this.values.size(); i++)
			if (this.values.get(i) == literal)
				return true;
		return false;
	}
	
	/*
	 * remove
	 */
	public void removeUnit(int val) {
		for (int i = 0; i < this.values.size(); i++)
			if (this.values.get(i) == val)
				this.values.remove(i);
	}
	
}
