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
		formulas = new ArrayList<Formula>();
		this.varcount = num_vars;
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
	
	public int getVarcount() {
		return varcount;
	}

	public void setVarcount(int varcount) {
		this.varcount = varcount;
	}
	
	public void setFormulas(int[][] formulas) {
		Formula form;
		for (int i = 0; i < formulas.length; i++)
		{
			form = new Formula();
			for (int j = 0; j < formulas[i].length; j++)
			{
				form.addValue(formulas[i][j]);
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
	
	//find pure literals
	//find unit clauses
	
	public void toConsole() {
		for (int i = 0; i < this.formulas.size(); i++)
		{
			for (int j = 0; j < this.formulas.get(i).getFormula().size(); j++)
			{
				System.out.print(this.formulas.get(i).getFormula().get(j));
				System.out.print(",");
			}
			System.out.println(" ");
		}
	}


	
	
	
	
	
	
	
	
}
