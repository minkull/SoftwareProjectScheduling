package org.opt4j.operator.mutate;

import org.opt4j.common.random.Rand;
import org.opt4j.genotype.IntegerGenotype;

import static org.mockito.Mockito.*;
import junit.framework.TestCase;

public class MutateIntegerRLSTester extends TestCase {

	IntegerGenotype genotype;
	int k, length, genVal;
	MutateIntegerRLS mut;
	Rand random;
	
	public MutateIntegerRLSTester() {
		super();
	}

	public MutateIntegerRLSTester(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		k = 3; length = 5;
		genotype = new IntegerGenotype(0,k);
		
		genVal = 1;
		for (int i = 0; i < length-1; ++i)
			genotype.add(genVal);
		genotype.add(k);
		
		random = mock(Rand.class); //context.mock(Rand.class);
		
		when(random.nextInt(anyInt())).thenReturn(	0,0,       // pos 0, new value 0
								  			0,genVal,  // pos 0, new value genVal+1 (genVal would be the same as the current value)
								  			0,k-1,     // pos 0, new value k
								  			1,0,       // pos 1, new value 0
								  			1,k-1,     // pos 1, new value k
								  			2,0,
								  			3,genVal,
								  			4,0,       // pos 4, new value 0
								  			4,1,       // pos 4, new value 1 (current value is k)
								  			4,k-1);    // pos 4, new value k-1 (current value is k)
		
		mut = new MutateIntegerRLS(random);
		
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testMutate() {
		
		mut.mutate(genotype, 0);
		assertEquals(0, genotype.get(0).intValue());
		genotype.set(0, genVal);
		mut.mutate(genotype, 0);
		assertEquals(genVal+1, genotype.get(0).intValue());
		genotype.set(0, genVal);
		mut.mutate(genotype, 0);
		assertEquals(k, genotype.get(0).intValue());
		
		mut.mutate(genotype, 0);
		assertEquals(0, genotype.get(1).intValue());
		genotype.set(1, genVal);
		mut.mutate(genotype, 0);
		assertEquals(k, genotype.get(1).intValue());
		
		mut.mutate(genotype, 0);
		assertEquals(0, genotype.get(2).intValue());
		
		mut.mutate(genotype, 0);
		assertEquals(genVal+1, genotype.get(3).intValue());
		
		mut.mutate(genotype, 0);
		assertEquals(0, genotype.get(4).intValue());
		genotype.set(4, k);
		mut.mutate(genotype, 0);
		assertEquals(1, genotype.get(4).intValue());
		genotype.set(4, k);
		mut.mutate(genotype, 0);
		assertEquals(k-1, genotype.get(4).intValue());
		
		/*
		when(random.nextInt(k)).thenReturn(	0,0,       // pos 0, new value 0
	  			0,genVal,  // pos 0, new value genVal+1 (genVal would be the same as the current value)
	  			0,k-1,     // pos 0, new value k
	  			1,0,       // pos 1, new value 0
	  			1,k-1,     // pos 1, new value k
	  			2,0,
	  			3,genVal,
	  			4,0,       // pos 4, new value 0
	  			4,1,       // pos 4, new value 1 (current value is k)
	  			4,k-1);    // pos 4, new value 2 (current value is k)
		
		
		
		// To check if each of the possible mutated values has ever been generated
		boolean [] values = new boolean[k+1];
		for (int i=0; i<k+1; ++i)
			values[i] = false;
		
		for (int t=0; t<10000; ++t) {
			mut.mutate(genotype, 0);
			
			boolean diff = false; // To check if at least one value has been mutated each time the mutation is applied
			
			for (int i=0; i<genotype.size(); ++i) {
				assertTrue(genotype.get(i) >= 0);
				assertTrue(genotype.get(i) <= k);
				
				if (genotype.get(i) != 1) {
					diff = true;
					
					for (int j=0; j<k+1; ++j)
						if (genotype.get(i) == j)
							values[j] = true;
				}
				
			}
			assertTrue(diff);
			diff = false;
			
			for (int i = 0; i < 10; ++i) {
				//System.out.print(genotype.get(i));
				genotype.set(i, 1);
			}
			//System.out.println();
		}
		
		for (int i=0; i<k+1; ++i)
			if (i != 1)
				assertTrue(values[i]);*/
	}

}
