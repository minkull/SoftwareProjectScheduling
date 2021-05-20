package PSP;

import java.util.Vector;

/**
 * A task precedence graph TPG for a PSP instance.
 * 
 * Obs.: The outer vector contains one position to each task.
 * Each position contains a vector of tasks that depend on this task (outgoing edges).
 * Be careful! The index of the outer vector represents the task.
 * However, the index of the inner vector does not represent anything!
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class TPG extends Vector<Vector<Integer>>{

	private static final long serialVersionUID = 2479555963702530192L;
	
	private int[] inDegree; // in degree for each task (= number of tasks on which each task depends in order to start)
	
	public TPG(int taskNumber){
		super(taskNumber); // Create vector with capacity taskNumber.
		
		// Create vector with capacity and size taskNumber.
		// Each position contains initially zero.
		inDegree = new int[taskNumber];
		for (int i=0; i<taskNumber; ++i) {
			inDegree[i] = 0;
		}
		
		// Allocate vectors of tasks that depend on this task (outgoing edges).
		// Each of these vectors is initially empty.
		for (int i=0; i<taskNumber; ++i) {
			this.add(new Vector<Integer>());
		}
	}

	public void incInDegreeTask(int t) {
		inDegree[t]++;
	}
	
	public int getInDegreeTask(int t) {
		return inDegree[t];
	}
	
	public int[] inDegreeClone() {
		return inDegree.clone();
	}
	
	public void addDependentTask(int t, int d) {
		this.get(t).add(d);
	}
	
	public int getNumberDependentTasks(int t) {
		return this.get(t).size();
	}
	
	public int getDependentTaskAtPosition(int t, int d) {
		return this.get(t).get(d);
	}
}
