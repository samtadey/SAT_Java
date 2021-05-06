package solver;

import java.util.ArrayList;
import java.util.List;

public class Formula {
	
	private List<Integer> values;
	
	Formula() {
		values = new ArrayList<Integer>(); 
	}
	
	void setFormula(List<Integer> values) {
		this.values = values;
	}
	
	List<Integer> getFormula() {
		return values;
	}
	
	void addValue(int val) {
		this.values.add(val);
	}

	boolean isSatisfiedBy(int a) {
		for (int i = 0; i < this.values.size(); i++)
			if (this.values.get(i) == a)
				return true;
		return false;
	}
	
}
