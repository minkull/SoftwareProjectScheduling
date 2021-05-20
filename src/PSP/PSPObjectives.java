package PSP;

import java.util.SortedMap;
import java.util.TreeMap;

import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;

/**
 * This class is necessary to store information about each individual component of the fitness
 * to be printed in the log file.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class PSPObjectives extends Objectives {
	public class ObjectiveComponentsInfo {
		public ObjectiveComponentsInfo(double cost, double duration, int undt, int reqsk, double overwork) {
			this.cost = cost;
			this.duration = duration;
			this.undt = undt;
			this.reqsk = reqsk;
			this.overwork = overwork;
		}
		
		public double cost;
		public double duration;
		public int undt, reqsk;
		public double overwork;
	}
	
	protected SortedMap<Objective, ObjectiveComponentsInfo> mapInfo = new TreeMap<Objective, ObjectiveComponentsInfo>();
	
	/**
	 * Adds the components of the objective in order to store information for the log.
	 * 
	 * @param objective
	 *            the objective
	 * @param cost, duration, undt
	 *            the cost, duration and number of tasks with no employee associated
	 */
	public void addInfo(Objective objective, double cost, double duration, int undt, int reqsk, double overwork) {
		mapInfo.put(objective, new ObjectiveComponentsInfo(cost, duration, undt, reqsk, overwork));
	}
	
	/**
	 * Get info for the log.
	 * @param objective
	 * @return
	 */
	public ObjectiveComponentsInfo getInfo(Objective objective) {
		return mapInfo.get(objective);
	}
	
}
