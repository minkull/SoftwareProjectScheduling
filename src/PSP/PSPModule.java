package PSP;

import org.opt4j.config.annotations.File;
import org.opt4j.config.annotations.Info;
import org.opt4j.config.annotations.Order;
import org.opt4j.core.problem.ProblemModule;
import org.opt4j.start.Constant;

/**
 * A PSP {@link ProblemModule}.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class PSPModule extends ProblemModule {

	@Info("The data set to be used for MLP's evaluation.")
	@Order(1)
	@File
	@Constant(value = "pspInstanceFileName", namespace = PSPInstance.class)
	protected String pspInstanceFileName = "";

	@Info("The parameter k to set the possible dedications to a task, which are in {0/k, 1/k, ..., k/k}.")
	@Order(2)
	@Constant(value = "k", namespace = PSPEvolutionaryParams.class)
	protected int k = 7;
	
	@Info("Value for *genotype* initiallization (if less than zero, init randomly). Should be at most k.")
	@Order(3)
	@Constant(value = "initGenotypeValue", namespace = PSPEvolutionaryParams.class)
	protected int initGenotypeValue = -1;
	
	@Info("Weight for the cost term of the fitness function.")
	@Order(4)
	@Constant(value = "wCost", namespace = PSPEvolutionaryParams.class)
	protected double wCost = 1e-6;

	@Info("Weight for the cost term of the fitness function.")
	@Order(5)
	@Constant(value = "wDuration", namespace = PSPEvolutionaryParams.class)
	protected double wDuration = 0.1;
	
	@Info("Weight for the cost term of the fitness function.")
	@Order(6)
	@Constant(value = "skillConstraintMode", namespace = PSPEvolutionaryParams.class)
	protected PSPEvolutionaryParams.SkillConstraintMode skillConstraintMode = PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS;
	
	@Info("Whether or not to use normalisation.")
	@Order(7)
	@Constant(value = "normalisation", namespace = PSPEvolutionaryParams.class)
	protected boolean normalisation = true;
	
	public PSPModule() {
		super();
	}
	
	public String getPspInstanceFileName() {
		return pspInstanceFileName;
	}
	
	public void setPspInstanceFileName(String pspInstanceFileName) {
		this.pspInstanceFileName = pspInstanceFileName;
	}
	
	public int getK() {
		return k;
	}
	
	public int getInitGenotypeValue() {
		return initGenotypeValue;
	}
	
	public boolean getNormalisation() {
		return normalisation;
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public void setNormalisation(boolean normalisation) {
		this.normalisation = normalisation;
	}
	
	public void setInitGenotypeValue(int initGenotypeValue) {
		this.initGenotypeValue = initGenotypeValue;
	}
	
	public void setWCost(double wCost) {
		this.wCost = wCost;
	}
	
	public void setWDuration(double wDuration) {
		this.wDuration = wDuration;
	}
	
	public void setSkillConstraintMode(PSPEvolutionaryParams.SkillConstraintMode skillConstraintMode) {
		this.skillConstraintMode = skillConstraintMode;
	}
	
	public double getWCost() {
		return wCost;
	}
	
	public double getWDuration() {
		return wDuration;
	}
	
	public PSPEvolutionaryParams.SkillConstraintMode getSkillConstraintMode() {
		return skillConstraintMode;
	}
	
	@Override
	protected void config() {
		bindProblem(PSPCreator.class, PSPDecoder.class, PSPEvaluator.class);
	}

}
