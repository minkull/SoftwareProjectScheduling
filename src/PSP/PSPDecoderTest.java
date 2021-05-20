package PSP;

import java.util.Random;

import junit.framework.TestCase;

/**
 * A tester for PSPDecoder.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PSPDecoderTest extends TestCase {
	
	private PSPInstance inst, inst2, inst7;
	private PSPGenotype gen;
	private PSPDecoder dec, dec2, dec7, decu, dec2u, dec7u;
	private PSPEvolutionaryParams eparams, eparamsUnion;
	
	public PSPDecoderTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		inst = new PSPInstance("instance_sample.txt");
		inst2 = new PSPInstance("instance_sample2.txt");
		inst7 = new PSPInstance("instance_sample7.txt");
		eparams = new PSPEvolutionaryParams(5,1e-6,0.1, PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS,-1,true);
		eparamsUnion = new PSPEvolutionaryParams(5,1e-6,0.1, PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS,-1,true);
		gen = new PSPGenotype(eparams.getK(), inst.getEmployeeNumber() * inst.getTaskNumber());
		gen.init(new Random());
		dec = new PSPDecoder(inst,eparams);
		dec2 = new PSPDecoder(inst2,eparams);
		dec7 = new PSPDecoder(inst7,eparams);
		decu = new PSPDecoder(inst,eparamsUnion);
		dec2u = new PSPDecoder(inst2,eparamsUnion);
		dec7u = new PSPDecoder(inst7,eparamsUnion);
		
		for (int i=0; i<gen.size(); ++i)
			gen.set(i, i);
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDecodeUnionEmployeesAllSkillsMode() {
		PSPPhenotype phen = decu.decode(gen);
		
		int i = 0;
		for (int e=0; e<inst.getEmployeeNumber(); ++e)
			for (int t=0; t<inst.getTaskNumber(); ++t) {
				assertEquals((double)i/(double)eparams.getK(), phen.getEmployeeTaskDedication(e, t));
				i++;
			}
		
		phen = dec2u.decode(gen);
		assertEquals(0d, phen.getEmployeeTaskDedication(0, 0));
		assertEquals(0.2d, phen.getEmployeeTaskDedication(0, 1));
		
		PSPGenotype gen7 = new PSPGenotype(eparams.getK(),inst7.getEmployeeNumber() * inst7.getTaskNumber());
		gen7.init(new Random());
		for (int j=0; j<gen7.size(); ++j)
			gen7.set(j, 1);
		
		phen = dec7u.decode(gen7);
		for (int e=0; e<inst7.getEmployeeNumber(); ++e)
			for (int t=0; t<inst7.getTaskNumber(); ++t) {
				assertEquals(1/(double)eparams.getK(), phen.getEmployeeTaskDedication(e, t));
			}
	}
	
	public void testDecodeEmployeesAllSkillsMode() {
		PSPPhenotype phen = dec.decode(gen);
		
		int i = 0;
		for (int e=0; e<inst.getEmployeeNumber(); ++e)
			for (int t=0; t<inst.getTaskNumber(); ++t) {
				if (inst.employeeHasAllNecessarySkills(e, t))
					assertEquals((double)i/(double)eparams.getK(), phen.getEmployeeTaskDedication(e, t));
				else assertEquals(0d, phen.getEmployeeTaskDedication(e, t));
				i++;
			}
		
		phen = dec2.decode(gen);
		assertEquals(0d, phen.getEmployeeTaskDedication(0, 0));
		assertEquals(0.2d, phen.getEmployeeTaskDedication(0, 1));
		
		PSPGenotype gen7 = new PSPGenotype(eparams.getK(),inst7.getEmployeeNumber() * inst7.getTaskNumber());
		gen7.init(new Random());
		for (int j=0; j<gen7.size(); ++j)
			gen7.set(j, 1);
		
		/*boolean taskDoable[] = new boolean[inst7.getTaskNumber()];
		for (int t=0; t<taskDoable.length; ++t)
			taskDoable[t] = false;*/
		
		phen = dec7.decode(gen7);
		for (int e=0; e<inst7.getEmployeeNumber(); ++e)
			for (int t=0; t<inst7.getTaskNumber(); ++t) {
				if (inst7.employeeHasAllNecessarySkills(e, t)) {
					assertEquals(1/(double)eparams.getK(), phen.getEmployeeTaskDedication(e, t));
					//taskDoable[t] = true;
				}
			}
		
		/*for (int t=0; t<inst7.getTaskNumber(); ++t) {
			System.out.println(taskDoable[t]);
			//assertTrue(taskDoable[t]);
		}*/
		
	}
	
	
	public void testGenotypePositionFromPhenotype() {
		int pos = dec.genotypePositionFromPhenotype(inst.getEmployeeNumber()-1, inst.getTaskNumber()-1);
		assertEquals(gen.size()-1, pos);
		
		pos = dec.genotypePositionFromPhenotype(0, 0);
		assertEquals(0, pos);
		
		pos = dec.genotypePositionFromPhenotype(0, inst.getTaskNumber()/2);
		assertEquals(inst.getTaskNumber()/2, pos);
		
		pos = dec.genotypePositionFromPhenotype(inst.getEmployeeNumber()/2, inst.getTaskNumber()/2);
		assertEquals(gen.size()/2, pos);
	}


}
