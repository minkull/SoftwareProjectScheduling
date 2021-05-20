package PSP;

import org.opt4j.common.random.Rand;

import com.google.inject.Inject;

/**
 * Performs crossover between two PSPGenotype p1 and p2 by randomly choosing with equal probabilities
 * between PSPCrossoverTask or PSPCrossoverEmployee
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PSPCrossoverMixedEmployeeTask extends PSPCrossover {

	private PSPCrossoverEmployee crossEmp;
	private PSPCrossoverTask crossTask;
	
	@Inject
	public PSPCrossoverMixedEmployeeTask(Rand random, PSPDecoder dec) {
		super(random, dec);
		crossEmp = new PSPCrossoverEmployee(random,dec);
		crossTask = new PSPCrossoverTask(random,dec);
	}

	@Override
	protected void crossover(PSPGenotype p1, PSPGenotype p2, PSPGenotype o1,
			PSPGenotype o2) {
		
		//System.out.println("mixed cross");
		
		if (random.nextDouble() < 0.5d) 
			crossEmp.crossover(p1, p2, o1, o2);
		else crossTask.crossover(p1, p2, o1, o2);

	}

}
