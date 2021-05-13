/**
 * 
 */
package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sam_t
 *
 */
public class FormulaSet {
	private List<Formula> formulas;
	private int varcount;
	
	public FormulaSet(int num_vars) {
		this.formulas = new ArrayList<Formula>();
		this.varcount = num_vars;
	}
	
//	public FormulaSet(ArrayList<ArrayList<Integer>> dimacs, int varcount) {
//		this.formulas = new ArrayList<Formula>();
//		setFormulas(dimacs);
//		this.varcount = varcount;
//	}
	
//	public FormulaSet(FormulaSet set) {
//		this.formulas = new ArrayList<Formula>();
//		setFormulas(set.getFormulas());
//		this.varcount = set.getVarcount();
//	}
	
	public FormulaSet(FormulaSet set) {
		this.formulas = set.formulas;
		this.varcount = set.varcount;
	}

	public List<Formula> getFormulas() {
		return formulas;
	}

	public void setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
	}
	
	public void addFormula(Formula formula) {
		this.formulas.add(formula);
	}
	
	public void removeFormula(int idx) throws IndexOutOfBoundsException {
		this.formulas.remove(idx);
	}
	
	public int getVarcount() {
		return varcount;
	}

	public void setVarcount(int varcount) {
		this.varcount = varcount;
	}
	
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
	
//	public void setFormulas(int[][] formulas) {
//		Formula form;
//		for (int i = 0; i < formulas.length; i++)
//		{
//			form = new Formula();
//			for (int j = 0; j < formulas[i].length; j++)
//			{
//				form.addValue(formulas[i][j]);
//			}
//			this.formulas.add(form);
//		}
//	}
	
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
	
	public boolean hasEmptyClause() {
		for (int i = 0; i < this.formulas.size(); i++)
			if (this.formulas.get(i).getFormula().size() < 1)
				return true;
		return false;
	}
	
	// [ + + + + + + + - - - - - - -] 
	// positive values first
	// negated literals at positive_idx + total_num_vars
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

	
	//find pure literals
	//find unit clauses
	
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
