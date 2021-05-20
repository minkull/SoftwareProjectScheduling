package PSP;

import junit.framework.TestCase;

/**
 * A tester for {@link PSPInstance}.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPInstanceTest extends TestCase {
	private PSPInstance inst;
	private PSPInstance inst2;
	
	public PSPInstanceTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		inst = new PSPInstance("instance_sample.txt");
		inst2 = new PSPInstance("instance_sample2.txt");
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testReadPspInstanceFile() {
		assertEquals(30, inst.getTaskNumber());
		assertEquals(15, inst.getEmployeeNumber());
		assertEquals(10, inst.getSkillNumber());
		
		assertEquals(2, inst2.getTaskNumber());
		assertEquals(1, inst2.getEmployeeNumber());
		assertEquals(4, inst2.getSkillNumber());
	}
	
	public void testReadCostSalarySkillTPG() {
		assertEquals(9812.344881756286, inst.getEmployeeSalary(0));
		assertEquals(8662d, inst.getEmployeeSalary(13));
		
		assertEquals(3d, inst.getTaskCost(27));
		assertEquals(17d, inst.getTaskCost(12));
		
		assertEquals(true, inst.employeeHasSkill(14,9));
		assertEquals(true, inst.employeeHasSkill(14,7));
		assertEquals(true, inst.employeeHasSkill(14,6));
		assertEquals(true, inst.employeeHasSkill(14,4));
		assertEquals(false, inst.employeeHasSkill(14,8));
		assertEquals(false, inst.employeeHasSkill(14,3));
		assertEquals(false, inst.employeeHasSkill(14,0));
		
		assertEquals(3, inst.getTaskSkillsNumber(5));
		assertEquals(5, (int) inst.getTaskSkill(5,0));
		assertEquals(4, (int) inst.getTaskSkill(5,1));
		assertEquals(1, (int) inst.getTaskSkill(5,2));
		
		int numberArcs = 0;
		for(int t=0; t<inst.getTaskNumber(); ++t) {
			numberArcs += inst.tpg.get(t).size();
		}
		assertEquals(56, numberArcs);
		
		assertEquals(11, inst.tpg.getDependentTaskAtPosition(6, 0));
		assertEquals(11, inst.tpg.getDependentTaskAtPosition(0, 0));
		assertEquals(9, inst.tpg.getDependentTaskAtPosition(5, 0));
		assertEquals(9, inst.tpg.getDependentTaskAtPosition(3, 0));
		assertEquals(8, inst.tpg.getDependentTaskAtPosition(3, 1));
		
		
		assertEquals(1, (int) inst2.getEmployeeNumber());
		for (int i=0; i<4; ++i)
			assertEquals(true, inst2.employeeHasSkill(0, i));
		
		assertEquals(2, inst2.getTaskSkillsNumber(0));
		assertEquals(0, (int) inst2.getTaskSkill(0,0));
		assertEquals(1, (int) inst2.getTaskSkill(0,1));
		assertEquals(2, inst2.getTaskSkillsNumber(1));
		assertEquals(2, (int) inst2.getTaskSkill(1,0));
		assertEquals(3, (int) inst2.getTaskSkill(1,1));
		assertEquals(2, (int) inst2.getTaskNumber());
		assertEquals(7d, inst2.getTaskCost(0));
		assertEquals(8d, inst2.getTaskCost(1));
	}
	
	public void testInDegree() {
		boolean atLeastOneIndependentTask = false;
		for (int t=0; t<inst.getTaskNumber(); ++t)
			if (inst.tpg.getInDegreeTask(t) == 0)
				atLeastOneIndependentTask = true;
		assertEquals(true, atLeastOneIndependentTask);
	}
	
	public void testTaskCostsClone() {
		double [] c = inst.taskCostsClone();
		c[0] = 12345d;
		
		assertFalse(c[0] == inst.getTaskCost(0));
	}
	
	public void testEmployeeHasNecessarySkills() {
		assertEquals(false, inst.employeeHasAllNecessarySkills(10, 9));
		assertEquals(false, inst.employeeHasAllNecessarySkills(10, 14));
		assertEquals(true, inst.employeeHasAllNecessarySkills(10, 4));
	}
}
