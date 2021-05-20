package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

// java experiments/CreateCostDurGraph a ../experiments/expsnorm/inst10-5-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst10-10-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst10-15-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-5-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-10-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-15-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-5-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-10-5.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-15-5.conf.txt_log_resumo.csv > ../experiments/expsnorm/graphBench5-1.txt
// java experiments/CreateCostDurGraph a ../experiments/expsnorm/inst10-5-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst10-10-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst10-15-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-5-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-10-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst20-15-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-5-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-10-10.conf.txt_log_resumo.csv ../experiments/expsnorm/inst30-15-10.conf.txt_log_resumo.csv > ../experiments/expsnorm/graphBench5-2.txt


public class CreateCostDurGraph {
	public static void main(String[] args) {
		char id = args[0].charAt(0); // id to be used for graph formatting
		int runs[] = new int[args.length-1];
		
		for (int f=1; f<args.length; ++f, ++id) {
			runs[f-1] = 0;
			String inputFile = args[f];
			
			File file = new File(inputFile);
			Scanner in;
			String line = "";

			try {
				in = new Scanner(file);
				while (in.hasNext()) {
					runs[f-1]++;
					line = in.nextLine();
					
					//Break that line up into chunks separated by tabs
					StringTokenizer myTokenizer = new StringTokenizer(line, "\t" ); 
					
					String token = "";
					for (int i=0; i<5; ++i)
						token = myTokenizer.nextToken(); // Cost
					System.out.print("(" + token + ",");
					token = myTokenizer.nextToken(); // Duration
					System.out.println(token + ") [" + id + "]");
					
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("File not found: " + inputFile);
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Total number of runs:");
		for (int i=0; i<runs.length; ++i)
			System.out.println(runs[i]);
	}
}
