

package PSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.opt4j.start.Constant;

import com.google.inject.Inject;

/**
 * This is an instance of the PSP problem.
 * It reads the instance from a file produced by the Alba and Chicano's problem instance
 * generator available at http://tracer.lcc.uma.es/problems/psp.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 * 
 */

public class PSPInstance {

	private int taskNumber;
	private double taskCosts[];
	private Vector<Integer> taskSkills[];
	
	private int employeeNumber;
	private double employeeSalaries[];
	// Each position of vector<Boolean> contains true if the employee has that skill in false if not.
	private boolean employeeSkills[][];
	
	private int skillNumber;
	
	public TPG tpg;
	
	@Inject
	public PSPInstance(
			@Constant(value = "pspInstanceFileName", namespace = PSPInstance.class) String pspInstanceFileName) 
			throws FileNotFoundException, IOException {
		readPspInstanceFile(pspInstanceFileName);
	}
	
	@SuppressWarnings("unchecked")
	protected void readPspInstanceFile(String pspInstanceFileName) throws FileNotFoundException, IOException {
		readTaskEmployeeSkillNumber(pspInstanceFileName);
		taskCosts = new double[taskNumber];
		
		// The vector of skills for each task is allocated, but contains
		// initially no skills (empty vector). This vector will be filled in
		// after calling readCostSalarySkillTPG.
		taskSkills = new Vector[taskNumber];
		for (int i=0; i<taskNumber; ++i) 
			taskSkills[i] = new Vector<Integer>(skillNumber); 
		
		employeeSalaries = new double[employeeNumber];
		employeeSkills = new boolean[employeeNumber][skillNumber];
		// Skills initialized so that all employees have no skills.
		for (int i=0; i<employeeNumber; ++i)
			for (int j=0; j<skillNumber; j++)
				employeeSkills[i][j] = false;
		
		tpg = new TPG(taskNumber);
		
		readCostSalarySkillTPG(pspInstanceFileName);
	}
	
	
	protected void readTaskEmployeeSkillNumber(String pspInstanceFileName) throws FileNotFoundException, IOException {
	
		BufferedReader myReader = null;
		try{
			// Attempt to open the file
			myReader = new BufferedReader( new FileReader( new File( pspInstanceFileName ) ) );
		}
		catch(FileNotFoundException e){
			IOException e2 = new IOException("Problem instance file "+pspInstanceFileName+" not found.");
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
		
		try {
			
			for(String line = myReader.readLine(); 
				line != null; 
				line = myReader.readLine()) {
				
				line = line.trim();
				if (line.startsWith("#") || line.isEmpty())
					continue;
				
				//Break that line up into chunks separated by commas
				StringTokenizer myTokenizer = new StringTokenizer(line, ".=" ); 
				
				String token = myTokenizer.nextToken();
				
				if (token.equals("task")) {
					token = myTokenizer.nextToken();
					if (token.equals("number"))
						taskNumber = Integer.parseInt(myTokenizer.nextToken());
				}
				
				if (token.equals("employee")) {
					token = myTokenizer.nextToken();
					if (token.equals("number"))
						employeeNumber = Integer.parseInt(myTokenizer.nextToken());
				}
				
				if (token.equals("skill")) {
					token = myTokenizer.nextToken();
					if (token.equals("number"))
						skillNumber = Integer.parseInt(myTokenizer.nextToken());
				}
				
				
			}				
		} catch (IOException e) {
			IOException e2 = new IOException("Error reading instance file "+pspInstanceFileName);
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
		
		try {
			myReader.close();
		} catch (IOException e) {
			IOException e2 = new IOException("Error closing problem instance file " + pspInstanceFileName);
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
	}
	
	
	protected void readCostSalarySkillTPG(String pspInstanceFileName) throws IOException, FileNotFoundException{
		BufferedReader myReader = null;
		try{
			// Attempt to open the file
			myReader = new BufferedReader( new FileReader( new File( pspInstanceFileName ) ) );
		}
		catch(FileNotFoundException e){
			FileNotFoundException e2 = new FileNotFoundException("Problem instance file "+pspInstanceFileName+" not found.");
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
		
		try {
			for(String line = myReader.readLine(); 
				line != null; 
				line = myReader.readLine()) {
			
				line = line.trim();
				if (line.startsWith("#") || line.isEmpty())
					continue;
			
				//Break that line up into chunks separated by commas
				StringTokenizer myTokenizer = new StringTokenizer(line, ".= " ); 
				
				String token = myTokenizer.nextToken();
				
				if (token.equals("employee")) {
					token = myTokenizer.nextToken();
					if (!token.equals("number")) { // then it is the index of the employee
						int employeeIndex = Integer.parseInt(token);
						token = myTokenizer.nextToken(); 
						if (!token.equals("skill") && !token.equals("salary")) {
							throw new IOException("Unexpected token.");
						}
						else if (token.equals("skill")) {
							if (!myTokenizer.nextToken().equals("number")) { // order of the skill
								int skill = Integer.parseInt(myTokenizer.nextToken());
								employeeSkills[employeeIndex][skill] = true;
							}
						} 
						else if (token.equals("salary")) {
							token = myTokenizer.nextToken();
							double salary = 0;
							if (myTokenizer.hasMoreTokens())
								salary = Double.parseDouble(token + "." + myTokenizer.nextToken());
							else salary = Double.parseDouble(token);
							employeeSalaries[employeeIndex] = salary;
						}
					}
						
				} else if (token.equals("task")) {
					token = myTokenizer.nextToken();
					if (!token.equals("number")) { // then it is the index of the task
						int taskIndex = Integer.parseInt(token);
						token = myTokenizer.nextToken();
						if (!token.equals("skill") && !token.equals("cost")) {
							throw new IOException("Unexpected token.");
						}
						else if (token.equals("skill")) {
							token = myTokenizer.nextToken();
							if (!token.equals("number")) { // then it is the order of the skill
								token = myTokenizer.nextToken();
								taskSkills[taskIndex].add(Integer.parseInt(token));
							}
						}
						else if (token.equals("cost")) {
							token = myTokenizer.nextToken();
							double cost = 0;
							if (myTokenizer.hasMoreTokens())
								cost = Double.parseDouble(token + "." + myTokenizer.nextToken());
							else cost = Double.parseDouble(token);
							taskCosts[taskIndex] = cost;
							
						}
					}
					
				} else if (token.equals("graph")) {
					myTokenizer.nextToken(); // arc
					if(!myTokenizer.nextToken().equals("number")) { // order of the arc
						int t1 = Integer.parseInt(myTokenizer.nextToken()); // first task
						int t2 = Integer.parseInt(myTokenizer.nextToken()); // second task
						tpg.incInDegreeTask(t2);
						tpg.get(t1).add(t2); // task t2 depends on t1
					}
				}
				
			}
			
		} catch (IOException e) {
			IOException e2 = new IOException("Error reading instance file "+pspInstanceFileName);
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
		
		try {
			myReader.close();
		} catch (IOException e) {
			IOException e2 = new IOException("Error closing problem instance file " + pspInstanceFileName);
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
	}
	
	
	public int getTaskNumber() {
		return taskNumber;
	}
	
	public int getEmployeeNumber() {
		return employeeNumber;
	}
	
	public int getSkillNumber() {
		return skillNumber;
	}
	
	public double getTaskCost(int t) {
		return taskCosts[t];
	}
	
	public double[] taskCostsClone() {
		return taskCosts.clone();
	}
	
	public double getEmployeeSalary(int e) {
		return employeeSalaries[e];
	}
	
	public boolean employeeHasSkill(int e, int s) {
		return employeeSkills[e][s];
	}
	
	public int getTaskSkillsNumber(int t) {
		return taskSkills[t].size();
	}
	
	public int getTaskSkill(int t, int s) {
		return taskSkills[t].get(s);
	}
	
	// Check if the employee by himself has all the necessary skills for performing that task
	// (our problem formulation)
	public boolean employeeHasAllNecessarySkills(int e, int t) {
		
		for (int s=0; s<getTaskSkillsNumber(t); ++s) {
			if (!employeeHasSkill(e, getTaskSkill(t, s))) 
				return false;
		}
		return true;
	}	
}
