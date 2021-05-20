package experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Create a summary file with the last line of each run.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class GetLastLine {

	/**
	 * @param args <number of runs> <log file names without the number of the run>+
	 * iteration       evaluations     runtime[s]      COSTDUR[MIN]    cost    duration        undt    reqsk   overwork      phenotypeBeforeNormalization
	 * 
	 * java -cp ../pspea.jar experiments.GetLastLine 100 inst-employees10.conf/inst-employees10.conf_logGA inst-employees15.conf/inst-employees15.conf_logGA inst-employees20.conf/inst-employees20.conf_logGA inst-employees5.conf/inst-employees5.conf_logGA inst-skills10.conf/inst-skills10.conf_logGA inst-skills2.conf/inst-skills2.conf_logGA inst-skills4.conf/inst-skills4.conf_logGA inst-skills6.conf/inst-skills6.conf_logGA inst-skills8.conf/inst-skills8.conf_logGA inst-tasks10.conf/inst-tasks10.conf_logGA inst-tasks20.conf/inst-tasks20.conf_logGA inst-tasks30.conf/inst-tasks30.conf_logGA inst10-10-10-5.conf.txt/inst10-10-10-5.conf.txt_logGA inst10-10-10-7.conf.txt/inst10-10-10-7.conf.txt_logGA inst10-10-10.conf.txt/inst10-10-10.conf.txt_logGA inst10-10-5.conf.txt/inst10-10-5.conf.txt_logGA inst10-15-10-5.conf.txt/inst10-15-10-5.conf.txt_logGA inst10-15-10-7.conf.txt/inst10-15-10-7.conf.txt_logGA inst10-15-10.conf.txt/inst10-15-10.conf.txt_logGA inst10-15-5.conf.txt/inst10-15-5.conf.txt_logGA inst10-5-10-5.conf.txt/inst10-5-10-5.conf.txt_logGA inst10-5-10-7.conf.txt/inst10-5-10-7.conf.txt_logGA inst10-5-10.conf.txt/inst10-5-10.conf.txt_logGA inst10-5-5.conf.txt/inst10-5-5.conf.txt_logGA inst20-10-10-5.conf.txt/inst20-10-10-5.conf.txt_logGA inst20-10-10-7.conf.txt/inst20-10-10-7.conf.txt_logGA inst20-10-10.conf.txt/inst20-10-10.conf.txt_logGA inst20-10-5.conf.txt/inst20-10-5.conf.txt_logGA inst20-15-10-5.conf.txt/inst20-15-10-5.conf.txt_logGA inst20-15-10-7.conf.txt/inst20-15-10-7.conf.txt_logGA inst20-15-10.conf.txt/inst20-15-10.conf.txt_logGA inst20-15-5.conf.txt/inst20-15-5.conf.txt_logGA inst20-5-10-5.conf.txt/inst20-5-10-5.conf.txt_logGA inst20-5-10-7.conf.txt/inst20-5-10-7.conf.txt_logGA inst20-5-10.conf.txt/inst20-5-10.conf.txt_logGA inst20-5-5.conf.txt/inst20-5-5.conf.txt_logGA inst30-10-10-5.conf.txt/inst30-10-10-5.conf.txt_logGA inst30-10-10-7.conf.txt/inst30-10-10-7.conf.txt_logGA inst30-10-10.conf.txt/inst30-10-10.conf.txt_logGA inst30-10-5.conf.txt/inst30-10-5.conf.txt_logGA inst30-15-10-5.conf.txt/inst30-15-10-5.conf.txt_logGA inst30-15-10-7.conf.txt/inst30-15-10-7.conf.txt_logGA inst30-15-10.conf.txt/inst30-15-10.conf.txt_logGA inst30-15-5.conf.txt/inst30-15-5.conf.txt_logGA inst30-5-10-5.conf.txt/inst30-5-10-5.conf.txt_logGA inst30-5-10-7.conf.txt/inst30-5-10-7.conf.txt_logGA inst30-5-10.conf.txt/inst30-5-10.conf.txt_logGA inst30-5-5.conf.txt/inst30-5-5.conf.txt_logGA
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst-employees5.conf/inst-employees5.conf_log expsnorm/log/inst-employees10.conf/inst-employees10.conf_log expsnorm/log/inst-employees15.conf/inst-employees15.conf_log expsnorm/log/inst-employees20.conf/inst-employees20.conf_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst-tasks10.conf/inst-tasks10.conf_log expsnorm/log/inst-tasks20.conf/inst-tasks20.conf_log expsnorm/log/inst-tasks30.conf/inst-tasks30.conf_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst-skills2.conf/inst-skills2.conf_log expsnorm/log/inst-skills4.conf/inst-skills4.conf_log expsnorm/log/inst-skills6.conf/inst-skills6.conf_log expsnorm/log/inst-skills8.conf/inst-skills8.conf_log expsnorm/log/inst-skills10.conf/inst-skills10.conf_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst10-10-10-5.conf.txt/inst10-10-10-5.conf.txt_log expsnorm/log/inst10-10-10-7.conf.txt/inst10-10-10-7.conf.txt_log expsnorm/log/inst10-10-10.conf.txt/inst10-10-10.conf.txt_log expsnorm/log/inst10-10-5.conf.txt/inst10-10-5.conf.txt_log expsnorm/log/inst10-15-10-5.conf.txt/inst10-15-10-5.conf.txt_log expsnorm/log/inst10-15-10-7.conf.txt/inst10-15-10-7.conf.txt_log expsnorm/log/inst10-15-10.conf.txt/inst10-15-10.conf.txt_log expsnorm/log/inst10-15-5.conf.txt/inst10-15-5.conf.txt_log expsnorm/log/inst10-5-10-5.conf.txt/inst10-5-10-5.conf.txt_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst10-5-10-7.conf.txt/inst10-5-10-7.conf.txt_log expsnorm/log/inst10-5-10.conf.txt/inst10-5-10.conf.txt_log expsnorm/log/inst10-5-5.conf.txt/inst10-5-5.conf.txt_log expsnorm/log/inst20-10-10-5.conf.txt/inst20-10-10-5.conf.txt_log expsnorm/log/inst20-10-10-7.conf.txt/inst20-10-10-7.conf.txt_log expsnorm/log/inst20-10-10.conf.txt/inst20-10-10.conf.txt_log expsnorm/log/inst20-10-5.conf.txt/inst20-10-5.conf.txt_log expsnorm/log/inst20-15-10-5.conf.txt/inst20-15-10-5.conf.txt_log expsnorm/log/inst20-15-10-7.conf.txt/inst20-15-10-7.conf.txt_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst20-15-10.conf.txt/inst20-15-10.conf.txt_log expsnorm/log/inst20-15-5.conf.txt/inst20-15-5.conf.txt_log expsnorm/log/inst20-5-10-5.conf.txt/inst20-5-10-5.conf.txt_log expsnorm/log/inst20-5-10-7.conf.txt/inst20-5-10-7.conf.txt_log expsnorm/log/inst20-5-10.conf.txt/inst20-5-10.conf.txt_log expsnorm/log/inst20-5-5.conf.txt/inst20-5-5.conf.txt_log expsnorm/log/inst30-10-10-5.conf.txt/inst30-10-10-5.conf.txt_log expsnorm/log/inst30-10-10-7.conf.txt/inst30-10-10-7.conf.txt_log expsnorm/log/inst30-5-5.conf.txt/inst30-5-5.conf.txt_log
	 * java -cp pspea.jar experiments.GetLastLine 100 expsnorm/log/inst30-10-10.conf.txt/inst30-10-10.conf.txt_log expsnorm/log/inst30-10-5.conf.txt/inst30-10-5.conf.txt_log expsnorm/log/inst30-15-10-5.conf.txt/inst30-15-10-5.conf.txt_log expsnorm/log/inst30-15-10-7.conf.txt/inst30-15-10-7.conf.txt_log expsnorm/log/inst30-15-10.conf.txt/inst30-15-10.conf.txt_log expsnorm/log/inst30-15-5.conf.txt/inst30-15-5.conf.txt_log expsnorm/log/inst30-5-10-5.conf.txt/inst30-5-10-5.conf.txt_log expsnorm/log/inst30-5-10-7.conf.txt/inst30-5-10-7.conf.txt_log expsnorm/log/inst30-5-10.conf.txt/inst30-5-10.conf.txt_log 
	 */
	public static void main(String[] args) {
		int runs = Integer.valueOf(args[0]);
		
		for (int f=1; f<args.length; ++f) {
			String baseFile = args[f];
				
			File resumoFile = new File(baseFile + "_summary.csv");
			PrintWriter resumoFileWriter = null;
			File dir = resumoFile.getParentFile();
			if (dir != null) {
				dir.mkdirs();
			}
			try {
				resumoFile.createNewFile();
				resumoFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(resumoFile)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			resumoFileWriter.println("iteration\tevaluations\truntime[s]\tCOSTDUR[MIN]\tcost\tduration\tundt\treqsk\toverwork\thitrate\tphenotypeBeforeNormalisation");
			for (int r = 0; r < runs; ++r) {
				File file = new File(baseFile + r + ".txt");
				Scanner in;
				String line = "";
	
				try {
					in = new Scanner(file);
					while (in.hasNext())
						line = in.nextLine();
					
				} catch (FileNotFoundException e) {
					System.out.println("File not found: " + baseFile + r + ".txt");
					e.printStackTrace();
				}
				
				// Print last line and its hit rate
				StringTokenizer myTokenizer = new StringTokenizer(line, "\t" ); 
				String token = "";
				for (int c=0; c<10; ++c) { // from column iteration until overwork
					token = myTokenizer.nextToken();
					resumoFileWriter.print(token + "\t");
					if (c == 8) {
						resumoFileWriter.print("=IF(AND(G" + (r+2) + "= 0;H" + (r+2) + " = 0;I" + (r+2) + " = 0);1;0)\t");
					}
				}
				resumoFileWriter.println();
				//resumoFileWriter.println(line);
			}
			
			resumoFileWriter.println("AVERAGE\t=AVERAGE(B2:B101)\t=AVERAGE(C2:C101)\t=AVERAGE(D2:D101)\t=AVERAGE(E2:E101)\t=AVERAGE(F2:F101)\t=AVERAGE(G2:G101)\t=AVERAGE(H2:H101)\t=AVERAGE(I2:I101)\t=SUM(J2:J101)");
			resumoFileWriter.println("Av. p_cost / p_dur\t \t \t \t \t=E102/F102\t=SUM(G2:G101)\t=SUM(H2:H101)\t=SUM(I2:I101)");
			resumoFileWriter.println("Stdev\t \t \t=STDEV(D2:D101)\t=STDEV(E2:E101)\t=STDEV(F2:F101)\t \t \t=IF(I103=0;1;0)");
			resumoFileWriter.println(" \t \t \t=D104/D102\t=E104/E102\t=F104/F102");
			resumoFileWriter.close();
			try {
				if (baseFile.contains("expsnorm"))
					Runtime.getRuntime().exec("mv " + baseFile + "_resumo.csv expsnorm/resumos");
				else if (baseFile.contains("expsnonorm"))
					Runtime.getRuntime().exec("mv " + baseFile + "_resumo.csv expsnonorm/resumos");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
