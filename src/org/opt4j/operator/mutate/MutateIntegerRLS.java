package org.opt4j.operator.mutate;

import java.util.Random;

import org.opt4j.common.random.Rand;
import org.opt4j.genotype.IntegerGenotype;

import com.google.inject.Inject;

/**
 * The {@link MutateIntegerRLS} mutates exactly one element of the
 * {@link IntegerGenotype}, chosen uniformly at random. Here, a new value is created
 * randomly between the lower and upper bounds.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class MutateIntegerRLS implements MutateInteger {

	protected final Random random;

	/**
	 * Constructs a {@link MutateIntegerRandom}.
	 * 
	 * @param random
	 *            the random number generator
	 */
	@Inject
	public MutateIntegerRLS(Rand random) {
		this.random = random;
	}

	/**
	 * Mutate a {@link IntegerGenotype}.
	 * Choose *one* random position to mutate and replace its value by another
	 * value choosing uniformly at random from U(lb,ub) \ v, where v is the current value.
	 * The probability of mutation p is ignored.
	 */
	@Override
	public void mutate(IntegerGenotype genotype, double p) {

		// choose *one* random position to mutate
		int pos = random.nextInt(genotype.size());
		
		int lb = genotype.getLowerBound(pos);
		int ub = genotype.getUpperBound(pos);
		//int value = random.nextInt(ub - lb + 1) + lb; this would take any value in U(lb,ub)
		
		// avoid getting a new random number that is the same as the current one in the genotype
		int value = random.nextInt(ub - lb) + lb;
		if (value >= genotype.get(pos))
			value++;
		
		genotype.set(pos, value);
		//System.out.println("RLS mutation.");
		
	}

}
