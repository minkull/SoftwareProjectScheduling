package PSP;

/**
 * The phenotype is a matrix employees vs tasks in which each position ij is the
 * dedication of employee i to task j. The dedication is a double value in {0/j,1/k,...,k/k}.
 * The dedication is NOT NORMALIZED here yet. This needs to be done during the evaluation, as it
 * depends on which tasks are performed at the same time.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 */
import java.util.Vector;

import org.opt4j.core.Phenotype;

public class PSPPhenotype extends Vector<Vector<Double>> implements Phenotype{

	private static final long serialVersionUID = -8890417810746004464L;
	
	public PSPPhenotype(int employeeNumber, int taskNumber) {
		super(employeeNumber); // capacity of outer vector is employeeNumber
		
		// Allocate each position of the matrix
		for (int i=0; i<employeeNumber; ++i) {
			this.add(new Vector<Double>(taskNumber));
			this.get(i).setSize(taskNumber);
		}
	}
	
	public void setEmployeeTaskDedication(int e, int t, double d) {
		this.get(e).set(t, d);
	}
	
	public double getEmployeeTaskDedication(int e, int t) {
		return this.get(e).get(t);
	}
	
	public String toString() {
		String ets = "";
		for (int e=0; e<this.size(); ++e)
			for (int t=0; t<this.get(e).size(); ++t)
				ets += this.get(e).get(t) + ",";
		return ets;
	}

}
