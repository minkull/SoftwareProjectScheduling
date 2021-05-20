package PSP;

import org.opt4j.common.random.Rand;
import org.opt4j.optimizer.ea.Pair;

//import org.jmock.Mockery;
//import org.jmock.Expectations;
//import org.jmock.integration.junit3.MockObjectTestCase;
//import org.jmock.lib.legacy.ClassImposteriser;

import static org.mockito.Mockito.*;

import junit.framework.TestCase;

/**
 * A tester class for PSPCrossoverEmployee.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

public class PSPCrossoverTest extends TestCase {

	private PSPInstance inst;
	private PSPGenotype gen1, gen2;
	private PSPDecoder dec;
	private PSPEvolutionaryParams eparams;
	private PSPCrossover cross;
	private Rand rand;
			
	public PSPCrossoverTest() {
		super();
	}
	
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
		inst = mock(PSPInstance.class);
		when(inst.getEmployeeNumber()).thenReturn(4);
		when(inst.getTaskNumber()).thenReturn(3);
		
		// Define a mock class for rand
		// It will return the sequence 0..5 when receiving nextInt calls.
		rand = mock(Rand.class);
		
		//inst = new PSPInstance("instance_sample.txt");
		eparams = new PSPEvolutionaryParams(5,1e-6,0.1, PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS,-1,true);
		dec = new PSPDecoder(inst,eparams);
		
		gen1 = new PSPGenotype(eparams.getK(), inst.getEmployeeNumber() * inst.getTaskNumber());
		gen2 = new PSPGenotype(eparams.getK(), inst.getEmployeeNumber() * inst.getTaskNumber());
		for (int e=0,i=0; e<inst.getEmployeeNumber(); ++e) 
			for (int t=0; t<inst.getTaskNumber(); t++, i++) {
				gen1.add(i);
				gen2.add(-i);
			}
		
		
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }

	public void testPSPCrossoverEmployee() {
		
		
		when(rand.nextDouble()).thenReturn(0.1,0.6,0.1,0.6);
		
		cross = new PSPCrossoverEmployee(rand, dec);//, 0.7d);
		
		PSPGenotype o1 = gen1.newInstance();
		PSPGenotype o2 = gen2.newInstance();
				
		cross.crossover(gen1,gen2,o1,o2);
		
		assertEquals(0,o1.size());
		assertEquals(0,o2.size());
		
		o1.init(0);
		o2.init(0);
		
		cross.crossover(gen1,gen2,o1,o2);
		
		assertEquals(o1.size(), inst.getEmployeeNumber() * inst.getTaskNumber());
		assertEquals(o2.size(), inst.getEmployeeNumber() * inst.getTaskNumber());

		checkPSPCrossoverEmployee(o1,o2);
		
		/*for (int e=0; e<inst.getEmployeeNumber(); e+=2)
			for (int t=0; t<inst.getTaskNumber(); ++t)
				assertEquals(gen1.get(e*inst.getTaskNumber() + t),o1.get(e*inst.getTaskNumber() + t));

		for (int e=1; e<inst.getEmployeeNumber(); e+=2)
			for (int t=0; t<inst.getTaskNumber(); ++t)
				assertEquals(gen1.get(e*inst.getTaskNumber() + t),o2.get(e*inst.getTaskNumber() + t));

		for (int e=1; e<inst.getEmployeeNumber(); e+=2)
			for (int t=0; t<inst.getTaskNumber(); ++t)
				assertEquals(gen2.get(e*inst.getTaskNumber() + t),o1.get(e*inst.getTaskNumber() + t));

		for (int e=0; e<inst.getEmployeeNumber(); e+=2)
			for (int t=0; t<inst.getTaskNumber(); ++t)
				assertEquals(gen2.get(e*inst.getTaskNumber() + t),o2.get(e*inst.getTaskNumber() + t));*/

	}
	
	public void checkPSPCrossoverEmployee(PSPGenotype o1, PSPGenotype o2) {
		assertEquals(o1.size(), inst.getEmployeeNumber() * inst.getTaskNumber());
		assertEquals(o2.size(), inst.getEmployeeNumber() * inst.getTaskNumber());

		int pos = 0;
		for (; pos<inst.getTaskNumber(); ++pos)
			assertEquals(gen1.get(pos),o1.get(pos));
		for (; pos<inst.getTaskNumber()*2; ++pos)
			assertEquals(gen2.get(pos),o1.get(pos));
		for (; pos<inst.getTaskNumber()*3; ++pos)
			assertEquals(gen1.get(pos),o1.get(pos));
		for (; pos<inst.getTaskNumber()*4; ++pos)
			assertEquals(gen2.get(pos),o1.get(pos));
		
		
		pos = 0;
		for (; pos<inst.getTaskNumber(); ++pos)
			assertEquals(gen2.get(pos),o2.get(pos));
		for (; pos<inst.getTaskNumber()*2; ++pos)
			assertEquals(gen1.get(pos),o2.get(pos));
		for (; pos<inst.getTaskNumber()*3; ++pos)
			assertEquals(gen2.get(pos),o2.get(pos));
		for (; pos<inst.getTaskNumber()*4; ++pos)
			assertEquals(gen1.get(pos),o2.get(pos));
	}
	
	// This also tests the call for crossover without passing the offspring as parameter
	public void testPSPCrossoverTask() {
		
		when(rand.nextDouble()).thenReturn(0.1,0.6,0.1);
				
		cross = new PSPCrossoverTask(rand, dec);//, 0.7d);
		
		PSPGenotype o1 = gen1.newInstance();
		PSPGenotype o2 = gen2.newInstance();
				
		cross.crossover(gen1,gen2,o1,o2);

		assertEquals(0,o1.size());
		assertEquals(0,o2.size());
				
		Pair<PSPGenotype> o = cross.crossover(gen1,gen2);
		o1 = o.getFirst();
		o2 = o.getSecond();

		
		checkPSPCrossoverTask(o1,o2);
		
	}
	
	public void checkPSPCrossoverTask(PSPGenotype o1, PSPGenotype o2) {
		assertEquals(o1.size(), inst.getEmployeeNumber() * inst.getTaskNumber());
		assertEquals(o2.size(), inst.getEmployeeNumber() * inst.getTaskNumber());

		int pos = 0;
		for (; pos<o1.size();) {
			assertEquals(gen1.get(pos),o1.get(pos)); pos++;
			assertEquals(gen2.get(pos),o1.get(pos)); pos++;
			assertEquals(gen1.get(pos),o1.get(pos)); pos++;
		}
		pos = 0;
		for (; pos<o1.size();) {
			assertEquals(gen2.get(pos),o2.get(pos)); pos++;
			assertEquals(gen1.get(pos),o2.get(pos)); pos++;
			assertEquals(gen2.get(pos),o2.get(pos)); pos++;
		}
	}
	
	public void testPSPCrossoverMixedEmployeeTask() {
		when(rand.nextDouble()).thenReturn(0.1).thenReturn(0.1,0.6,0.1,0.6);
		
		cross = new PSPCrossoverMixedEmployeeTask(rand, dec);
		Pair<PSPGenotype> o = cross.crossover(gen1,gen2);
		checkPSPCrossoverEmployee(o.getFirst(), o.getSecond()); 
		
		when(rand.nextDouble()).thenReturn(0.6).thenReturn(0.1,0.6,0.1);
		o = cross.crossover(gen1,gen2);
		checkPSPCrossoverTask(o.getFirst(), o.getSecond());
		
		
	}

}
