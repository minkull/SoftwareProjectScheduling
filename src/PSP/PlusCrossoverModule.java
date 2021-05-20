package PSP;

import org.opt4j.operator.crossover.BasicCrossoverModule;
import org.opt4j.config.annotations.Info;

/**
 * An extended {@link CrossoverModule}.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PlusCrossoverModule extends BasicCrossoverModule {
	
	@Info("The type of the crossover operator for the PSP genotype.")
	protected PspCrossoverType pspCrossoverType = PspCrossoverType.MIXED_EMPLOYEE_TASK;

	/*@Required(property = "pspCrossoverType", elements = { "EMPLOYEE" })
	@Info("The probability for a crossover.")
	@Constant(value = "crossoverRate", namespace = PSPCrossoverEmployee.class)
	protected double crossoverRate = 0.5;*/
		
	/**
	 * Type of {@code Crossover} operator for the {@code FloatGenotype}.
	 *
	 */
	public enum PspCrossoverType {
		/**
		 * Use the {@link PSPCrossoverEmployee}.
		 */
		EMPLOYEE,
		TASK,
		MIXED_EMPLOYEE_TASK;
	}
	
	public PlusCrossoverModule() {
		super();
	}

			
	/**
	 * Return the {@code Operator} for {@code FloatGenotype}.
	 * 
	 * @return the operator
	 */
	public PspCrossoverType getPspCrossoverType() {
		return pspCrossoverType;
	}

	/**
	 * Sets the {@code Operator} for {@code FloatGenotype}.
	 * 
	 * @param floatType
	 *            the operator
	 */
	public void setPspCrossoverType(PspCrossoverType pspCrossoverType) {
		this.pspCrossoverType = pspCrossoverType;
	}
	
	/**
	 * Returns the crossoverRate value.
	 * 
	 * @return the crossoverRate
	 */

	/*public double getCrossoverRate() {
		return crossoverRate;
	}

	/**
	 * Sets the crossoverRate value.
	 * 
	 * @param crossoverRate
	 *            the crossoverRate to set
	 */
	/*public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opt4j.start.Opt4JModule#config()
	 */
	@Override
	public void config() {
		super.config();
		
		addOperator(PSPCrossover.class);
		
		switch (pspCrossoverType) {
		case EMPLOYEE:
			bind(PSPCrossover.class).to(PSPCrossoverEmployee.class).in(SINGLETON);
			break;		
		case TASK:
			bind(PSPCrossover.class).to(PSPCrossoverTask.class).in(SINGLETON);
			break;	
		case MIXED_EMPLOYEE_TASK:
			bind(PSPCrossover.class).to(PSPCrossoverMixedEmployeeTask.class).in(SINGLETON);
			break;	
		}

		
	}
}



