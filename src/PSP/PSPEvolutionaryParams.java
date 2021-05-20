package PSP;

import org.opt4j.start.Constant;

import com.google.inject.Inject;

/**
 * Parameters of the genotype and fitness function for PSP. 
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPEvolutionaryParams {
	
	public enum SkillConstraintMode {
		/**
		 * Our skill contraint mode: all the employees performing a task must have all the necessary
		 * skills for that task.
		 */
		EMPLOYEES_ALL_SKILLS,
		/**
		 * Alba and Chicano's constraint mode: the union of all employees assigned to a task must
		 * have all the necessary skills for that task.
		 */
		UNION_EMPLOYEES_ALL_SKILLS;
	}
	
	public static double EMPLOYEE_MAX_DEDICATION = 1d;
	
	private int k;
	private int initGenotypeValue;
	private boolean normalisation;
	private double wCost;
	private double wDuration;
	private SkillConstraintMode skillConstraintMode; // 0 ours, 1 alba and chicano
	// ours: in order for an employee to be able to work for a task, it needs to have all the skills necessary for this task.
	// in the implementation, this will make the phenotype always contain zero for employee e and task t if e does not have
	// all the skills necessary for t. In this case, the fitness will be wCost * cost + wDur * dur if the solution is
	// feasible and wUndt * undt if the phenotype shows that undt tasks have no employee associated.
	// alba and chicano's: any employee can work for a task as long as the union of skills of all employees working
	// for this task contains the skills necessary for this task (eq. 4 in their paper).
	// In our implementation, this will not touch the conversion from genotype to phenotype, but will influence the
	// evaluation. In this case, the fitness of a feasible solution is wCost * cost + wDur * dur.
	// However, there are different cases of unfeasible solutions:
	// wUndt * undt + wReqsk * sumReqsk.
	// If there is a task with no associated to it (undt > 0), wUndt * undt is used. Remember that there is no change
	// If there is a task with employees associated, but not enough skills, reqsk is used.
	
	@Inject
	public PSPEvolutionaryParams(
			@Constant(value = "k", namespace = PSPEvolutionaryParams.class) int k,
			@Constant(value = "wCost", namespace = PSPEvolutionaryParams.class) double wCost,
			@Constant(value = "wDuration", namespace = PSPEvolutionaryParams.class) double wDuration,
			@Constant(value = "skillConstraintMode", namespace = PSPEvolutionaryParams.class) SkillConstraintMode skillConstraintMode,
			@Constant(value = "initGenotypeValue", namespace = PSPEvolutionaryParams.class) int initGenotypeValue,
			@Constant(value = "normalisation", namespace = PSPEvolutionaryParams.class) boolean normalisation) {
		this.k = k;
		this.initGenotypeValue = initGenotypeValue;
		this.wCost = wCost;
		this.wDuration = wDuration;
		this.skillConstraintMode = skillConstraintMode;
		this.normalisation = normalisation;
	}
	
	// The parameter k to set the possible dedications to a task, which are in {0/k, 1/k, ..., k/k}.
	public int getK() {
		return k;
	}
	
	public int getInitGenotypeValue() {
		return initGenotypeValue;
	}
	
	public boolean getNormalisation() {
		return normalisation;
	}
	
	public double wCost() {
		return wCost;
	}
	
	public double wDuration() {
		return wDuration;
	}
	
	public SkillConstraintMode getSkillConstraintMode() {
		return skillConstraintMode;
	}
}
