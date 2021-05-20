package PSP;

import junit.framework.TestCase;

/**
 * A tester for {@link TPG}.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class TPGTest extends TestCase {
	
	private int numTasks = 5;
	private TPG tpg;
	
	public TPGTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		tpg = new TPG(numTasks);
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testTPG() {
		for (int t=0; t<numTasks; ++t) {
			for (int i=0; i<=t; ++i)
				tpg.incInDegreeTask(t);
		}
		
		tpg.addDependentTask(0, 1);
		tpg.addDependentTask(0, 2);
		tpg.addDependentTask(1, 3);
		tpg.addDependentTask(2, 4);
		
		for (int t=0; t<numTasks; ++t) {
			assertEquals(t+1, tpg.getInDegreeTask(t));
		}
		
		assertEquals(2, tpg.getNumberDependentTasks(0));
		assertEquals(1, tpg.getDependentTaskAtPosition(0,0));
		assertEquals(2, tpg.getDependentTaskAtPosition(0,1));
		assertEquals(3, tpg.getDependentTaskAtPosition(1,0));
		assertEquals(4, tpg.getDependentTaskAtPosition(2,0));
	}
	
	
}
