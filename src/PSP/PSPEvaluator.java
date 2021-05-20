package PSP;

/**
 * This implements our original fitness function that contains a single objective.
 * The objective is a weighted sum of the cost and duration of the project, to be minimized.
 * If the solution has some task with no employee associated (unfeasible), the fitness is 
 * WUndt * undt
 * where WUndt is (wCost * pessimisticCost + wDur * pessimisticDur) * 2.
 * 
 * 17/10/11 Modifications to choose whether or not to use normalisation.
 * 
 * @author Leandro L. Minku (L.L.Minku@cs.bham.ac.uk)
 * 
 */

import java.util.LinkedList;
import java.util.Vector;

import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.problem.Evaluator;

import com.google.inject.Inject;

public class PSPEvaluator implements Evaluator<PSPPhenotype> {
	
	// Contains a list of tasks with a certain duration dur
	protected class TaskDur {
		public TaskDur(LinkedList<Integer> tasks, double dur) {
			this.tasks = tasks;
			this.dur = dur;
		}
		LinkedList<Integer> tasks;
		double dur;
	}

	PSPInstance inst;
	PSPEvolutionaryParams eparams;
	
	protected Objective []objective;
	// We will have only 1 objective, which is 1 / the weighted sum of cost and duration of the project.
	// If the solution is not feasible, i.e., there are tasks with no employee associated,
	// then the weighted sum is summed to a weighted penalty
	public static final int COSTDUR = 0; 
	
	@Inject
	public PSPEvaluator(PSPInstance inst, PSPEvolutionaryParams eparams) {
		this.inst = inst;
		this.eparams = eparams;

		objective = new Objective[1];
		objective[COSTDUR] = new Objective("COSTDUR", Sign.MIN);
	}
	
	@Override
	public PSPObjectives evaluate(PSPPhenotype phen) {
		
		// Current in degree for each task. This is the number of tasks that still need to be 
		// completed before this task can start.
		int [] curInDegree = inst.tpg.inDegreeClone();
		
		// Current effort necessary to complete each task.
		double[] curEffortRequiredToFinishTask = inst.taskCostsClone();
		
		// Vector containing all unfinished tasks.
		// Be careful! The content of the vector is the index for the task.
		// The index of the vector is not the same as the index of the task.
		LinkedList<Integer> unfinishedTasks = new LinkedList<Integer>();
		for (int t=0; t<inst.getTaskNumber(); ++t)
			unfinishedTasks.add(t); // No task has finished yet.
		
		double cost = 0d;
		double duration = 0d;
		int undt = 0, reqsk = 0;
		double fitness = 0d;
		double totalOverwork = 0d;
		
		// This are updated at every iteration of the loop.
		// They are initialized here not to need to be allocated at every iteration, saving time.
		double totalDedicationToTask[] = new double[inst.getTaskNumber()];
		double unnormSumDedicationEmployee[] = new double[inst.getEmployeeNumber()];
		
		// While there are unfinished tasks.
		while (unfinishedTasks.size() > 0) {
			
			// Tasks that can start now.
			Vector<Integer> curIndependentTasks = determineCurIndependentTasks(curInDegree);
			
			if (curIndependentTasks.size() == 0) {
				System.err.println("Problem instance not solvable!");
				cost = duration = fitness = -1d;
				undt = -1;
				break;
			}
			
			// Determine the sum of the dedication of each employee across all tasks being done at the same time
			for (int e=0; e<inst.getEmployeeNumber(); ++e) 
				unnormSumDedicationEmployee[e] = determineUnnormSumCurrDedicationEmployee(e, curIndependentTasks, phen);
							
			// Determine the total dedication currently done by all employees to each task
			for (int i=0; i<curIndependentTasks.size();) {
				int t = curIndependentTasks.get(i);
				
				totalDedicationToTask[t] = 0d;
				for (int e=0; e<inst.getEmployeeNumber(); ++e) {
					// We take the max here so that
					// the normalization won't be able to increase the amount of work done by an employee.
					// That was done so that the evolutionary algorithm
					// won't increase the amount of work of employees that may be too expensive.	
					if (eparams.getNormalisation()) // 17/10/11 <---le normalisation option
						totalDedicationToTask[t] += phen.getEmployeeTaskDedication(e, t)/Math.max(1d,unnormSumDedicationEmployee[e]);
					else // 17/10/11 <---le normalisation 
						totalDedicationToTask[t] += phen.getEmployeeTaskDedication(e, t); // 17/10/11 <---le normalisation 
				}
				
				// If a task has no dedication from any employee, the project cannot finish.
				// If we don't need to calculate undt or consider a certain cost to a task, 
				// we could even break out form the loop here.
				// Actually, we could leave that outside the main loop, saving time for not calculating the
				// cost and duration of a project without considering the unfeasible tasks. However, I left it
				// here should we decide to use this cost and duration for the optimization.
				if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS &&
					totalDedicationToTask[t] == 0d) {
					undt++; 
					removeTask(t, unfinishedTasks, curInDegree);
					curIndependentTasks.remove(Integer.valueOf(t)); // tasks with no employee associated are not used to calculate the duration/cost of the project <---Le29/09/11
				}
				else if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS) {
					int reqskt = skillsStillNecessaryToCompleteTask(t, phen);
					if (reqskt > 0) {
						reqsk += reqskt;
						removeTask(t, unfinishedTasks, curInDegree);
						curIndependentTasks.remove(Integer.valueOf(t)); // tasks whose assigned employees haven't got enough skills are not used to calculate the duration/cost of the project 
						// however, they will influence the dedication that other employees have to other tasks,
						// and the algorithm will consider that the employees are dedicating themselves to the task that
						// cannot be completed.
					}
					else i++;
				}
				else i++;
			}
			                      
