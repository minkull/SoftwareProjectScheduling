package PSP;

import junit.framework.TestCase;

/**
 * A tester for {@link PSPPhenotype}.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPPhenotypeTest extends TestCase {
	
	private PSPPhenotype phen;
	private int numEmployee;
	private int numTask;
	
	public PSPPhenotypeTest() {
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		numEmployee = 3;
		numTask = 4;
		phen = new PSPPhenotype(numEmployee, numTask);
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }

	public void testSetGet() {
		for (int e=0; e<numEmployee; ++e)
			for (int t=0; t<numTask; ++t)
				phen.setEmployeeTaskDedication(e, t, e+t);
		
		for (int e=0; e<numEmployee; ++e)
			for (int t=0; t<numTask; ++t)
				assertEquals((double)e+t, phen.getEmployeeTaskDedication(e,t));
	}
}
