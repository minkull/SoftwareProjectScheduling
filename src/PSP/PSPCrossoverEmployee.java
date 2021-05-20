
/**
 * Performs crossover between two PSPGenotype p1 and p2. PSPGenotype is a vector representing a 
 * matrix of employees raw dedications to tasks. For each employee, PSPCrossoverEmployee randomly selects
 * a line of dedications from either p1 or p2 to compose the offspring.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

package PSP;

import org.opt4j.common.random.Rand;

import com.google.inject.Inject;

public class PSPCrossoverEmployee extends PSPCrossover {

	//protected final double crossoverRate;
	
	@Inject
	public PSPCrossoverEmployee(Rand random, PSPDecoder dec) { 
		//@Constant(value = "crossoverRate", namespace = PSPCrossoverEmployee.class) double crossoverRate) {
		super(random, dec);
		//this.crossoverRate = crossoverRate;
	}

	/**
	 * Performs crossover between p1 and p2 and stores result in o1 and o2. 
	 * For each employee, randomly selects
	 * a line of dedications from either p1 or p2 to compose the offspring.
	 * CAUTION!!! This assumes that o1 and o2's internal vector is already allocated with the correct size.
	 * If the size is different from p1 or p2, this method will not perform crossover. 
	 */
	@Override
	protected void crossover(PSPGenotype p1, PSPGenotype p2, PSPGenotype o1, PSPGenotype o2) {
		
		if (p1.size() != p2.size() || p1.size() != o1.size() || p1.size() != o2.size()) {
			System.out.println("Err: crossover not performed -- parent or offspring with incorrect size.");
			return;
		}
		
		//System.out.println("employee cross");
		
		//System.out.println("Doing xover");
		// For each employee
		for (int e=0; e<dec.inst.getEmployeeNumber(); ++e) {
			
			// Randomly choose between p1 and p2 to copy the dedications of employee e to o1 and o2.
			if (random.nextDouble() < 0.5d) {
				for (int t=0; t<dec.inst.getTaskNumber(); ++t) {
					int pos = dec.genotypePositionFromPhenotype(e,t);
					o1.set(pos, p1.get(pos));
					o2.set(pos, p2.get(pos));
				}
					
			} else {
				for (int t=0; t<dec.inst.getTaskNumber(); ++t) {
					int pos = dec.genotypePositionFromPhenotype(e,t);
					o1.set(pos, p2.get(pos));
					o2.set(pos, p1.get(pos));
				}
			}
		}

	}

}
