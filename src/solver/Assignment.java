/**
 * 
 */
package solver;

/**
 * @author sam_t
 *
 */
public class Assignment {
	
	private int[] solution;
	
	public Assignment(int num_vars) {
		this.solution =  new int[num_vars];
	}
	
	public Assignment(int[] sol) {
		this.solution = sol;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}
	
	public void setSolValue(int idx, int val) throws IndexOutOfBoundsException {
		this.solution[idx] = val;
	}
	
	public boolean isValueAssigned(int val) throws IndexOutOfBoundsException {
		if (this.solution[Math.abs(val) - 1] != 0)
			return true;
		return false;
	}
	
	public boolean allValuesAssigned() {
		for (int i = 0; i < this.solution.length; i++)
			if (solution[i] == 0)
				return false;
		return true;
	}
	
//	public int makeFirstAssignment() {
//		
//	}
//	
//	//
//	//function like this in DPLL
//	public int makeAssignmentHigh(int[] counts) {
//		
//	}
}
