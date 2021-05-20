package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The fitness printed is actually 1/fitness, to be compareable to Alba and Chicano's more easily.
 * Args: runs, number of generations in the log, base name of input files 
 * java -cp pspea.jar experiments.CreateAverageFitnessAcrossGenerationsGraph 100 51 expsnorm/log/inst10-5-5.conf.txt/inst10-5-5.conf.txt_log expsnorm/log/inst10-10-5.conf.txt/inst10-10-5.conf.txt_log expsnorm/log/inst10-15-5.conf.txt/inst10-15-5.conf.txt_log

 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */
public class CreateAverageFitnessAcrossGenerationsGraph {
	public static void main(String[] args) {
		int runs = Integer.parseInt(args[0]);
		int generations = Integer.parseInt(args[1]);

		for (int f=2; f<args.length; ++f) {
			double [][] stats = new double[generations][9];
			for (int g=0; g<stats.length; ++g)
				for (int s=0; s<stats[g].length; ++s)
					stats[g][s] = 0d;
			
			String baseFileName = args[f];
			
			for (int r=0; r<runs; ++r) {
				File file = new File(baseFileName + r + ".txt");
				Scanner in;
				String line = "";

				try {
					in = new Scanner(file);
					
					line = in.nextLine(); // header
					// Each line is a generation after the header
					for (int g=0; in.hasNext(); ++g) {
						line = in.nextLine();
						
						//Break that line up into chunks separated by tabs
						StringTokenizer myTokenizer = new StringTokenizer(line, "\t" ); 
						
						for (int s=0; s<stats[g].length; ++s) {
							String token = myTokenizer.nextToken();
							stats[g][s] += Double.parseDouble(token);
						}
					}
					in.close();
				} catch (FileNotFoundException e) {
					System.out.println("File not found: " + baseFileName + r + ".txt");
					e.printStackTrace();
				}
			}
			
			
			for (int g=0; g<stats.length; ++g) {
				System.out.println("(" + stats[g][0]/runs + "," + 1d/(stats[g][3]/runs) + ")");
			}
			System.out.println("----------------------");
			
		}
		
	}
}