			// If all the tasks that were currently independent were actually uncompletable (no employee associated)
			// go to the next iteration of the loop <---Le29/09/11
			if (curIndependentTasks.size() == 0)
				continue;
			
			TaskDur td = durToFinishFirstTask(curIndependentTasks, totalDedicationToTask, curEffortRequiredToFinishTask);
			double durToFinishFirstTask = td.dur;
			LinkedList<Integer> firstTasksToFinish = td.tasks;
						
			cost = updateCost(cost, durToFinishFirstTask, unnormSumDedicationEmployee);
			duration += durToFinishFirstTask;
			totalOverwork = updateOverwork(totalOverwork, durToFinishFirstTask, unnormSumDedicationEmployee); // 17/10/11 <---le normalisation option
			
			
			for (int i=0; i<curIndependentTasks.size(); ++i) {
				int t = curIndependentTasks.get(i);
				
				if (!firstTasksToFinish.contains(t)) {
					curEffortRequiredToFinishTask[t] -= (durToFinishFirstTask * totalDedicationToTask[t]);
				} else { // We need to do this directly for the first task to finish to avoid problems with precision, that would leave to never getting current effort necessary of zero or to get it negative 
					curEffortRequiredToFinishTask[t] = 0d;
					//if (totalDedicationToTask[t] != 0d) // We need this check here because if the task has no dedication at all for it, it will have already been removed in the loop above <--- we don't need this anymore because we are removing the task from curIndependentTasks in the loop above now <---Le29/09/11
					removeTask(t, unfinishedTasks, curInDegree);
				}
			}			
		}	
		
		if (fitness != -1d)
			// The current cost and duration passed here for unfeasible solutions is the cost and duration
			// of the project without considering the tasks that cannot be completed, i.e., considering that they are
			// automatically all completed in time zero and with no cost.
			fitness = calculateFitness(cost, duration, undt, reqsk, totalOverwork);
				
		PSPObjectives objectives = new PSPObjectives();
		objectives.add(objective[COSTDUR], fitness);
		objectives.addInfo(objective[COSTDUR], cost, duration, undt, reqsk, totalOverwork); // added overwork 17/10/11 <---le normalisation option
		return objectives;
	}
	
	public double calculateFitness(double cost, double duration, int undt, int reqsk, double overwork) { // added overwork 17/10/11 <---le normalisation option

		if (undt == 0 && reqsk == 0 && overwork == 0d) // added overwork 17/10/11 <---le normalisation option
			return eparams.wCost() * cost + eparams.wDuration() * duration;
		
		if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS && undt > 0)
			return wUndt() * undt;
		else if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.UNION_EMPLOYEES_ALL_SKILLS && reqsk > 0)
			return wReqsk() * reqsk;
		else // there is overwork
			return wUndt() + overwork; // added overwork 17/10/11 <---le normalisation option
		
		//	fitness = //eparams.wCost() * cost + eparams.wDuration() * duration + // this would be the cost and duration calculated without considering the tasks that cannot be completed.
		//			  wUndt() * undt + wReqsk * reqsk;
	}
	
	// is the (wCost * pessimistic upper bound of the cost + wDur * pessimistic upper bound of the duration) * 2.
	protected double pessimisticCostDur() {
		double pessimisticDuration = 0d;
		double pessimisticCost = 0d;
		for (int t=0; t<inst.getTaskNumber(); t++) {
			pessimisticDuration += inst.getTaskCost(t);
			for (int e=0; e<inst.getEmployeeNumber(); ++e)
				pessimisticCost += inst.getEmployeeSalary(e) * inst.getTaskCost(t);
		}
		pessimisticDuration *= eparams.getK();
		
		return 2 * (eparams.wCost() * pessimisticCost + eparams.wDuration() * pessimisticDuration);
	}
	
	// The weight given to undt in the fitness calculation of unfeasible solutions

	protected double wUndt() {
		//if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS)
			return pessimisticCostDur();
		//else return 10d; // this is set to 10 because in Alba and Chicano's experiments, wUndt and wReqsk had the same value.
	}
	
	protected double wReqsk() {
		//if (eparams.getSkillConstraintMode() == PSPEvolutionaryParams.SkillConstraintMode.EMPLOYEES_ALL_SKILLS)
		return pessimisticCostDur();
		//else return 10d; // this is set to 10 because in Alba and Chicano's experiments, wUndt and wReqsk had the same value.
	}
	
	public void removeTask(int t, LinkedList<Integer> unfinishedTasks, int [] curInDegree) {
		unfinishedTasks.remove(Integer.valueOf(t));
		
		// Update the inDegree of all tasks that depend on this one
		for(int j=0; j<inst.tpg.getNumberDependentTasks(t); ++j) {
			int d = inst.tpg.getDependentTaskAtPosition(t, j);
			curInDegree[d]--;
		}
		curInDegree[t] = -1; // To avoid t getting inserted again in the currently independent tasks.
	}
	
	// 17/10/11 <---le normalisation option
	public double updateOverwork(double totalOverwork, double durToFinishFirstTask, double [] unnormSumDedicationEmployee) {
		
		if (eparams.getNormalisation())
			return 0d;
		
		double additionalOverwork = 0d;
		for (int e=0; e<inst.getEmployeeNumber(); ++e) {
			double stepOverwork = unnormSumDedicationEmployee[e] - PSPEvolutionaryParams.EMPLOYEE_MAX_DEDICATION;
			if (stepOverwork > 0)
				additionalOverwork += stepOverwork;
		}
		return totalOverwork + durToFinishFirstTask * additionalOverwork;
	}
	
	public double updateCost(double cost, double durToFinishFirstTask, double [] unnormSumDedicationEmployee) {
		double stepCost = 0d;
		
		for (int e=0; e<inst.getEmployeeNumber(); ++e) {
			// If the unnormalized sum of dedications of an employee is > 1, that means that the normalization would make it 1
			// Else, the normalization would leave it as it is.
			// That's the reason for taking the min(1,unnormSum).
			if (eparams.getNormalisation()) // 17/10/11 <---le normalisation option
				stepCost += (inst.getEmployeeSalary(e) * Math.min(1d, unnormSumDedicationEmployee[e]));
			else // 17/10/11 <---le normalisation option
				stepCost += (inst.getEmployeeSalary(e) * unnormSumDedicationEmployee[e]); // 17/10/11 <---le normalisation option
		}
		return cost + durToFinishFirstTask * stepCost;
	}
	
	// Determines what tasks will finish first, according to the total dedication of employees to this task
	// and to the effort still required to complete this task.
	// Before, I was considering that there is only one task with a certain minimum duration that will finish
	// first. However, there might be more than one task with a minimum duration that will finish at the same 
	// time as the first tasks to finish. So, this method actually returns all the tasks with minimum duration dur
	// that will finish first.
	protected TaskDur durToFinishFirstTask(Vector<Integer> curIndependentTasks, double [] totalDedicationToTask, double [] curEffortRequiredToFinishTask) {
		int minTask = curIndependentTasks.get(0); 
		LinkedList<Integer> minTasks = new LinkedList<Integer>();
		minTasks.add(minTask);
		double minEff;
		
		if (totalDedicationToTask[minTask] > 0) 
			minEff = curEffortRequiredToFinishTask[minTask]/totalDedicationToTask[minTask];
		else minEff = Double.MAX_VALUE;
		
		for (int i=1; i<curIndependentTasks.size(); ++i) {
			int t = curIndependentTasks.get(i);
			double eff = 0d;
			if (totalDedicationToTask[t] > 0)
				eff = curEffortRequiredToFinishTask[t]/totalDedicationToTask[t];
			else eff = Double.MAX_VALUE;
			
			if (eff < minEff) {
				minEff = eff;
				minTasks.clear();
				minTasks.add(t);
			} else if (eff == minEff) {
				minTasks.add(t);
			}
		}
		
		return new TaskDur(minTasks,minEff);
	}
	
	// This is to be used for the normalization.
	protected double determineUnnormSumCurrDedicationEmployee(int e, Vector<Integer> curIndependentTasks, PSPPhenotype phen) {
		double sum = 0d;
		for (int i=0; i<curIndependentTasks.size(); ++i) {
			int t = curIndependentTasks.get(i);
			sum += phen.getEmployeeTaskDedication(e, t);
		}
		return sum;
	}
	
	// Tasks that can start now.
	// Input: curInDegree -- the current in degree for each task
	protected Vector<Integer> determineCurIndependentTasks(int [] curInDegree) {
		Vector<Integer> curIndependentTasks = new Vector<Integer>(inst.getTaskNumber());
		for (int t=0; t<curInDegree.length; ++t)
			if (curInDegree[t] == 0)
				curIndependentTasks.add(t);
		return curIndependentTasks;
	}
	

	// Checks whether the union of employees assigned to a task contains all the skills necessary to 
	// complete a task.
	// Returns the number of skills not
	// contained by any employee, but that are necessary for the task.
	public int skillsStillNecessaryToCompleteTask(int t, PSPPhenotype phen) {
		
		int reqsk = 0;
			
		for (int s=0; s<inst.getTaskSkillsNumber(t); ++s) {
			
			boolean anEmployeeHasSkill = false; 
			for (int e=0; e<inst.getEmployeeNumber() && !anEmployeeHasSkill; ++e) {
				if (phen.getEmployeeTaskDedication(e, t) != 0 && inst.employeeHasSkill(e, inst.getTaskSkill(t, s))) 
					anEmployeeHasSkill = true;
			}
			if (!anEmployeeHasSkill)
				reqsk++;
		}
		return reqsk;
	}
}
