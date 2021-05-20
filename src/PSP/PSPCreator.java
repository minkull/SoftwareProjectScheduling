package PSP;

import java.util.Random;

import org.opt4j.common.random.Rand;
import org.opt4j.core.problem.Creator;

import com.google.inject.Inject;

/**
 * A {@link Creator} for the PSP problem.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PSPCreator implements Creator<PSPGenotype> {

	Random rand;
	PSPInstance pspInstance;
	PSPEvolutionaryParams eparams;

	@Inject
	public PSPCreator(PSPInstance pspInstance, PSPEvolutionaryParams eparams, Rand rand) {
		this.rand = rand;
		this.pspInstance = pspInstance;
		this.eparams = eparams;
	}
	
	public PSPGenotype create() {
		PSPGenotype genotype = new PSPGenotype(eparams.getK(), 
											   pspInstance.getEmployeeNumber() * pspInstance.getTaskNumber());
		
		if (eparams.getInitGenotypeValue() < 0)
			genotype.init(rand);
		else 
			genotype.init(eparams.getInitGenotypeValue());
		
		return genotype;
	}
}
