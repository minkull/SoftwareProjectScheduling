

package PSP;

import java.lang.reflect.Constructor;
import java.util.Random;

import org.opt4j.core.Genotype;
import org.opt4j.genotype.IntegerGenotype;

/**
 * An {@link IntegerGenotype} for PSP.
 * The genotype uses integers to represent the dedications of a employee to a task.
 * The integers are in 0,1,...,K.
 * Besides, the matrix of employee x task is internally represented by an ArrayList (vector).
 * The size of the ArrayList should be initialized to the number of employees * number of tasks.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPGenotype extends IntegerGenotype {

	private static final long serialVersionUID = 6692226599048764689L;

	private int k;
	private int length;
	
	// The parameter k to set the possible dedications to a task, which are in {0/k, 1/k, ..., k/k}.
	// The genotype will represent those dedications as {0,1,...,k}.
	// The size of the genotype should be number of employees * number of tasks.
	public PSPGenotype(int k, int length) {
		super(0,k);
		this.k = k;
		this.length = length;
	}
	
	public PSPGenotype(Integer k, Integer length) throws Exception{
		super(0,k);
		this.k = k.intValue();
		this.length = length.intValue();
	}
	
	public void init(Random random) {
		init(random, length);
	}
	
	protected void init(int initValue) {
		for (int i = 0; i < length; i++) {
			if (i >= size()) {
				add(initValue);
			} else {
				set(i, initValue);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <G extends Genotype> G newInstance() {
		try {
			Constructor<? extends PSPGenotype> cstr = this.getClass().getConstructor(Integer.class, Integer.class);
			return (G) cstr.newInstance(k, length);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
