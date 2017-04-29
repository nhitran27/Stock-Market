// DriverClass.java
// This class needs to have the "path" and "file" variables configured manually
// The list.txt file should only contain the Companies names and stock abbreviations
// 	typed in the following format:
// Company1 name,ABB1
// Company2 name,ABB2
// etc.
// A number of .csv files named [ABB1].csv, [ABB2].csv and so forth should correspond
// 	to the number of companies and their respective abbreviations on the list.txt file.
// Both the list.txt file and all of the .csv files should be placed in a folder specified
// 	by the "path" variable below.
//
// If properly configured the program will create new .csv file(s) named eng_[ABBR].csv
// 	per each of the input .csv files which should be opened in MS Excel and further formatted
////////////////////////////////////////////////////////////////
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DriverClass {
	public static void main(String[] args) throws IOException
	{
		String path = "input files\\";	// save path - adjust as necessary #####################
		String file = "list.txt";	// csv file list - adjust as necessary #################
		
		Scanner count = new Scanner(new File(path+file));
		System.out.println("Fetching csv file names...");
		int fileCount = 0;
		
		while(count.hasNextLine())
		{
			fileCount++; count.nextLine();	// counting csv files
		}
		System.out.println("Identified " + fileCount + " files");
		count.close();
		
		Scanner read = new Scanner(new File(path+file));
		while(read.hasNextLine())
		{
			String[] stock = read.nextLine().split(",");	// split pieces of data
			
			System.out.println("\nProcessing " + stock[0] + " [" + stock[1] + "]...");
			CandleHistory hist = new CandleHistory(stock[0],stock[1],path,stock[1]+".csv");
			
			System.out.println("Found " + hist.findEngulfings() + " engulfings");
			hist.exportEngulfings();	// exporting each set of engulfings into a new .csv file
		}
		read.close();
	}
}
