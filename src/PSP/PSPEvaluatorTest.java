package PSP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import org.opt4j.core.Value;

import junit.framework.TestCase;

/**
 * A tester for {@link PSPEvaluator}.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPEvaluatorTest extends TestCase {
	private PSPInstance inst, inst2, inst7;
	private PSPEvaluator eval, eval2, eval7;
	private PSPEvolutionaryParams eparams, eparamsu, eparamsn;

	
	public PSPEvaluatorTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		inst = new PSPInstance("instance_sample2.txt");
		inst2 = new PSPInstance("instance_sample4.txt");
		inst7 = new PSPInstance("instance_sample7.txt");
		eparams = new PSPEvolutionaryParams(5,1e-6,0.1,PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS,-1,true);
		eparamsu = new PSPEvolutionaryParams(5,1e-6,0.1,PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS,-1,true);
		eparamsn = new PSPEvolutionaryParams(5,1e-6,0.1,PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS,-1,false);
		eval = new PSPEvaluator(inst, eparams);
		eval2 = new PSPEvaluator(inst2, eparams);
		eval7 = new PSPEvaluator(inst7, eparams);
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testUpdateCost() {
		double [] unnormSumDedicationEmployee = {1.5};
		assertEquals(123d + 100d, eval.updateCost(123d, 10d, unnormSumDedicationEmployee));
		
		double [] unnormSumDedicationEmployee2 = {0.7};
		assertEquals(123d + 70d, eval.updateCost(123d, 10d, unnormSumDedicationEmployee2));
		
		double [] unnormSumDedicationEmployee3 = {0.7, 1.1};
		assertEquals(123d + 700000000010d, eval2.updateCost(123d, 10d, unnormSumDedicationEmployee3));
	}
	
	public void testDetermineUnnormSumDedicationEmployee() {
		PSPPhenotype phen = new PSPPhenotype(4, 5);
		for (int e=0; e<4; ++e) 
			for (int t=0; t<5; ++t) 
				phen.setEmployeeTaskDedication(e, t, e+t);
		
		Vector<Integer> curIndpTasks = new Vector<Integer>(3);
		curIndpTasks.add(0);
		curIndpTasks.add(3);
		curIndpTasks.add(1);
		
		assertEquals(7d, eval.determineUnnormSumCurrDedicationEmployee(1, curIndpTasks, phen));
		
		for (int e=0; e<4; ++e) 
			for (int t=0; t<5; ++t) 
				phen.setEmployeeTaskDedication(e, t, (e+t)/100d);
		
		assertEquals(0.07d, eval.determineUnnormSumCurrDedicationEmployee(1, curIndpTasks, phen));
		
		PSPPhenotype phen2 = new PSPPhenotype(1,2);
		for (int e=0; e<1; ++e) 
			for (int t=0; t<2; ++t) 
				phen2.setEmployeeTaskDedication(e, t, 1);
		
		curIndpTasks = eval.determineCurIndependentTasks(inst.tpg.inDegreeClone());
		assertEquals(1d, eval.determineUnnormSumCurrDedicationEmployee(0, curIndpTasks, phen2));
	}
	
	public void testDurrToFinishFistTask() {
		double [] totalDedicationToTask = new double[5];
		double [] efforCurrRequiredToFinishTask = new double[5];
		
		Vector<Integer> curIndependentTasks = new Vector<Integer>();
		
		for (int t=0; t<5; ++t) {
			curIndependentTasks.add(t);
			totalDedicationToTask[t] = (double)t;
			efforCurrRequiredToFinishTask[t] = 5d;
		}
		
		PSPEvaluator.TaskDur dd = eval.durToFinishFirstTask(curIndependentTasks, totalDedicationToTask, efforCurrRequiredToFinishTask);
		assertEquals(1.25d, dd.dur);
		assertEquals(4, dd.tasks.get(0).intValue());
		
		for (int t=0; t<5; ++t) {
			totalDedicationToTask[t] = (double)5-t;
		}
		
		dd = eval.durToFinishFirstTask(curIndependentTasks, totalDedicationToTask, efforCurrRequiredToFinishTask);
		assertEquals(1d, dd.dur);
		assertEquals(0, dd.tasks.get(0).intValue());
		
		curIndependentTasks.remove(0);
		dd = eval.durToFinishFirstTask(curIndependentTasks, totalDedicationToTask, efforCurrRequiredToFinishTask);
		assertEquals(5/4d, dd.dur);
		assertEquals(1, dd.tasks.get(0).intValue());
		
		for (int t=0; t<5; ++t) {
			totalDedicationToTask[t] = 1d;
		}
		dd = eval.durToFinishFirstTask(curIndependentTasks, totalDedicationToTask, efforCurrRequiredToFinishTask);
		assertEquals(4, dd.tasks.size());
		assertEquals(5d, dd.dur);
		
	}
	
	public void testDetermineCurIndependentTasks() {
		int [] inDegree = inst.tpg.inDegreeClone();
		
		Vector<Integer> indpTasks = eval.determineCurIndependentTasks(inDegree);
		assertEquals(1, indpTasks.size());
		assertEquals(0, (int) indpTasks.get(0));
		
		int [] inDegree2 = {1,2,5,0,2,0,2};
		indpTasks = eval.determineCurIndependentTasks(inDegree2);
		assertEquals(2, indpTasks.size());
		assertEquals(3, (int) indpTasks.get(0));
		assertEquals(5, (int) indpTasks.get(1));
	}
	
	public void testRemoveTask() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(2);
		list.add(7);
		list.add(4);
		list.add(1);
		list.remove(Integer.valueOf(1));
		assertEquals(3, list.size());
		assertEquals(2, (int) list.get(0));
		assertEquals(7, (int) list.get(1));
		assertEquals(4, (int) list.get(2));
	}
	
	public void testPessimisticCostDur() {
		assertEquals(75*2d*eparams.wDuration() + 150*2d * eparams.wCost(), eval.pessimisticCostDur());
	}
	
	
	public void testEvaluateNoNormalisation() throws FileNotFoundException, IOException {
		// One employee, two tasks, sequential
		PSPInstance inst = new PSPInstance("instance_sample2.txt");
		PSPEvaluator eval = new PSPEvaluator(inst, eparamsn);
		
		// overwork
		PSPPhenotype phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 1.2);
		phen.setEmployeeTaskDedication(0, 1, 1.3);
		
		PSPObjectives objs = eval.evaluate(phen);
		Value<?> fitness = objs.get(eval.objective[0]);
		PSPObjectives.ObjectiveComponentsInfo valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/1.2 + 8/1.3, valueInfo.duration);
		assertEquals(7 * 10d + 8 * 10d, valueInfo.cost);
		assertEquals(eval.pessimisticCostDur() + 7/1.2 * 0.2 + 8/1.3 * 0.3, fitness.getValue());
		
		// underwork
		phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 0.2);
		phen.setEmployeeTaskDedication(0, 1, 0.3);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/0.2 + 8/0.3, valueInfo.duration);
		assertEquals(7 * 10d + 8 * 10d, valueInfo.cost);
		assertEquals((7/0.2 + 8/0.3) * eparamsn.wDuration() + (7 * 10d + 8 * 10d) * eparamsn.wCost(), fitness.getValue());
		
		// One employee, two tasks, sequential, no employee has necessary abilities
		inst = new PSPInstance("instance_sample6.txt");
		eval = new PSPEvaluator(inst, eparamsn);
		
		PSPGenotype gen = new PSPGenotype(1,2);
		gen.init(new Random());
		for (int i=0; i<gen.size(); ++i)
			gen.set(i,1);
		PSPDecoder dec = new PSPDecoder(inst,eparamsn);
		
		phen = dec.decode(gen);
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(eval.wReqsk() * 4, fitness.getValue());
		
		// One employee, two tasks, parallel, overwork
		inst = new PSPInstance("instance_sample3.txt");
		eval = new PSPEvaluator(inst, eparamsn);
		
		phen.setEmployeeTaskDedication(0, 0, 0.5);
		phen.setEmployeeTaskDedication(0, 1, 0.6);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/0.6 + (7 - (7/0.6) * 0.5) / 0.5, valueInfo.duration);
		assertEquals(7/0.6 * 11d + ((7d - (7/0.6) * 0.5) / 0.5) * 10 * 0.5, valueInfo.cost);
		assertEquals(eval.pessimisticCostDur() + 7/0.6 * (1.1 - 1d), fitness.getValue());
		
		
	}
	
	public void testEvaluateUnionEmployeesAllSkillsMode() throws FileNotFoundException, IOException {
		// One employee, two tasks, sequential, underwork
		PSPInstance inst = new PSPInstance("instance_sample2.txt");
		PSPEvaluator evalu = new PSPEvaluator(inst, eparamsu);
		
		PSPPhenotype phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 0.2);
		phen.setEmployeeTaskDedication(0, 1, 0.3);
		
		PSPObjectives objs = evalu.evaluate(phen);
		Value<?> fitness = objs.get(evalu.objective[0]);
		PSPObjectives.ObjectiveComponentsInfo valueInfo = objs.getInfo(evalu.objective[0]);
		
		assertEquals(7/0.2 + 8/0.3, valueInfo.duration);
		assertEquals(7 * 10d + 8 * 10d, valueInfo.cost);
		assertEquals((7/0.2 + 8/0.3) * eparamsu.wDuration() + (7 * 10d + 8 * 10d) * eparamsu.wCost(), fitness.getValue());
		
		// One employee, two tasks, sequential, no employee has necessary abilities
		inst = new PSPInstance("instance_sample6.txt");
		evalu = new PSPEvaluator(inst, eparamsu);
		
		PSPGenotype gen = new PSPGenotype(1,2);
		gen.init(new Random());
		for (int i=0; i<gen.size(); ++i)
			gen.set(i,1);
		PSPDecoder dec = new PSPDecoder(inst,eparamsu);
		
		phen = dec.decode(gen);
		objs = evalu.evaluate(phen);
		fitness = objs.get(evalu.objective[0]);
		valueInfo = objs.getInfo(evalu.objective[0]);
		
		assertEquals(evalu.wReqsk() * 4, fitness.getValue());
		
		// One employee, two tasks, sequential, overwork
		phen.setEmployeeTaskDedication(0, 0, 1.1);
		phen.setEmployeeTaskDedication(0, 1, 0.6);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7 + 8/0.6, valueInfo.duration);
		assertEquals(70d + 80d, valueInfo.cost);
		assertEquals((7 + 8/0.6) * eparamsu.wDuration() + (70d + 80d) * eparamsu.wCost(), fitness.getValue());
		
		// One employee, two tasks, parallel, underwork
		inst = new PSPInstance("instance_sample3.txt");
		eval = new PSPEvaluator(inst, eparamsu);
		
		phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 0.2);
		phen.setEmployeeTaskDedication(0, 1, 0.4);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(17.5 + 17.5, valueInfo.duration);
		assertEquals(17.5 * 10 * 0.6 + 17.5 * 10 * 0.2, valueInfo.cost);
		assertEquals((17.5 + 17.5) * eparamsu.wDuration() + (17.5 * 10 * 0.6 + 17.5 * 10 * 0.2) * eparamsu.wCost(), fitness.getValue());
		
		// One employee, two tasks, parallel, overwork
		phen.setEmployeeTaskDedication(0, 0, 0.5);
		phen.setEmployeeTaskDedication(0, 1, 0.6);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/(0.6/1.1) + (7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5, valueInfo.duration);
		assertEquals(7/(0.6/1.1) * 10 + ((7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * 10 * 0.5, valueInfo.cost);
		assertEquals((7/(0.6/1.1) + (7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * eparamsu.wDuration() + (7/(0.6/1.1) * 10 + ((7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * 10 * 0.5) * eparamsu.wCost(), fitness.getValue());
		
		// Two employee, two tasks, parallel, underwork
		inst = new PSPInstance("instance_sample4.txt");
		eval = new PSPEvaluator(inst, eparamsu);
		
		phen = new PSPPhenotype(2,2);
		phen.setEmployeeTaskDedication(0, 0, 0.1);
		phen.setEmployeeTaskDedication(0, 1, 0.2);
		phen.setEmployeeTaskDedication(1, 0, 0.3);
		phen.setEmployeeTaskDedication(1, 1, 0.4);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/0.6 + (7 - (7/0.6 * 0.4)) / 0.4, valueInfo.duration);
		assertEquals(7/0.6 * (100000000000d * 0.3 + 1 * 0.7) + (7 - (7/0.6 * 0.4)) / 0.4 * (100000000000d * 0.1 + 1 * 0.3), valueInfo.cost);
		assertEquals((7/0.6 + (7 - (7/0.6 * 0.4)) / 0.4) * eparamsu.wDuration() + (7/0.6 * (100000000000d * 0.3 + 1 * 0.7) + (7 - (7/0.6 * 0.4)) / 0.4 * (100000000000d * 0.1 + 1 * 0.3)) * eparamsu.wCost(), fitness.getValue());
		
		
		// One employee, two tasks, sequential, no employee has all necessary, but has some
		inst = new PSPInstance("instance_sample8.txt");
		eval = new PSPEvaluator(inst, eparamsu);
	
		dec = new PSPDecoder(inst,eparamsu);
		
		phen = dec.decode(gen);
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(eval.wReqsk() * 3, fitness.getValue());
	}
	
	public void testEvaluateEmployeesAllSkillsMode() throws FileNotFoundException, IOException {
		
		// One employee, two tasks, sequential, underwork
		PSPInstance inst = new PSPInstance("instance_sample2.txt");
		PSPEvaluator eval = new PSPEvaluator(inst, eparams);
		
		PSPPhenotype phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 0.2);
		phen.setEmployeeTaskDedication(0, 1, 0.3);
		
		PSPObjectives objs = eval.evaluate(phen);
		Value<?> fitness = objs.get(eval.objective[0]);
		PSPObjectives.ObjectiveComponentsInfo valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/0.2 + 8/0.3, valueInfo.duration);
		assertEquals(7 * 10d + 8 * 10d, valueInfo.cost);
		assertEquals((7/0.2 + 8/0.3) * eparams.wDuration() + (7 * 10d + 8 * 10d) * eparams.wCost(), fitness.getValue());
		
		// One employee, two tasks, sequential, overwork
		phen.setEmployeeTaskDedication(0, 0, 1.1);
		phen.setEmployeeTaskDedication(0, 1, 0.6);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7 + 8/0.6, valueInfo.duration);
		assertEquals(70d + 80d, valueInfo.cost);
		assertEquals((7 + 8/0.6) * eparams.wDuration() + (70d + 80d) * eparams.wCost(), fitness.getValue());
		
		// One employee, two tasks, parallel, underwork
		inst = new PSPInstance("instance_sample3.txt");
		eval = new PSPEvaluator(inst, eparams);
		
		phen = new PSPPhenotype(1,2);
		phen.setEmployeeTaskDedication(0, 0, 0.2);
		phen.setEmployeeTaskDedication(0, 1, 0.4);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(17.5 + 17.5, valueInfo.duration);
		assertEquals(17.5 * 10 * 0.6 + 17.5 * 10 * 0.2, valueInfo.cost);
		assertEquals((17.5 + 17.5) * eparams.wDuration() + (17.5 * 10 * 0.6 + 17.5 * 10 * 0.2) * eparams.wCost(), fitness.getValue());
		
		// One employee, two tasks, parallel, overwork
		phen.setEmployeeTaskDedication(0, 0, 0.5);
		phen.setEmployeeTaskDedication(0, 1, 0.6);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/(0.6/1.1) + (7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5, valueInfo.duration);
		assertEquals(7/(0.6/1.1) * 10 + ((7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * 10 * 0.5, valueInfo.cost);
		assertEquals((7/(0.6/1.1) + (7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * eparams.wDuration() + (7/(0.6/1.1) * 10 + ((7 - (7/(0.6/1.1)) * (0.5/1.1)) / 0.5) * 10 * 0.5) * eparams.wCost(), fitness.getValue());
		
		// Two employee, two tasks, parallel, underwork
		inst = new PSPInstance("instance_sample4.txt");
		eval = new PSPEvaluator(inst, eparams);
		
		phen = new PSPPhenotype(2,2);
		phen.setEmployeeTaskDedication(0, 0, 0.1);
		phen.setEmployeeTaskDedication(0, 1, 0.2);
		phen.setEmployeeTaskDedication(1, 0, 0.3);
		phen.setEmployeeTaskDedication(1, 1, 0.4);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(7/0.6 + (7 - (7/0.6 * 0.4)) / 0.4, valueInfo.duration);
		assertEquals(7/0.6 * (100000000000d * 0.3 + 1 * 0.7) + (7 - (7/0.6 * 0.4)) / 0.4 * (100000000000d * 0.1 + 1 * 0.3), valueInfo.cost);
		assertEquals((7/0.6 + (7 - (7/0.6 * 0.4)) / 0.4) * eparams.wDuration() + (7/0.6 * (100000000000d * 0.3 + 1 * 0.7) + (7 - (7/0.6 * 0.4)) / 0.4 * (100000000000d * 0.1 + 1 * 0.3)) * eparams.wCost(), fitness.getValue());
		
		// One employee, two tasks, sequential, no employee has necessary abilities
		inst = new PSPInstance("instance_sample6.txt");
		eval = new PSPEvaluator(inst, eparams);
		
		PSPGenotype gen = new PSPGenotype(1,2);
		gen.init(new Random());
		for (int i=0; i<gen.size(); ++i)
			gen.set(i,1);
		PSPDecoder dec = new PSPDecoder(inst,eparams);
		
		phen = dec.decode(gen);
		
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(eval.wUndt() * 2, fitness.getValue());
		
		// One employee, two tasks, sequential, no employee has all necessary, but has some
		inst = new PSPInstance("instance_sample8.txt");
		eval = new PSPEvaluator(inst, eparams);
	
		dec = new PSPDecoder(inst,eparams);
		
		phen = dec.decode(gen);
		objs = eval.evaluate(phen);
		fitness = objs.get(eval.objective[0]);
		valueInfo = objs.getInfo(eval.objective[0]);
		
		assertEquals(eval.wUndt() * 2, fitness.getValue());
		
	}
	
	public void testSkillsStillNecessaryToCompleteTasks() {

		PSPPhenotype phen7 = new PSPPhenotype(inst7.getEmployeeNumber(),inst7.getTaskNumber());
		for (int e=0; e<inst7.getEmployeeNumber(); ++e)
			for (int t=0; t<inst7.getTaskNumber(); ++t)
				phen7.setEmployeeTaskDedication(e, t, 1);
		
		for (int t=0; t<inst7.getTaskNumber(); ++t) {
			int reqsk = eval7.skillsStillNecessaryToCompleteTask(t, phen7);
			assertEquals(0, reqsk);
		}
		
		for (int e=0; e<inst7.getEmployeeNumber(); ++e)
			for (int t=0; t<inst7.getTaskNumber(); ++t)
				phen7.setEmployeeTaskDedication(e, t, 0d);
		
		for (int t=0; t<inst7.getTaskNumber(); ++t) {
			int reqsk = eval7.skillsStillNecessaryToCompleteTask(t, phen7);
			assertEquals(inst7.getTaskSkillsNumber(t), reqsk);
		}
		
		
		phen7.setEmployeeTaskDedication(4, 0, 1);
		int reqsk = eval7.skillsStillNecessaryToCompleteTask(0, phen7);
		assertEquals(inst7.getTaskSkillsNumber(0), reqsk);
		
		phen7.setEmployeeTaskDedication(2, 0, 1);
		reqsk = eval7.skillsStillNecessaryToCompleteTask(0, phen7);
		assertEquals(1, reqsk);
		
		phen7.setEmployeeTaskDedication(3, 0, 1);
		reqsk = eval7.skillsStillNecessaryToCompleteTask(0, phen7);
		assertEquals(0, reqsk);
	
	}
}
