package experiments;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Primes {
	public static int [] readPrimes() {
		
		File file = new File("10000primesedt.csv");
		Scanner in;
		
		int [] primes = new int[10000];
		
		try {
			in = new Scanner(file);
			for (int i=0; i<10000; i++)
				primes[i] = Integer.valueOf(in.next());			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found: 10000primesedt.csv");
			e.printStackTrace();
		}
		
		return primes;
	}
}
