package PSP;

/**
 * Translates the genotype, which consists of an ArrayList<Integer> into
 * a phenotype, which is a matrix of dedications of employees to tasks.
 */

import org.opt4j.core.problem.Decoder;

import com.google.inject.Inject;

/**
 * A {@link Decoder} for PSP.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PSPDecoder implements Decoder<PSPGenotype, PSPPhenotype> {

	public PSPInstance inst;
	private PSPEvolutionaryParams eparams;
	
	@Inject
	public PSPDecoder(PSPInstance inst, PSPEvolutionaryParams eparams) {
		super();
		this.inst = inst;
		this.eparams = eparams;
	}
	
	public PSPPhenotype decode(PSPGenotype gen) {
		PSPPhenotype phen = new PSPPhenotype(inst.getEmployeeNumber(), inst.getTaskNumber());
		
		// For each employee and task
		for (int e=0; e<inst.getEmployeeNumber(); ++e) {
			for (int t=0; t<inst.getTaskNumber(); ++t) {

				if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS ||
				    inst.employeeHasAllNecessarySkills(e,t)) {
					double d = (double)gen.get(genotypePositionFromPhenotype(e,t))/(double)eparams.getK();
					phen.setEmployeeTaskDedication(e, t, d);
				} else {
					phen.setEmployeeTaskDedication(e, t, 0d);
				}
			}
		}
		return phen;
	}
	
	public int genotypePositionFromPhenotype(int e, int t) {
		return e * inst.getTaskNumber() + t; 
	}
}
