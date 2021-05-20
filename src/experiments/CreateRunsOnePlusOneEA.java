package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import PSP.PSPInstance;


// java -cp pspea.jar experiments.CreateRuns 100 *.conf.txt *.conf

/**
 * Prepare config files and scripts to run a (1 + 1) GA with the given problem instances.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class CreateRunsOnePlusOneEA {

	/**
	 * @param <number of runs> <problem instance file names>+
	 */
	public static void main(String[] args) {
		int runs = Integer.valueOf(args[0]);
		int initGenotypeValue = -1; // random initialisation
		boolean normalisation = true;
		
	
		int [] primes = Primes.readPrimes();
		
		for (int f=1; f<args.length; ++f) {
			String instanceFileName = args[f]; // e.g. experiments/inst-exp-web/inst10-5-10-5.conf.txt
			PSPInstance instance = null;
			try {
				instance = new PSPInstance(instanceFileName);
			} catch (FileNotFoundException e1) {
				System.out.println("Error creating instance -- file not found (" + instanceFileName + ")");
				e1.printStackTrace();
				System.exit(-1);
			} catch (IOException e1) {
				System.out.println("Error creating instance -- could not open input file " + instanceFileName);
				e1.printStackTrace();
			}
			
			File runFile = new File("run1p1EA_" + instanceFileName + ".bat");
			File dir = runFile.getParentFile();
			if (dir != null) {
				dir.mkdirs();
			}
			PrintWriter runFileWriter = null;
			try {
				runFile.createNewFile();
				runFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(runFile)));
			} catch (IOException e2) {
				System.out.println("Error creating instance -- could not open output file " + runFile.getName());
				e2.printStackTrace();
			}
			
			
			for (int r = 0; r < runs; ++r) {

				try {
					File configFile = new File("config1p1EA/" + instanceFileName + "/" + instanceFileName + "_config1p1EA" + r + ".xml");
					dir = configFile.getParentFile();
					if (dir != null) {
						dir.mkdirs();
					}
					configFile.createNewFile();

					PrintWriter configFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(configFile)));

					configFileWriter.println("<configuration>");
					configFileWriter.println("  <module class=\"PSP.PSPLoggerModule\">");
					configFileWriter.println("    <property name=\"filename\">log1p1EA/" + instanceFileName + "/" + instanceFileName + "_log1p1EA" + r + ".txt</property>");
					configFileWriter.println("    <property name=\"loggingPerEvaluation\">true</property>");
					configFileWriter.println("    <property name=\"evaluationStep\">5064</property>");
					configFileWriter.println("    <property name=\"loggingPerIteration\">true</property>");
					configFileWriter.println("    <property name=\"iterationStep\">100</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("  <module class=\"PSP.PSPModule\">");
					configFileWriter.println("    <property name=\"pspInstanceFileName\">" + instanceFileName + "</property>");
					configFileWriter.println("    <property name=\"k\">7</property>");
					configFileWriter.println("    <property name=\"initGenotypeValue\">" + initGenotypeValue + "</property>");
					configFileWriter.println("    <property name=\"wCost\">1.0E-6</property>");
					configFileWriter.println("    <property name=\"wDuration\">0.1</property>");
					configFileWriter.println("    <property name=\"skillConstraintMode\">UNION_EMPLOYEES_ALL_SKILLS</property>");
					configFileWriter.println("    <property name=\"normalisation\">" + normalisation + "</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("  <module class=\"org.opt4j.common.archive.ArchiveModule\">");
					configFileWriter.println("    <property name=\"type\">POPULATION</property>");
					configFileWriter.println("    <property name=\"capacity\">100</property>");
					configFileWriter.println("    <property name=\"divisions\">7</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("  <module class=\"org.opt4j.common.random.RandomModule\">");
					configFileWriter.println("    <property name=\"type\">MERSENNE_TWISTER</property>");
					configFileWriter.println("    <property name=\"usingSeed\">true</property>");
					configFileWriter.println("    <property name=\"seed\">" + primes[r] + "</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("  <module class=\"org.opt4j.operator.mutate.BasicMutateModule\">");
					configFileWriter.println("    <property name=\"doubleType\">POLYNOMIAL</property>");
					configFileWriter.println("    <property name=\"eta\">20.0</property>");
					configFileWriter.println("    <property name=\"sigma\">0.1</property>");
					configFileWriter.println("    <property name=\"mutationRateType\">CONSTANT</property>");
					configFileWriter.println("    <property name=\"mutationRate\">" + 1d / (instance.getEmployeeNumber() * instance.getTaskNumber()) + "</property>");
					configFileWriter.println("    <property name=\"permutationType\">MIXED</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("  <module class=\"org.opt4j.optimizer.ea.ElitismSelectorModule\"/>");
					configFileWriter.println("  <module class=\"org.opt4j.optimizer.ea.EvolutionaryAlgorithmModule\">");
					configFileWriter.println("    <property name=\"generations\">5064</property>"); // the number of generations in this case is exactly the same as the number of evaluations
					// we have one new individual in each generation. The first generation only produces a new individual for the 1+1 EA, without children (check EvolutionaryAlgorithm.java). That's why the
					// number of iterations is exactly the same as the number of evaluations for the 1+1 EA.
					configFileWriter.println("    <property name=\"alpha\">1</property>");
					configFileWriter.println("    <property name=\"mu\">1</property>");
					configFileWriter.println("    <property name=\"lambda\">1</property>");
					configFileWriter.println("    <property name=\"crossoverRate\">0.0</property>");
					configFileWriter.println("  </module>");
					configFileWriter.println("</configuration>");

					configFileWriter.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}

				//System.out.println("java -cp pspea.jar:/Users/Leandro/Documents/SEBASE/Approaches/opt4j-2.4/opt4j-2.4.jar:lib/junit.jar org.opt4j.start.Opt4JStarter experiments/test_" + r + ".xml"); // <--- before it needed to add also guice and guice-assistant
				runFileWriter.println("java -cp pspea.jar:opt4j-2.4.jar:junit.jar org.opt4j.start.Opt4JStarter config1p1EA/" + instanceFileName + "/" + instanceFileName + "_config1p1EA" + r + ".xml"); // <--- before it needed to add also guice and guice-assistant


			}
			
			runFileWriter.close();
		}
	}

}
