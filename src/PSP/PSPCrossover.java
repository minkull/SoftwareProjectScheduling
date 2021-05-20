package PSP;

import java.util.Random;

import org.opt4j.common.random.Rand;
import org.opt4j.operator.crossover.Crossover;
import org.opt4j.optimizer.ea.Pair;

import com.google.inject.Inject;

/**
 * A generic crossover for {@link PSPGenotype}.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public abstract class PSPCrossover implements Crossover<PSPGenotype> {

	protected final Random random;
	public final PSPDecoder dec;
	
	@Inject
	public PSPCrossover(Rand random, PSPDecoder dec) {
		this.random = random;
		this.dec = dec;
	}

	@Override
	public Pair<PSPGenotype> crossover(PSPGenotype p1, PSPGenotype p2) {
		PSPGenotype o1 = p1.newInstance();
		PSPGenotype o2 = p2.newInstance();
		
		// make sure we have all positions of the genotype already allocated
		o1.init(0);
		o2.init(0);
		
		crossover(p1, p2, o1, o2);

		//normalize.normalize(o1);
		//normalize.normalize(o2);

		Pair<PSPGenotype> offspring = new Pair<PSPGenotype>(o1, o2);
		return offspring;
		
	}
	
	/**
	 * Performs a crossover of two parent PSP genotypes {@code PSPGenotype}.
	 * 
	 * @param p1
	 *            the first parent
	 * @param p2
	 *            the second parent
	 * @param o1
	 *            the first offspring
	 * @param o2
	 *            the second offspring
	 */
	protected abstract void crossover(PSPGenotype p1, PSPGenotype p2,
			PSPGenotype o1, PSPGenotype o2);

}
