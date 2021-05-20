package org.opt4j.operator.mutate;


import org.opt4j.config.annotations.Info;

/**
 * An extended {@link MutateModule}.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */
public class PlusMutateModule extends BasicMutateModule {

	@Info("The type of the mutate operator for the Integer genotype.")
	protected IntegerMutateType integerMutateType = IntegerMutateType.RLS;
	
	/**
	 * Type of {@code Mutate} operator for the {@code IntegerGenotype}.
	 */
	public enum IntegerMutateType {
		RANDOM,
		RLS;
	}
	
	public PlusMutateModule() {
		super();
	}

	/**
	 * Returns the integer mutation type.
	 * 
	 * @return the integerMutateType
	 */
	public IntegerMutateType getIntegerMutateType() {
		return integerMutateType;
	}

	/**
	 * Sets the integerMutateType.
	 * 
	 * @param integerMutateType
	 *            the integerMutateType to set
	 */
	public void setIntegerMutateType(IntegerMutateType integerMutateType) {
		this.integerMutateType = integerMutateType;
	}
	
	@Override
	public void config() {				
		super.config();
		
		addOperator(MutateIntegerRLS.class);
		
		switch (integerMutateType) {
		case RANDOM:
			bind(MutateInteger.class).to(MutateIntegerRandom.class).in(SINGLETON);
			break;
		case RLS:
			bind(MutateInteger.class).to(MutateIntegerRLS.class).in(SINGLETON);
			break;
		}
	}

}
