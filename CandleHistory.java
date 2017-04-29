// CandleHistory.java
// Upon instantiation imports candle data from a file into an array of candle objects
////////////////////////////////////////////////////////////////
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CandleHistory
{
	public String stockName, abbreviation, fileName, importPath;
	public Candle[] history;
	public Engulfing[] allEngulfings;
	public int candleCount, enCount = 0;
//--------------------------------------------------------------	
	public CandleHistory(String sName, String abbr, String path, String fName) throws FileNotFoundException	// constructor
	{
		stockName = sName; importPath = path; abbreviation = abbr; fileName = fName;
		
		Scanner count = new Scanner(new File(importPath+fName));
		System.out.println("Importing the candle data...");
		int candles = -1;					// candle count less header line
		
		while(count.hasNextLine())
		{
			candles++; 
			count.nextLine();	// counting candles
		}
		
		System.out.println("Found " + candles + " candles");
		candleCount = candles;
		count.close();
		
		Scanner read = new Scanner(new File(importPath+fName));
		history = new Candle[candles];		// declare array to store candles
		int lineCtr = 0;
		read.nextLine();					// skip header line
		
		while(read.hasNextLine())			// importing candles
		{
        	String[] x = read.nextLine().split(",");	// split pieces of data
        	String[] d = x[0].split("/");				// split date
        	history[lineCtr] = new Candle(new Date(Byte.parseByte(d[0]), Byte.parseByte(d[1]), Integer.parseInt(d[2])),
        			Double.parseDouble(x[1]), Double.parseDouble(x[2]), Double.parseDouble(x[3]), Double.parseDouble(x[4]),
        			Integer.parseInt(x[5]), Double.parseDouble(x[6]));	// instantiate a new candle
	        lineCtr++;
        }
		
		System.out.println("Importing complete.");
		read.close();
	}
//--------------------------------------------------------------
	public int findEngulfings()				// find all engulfings
	{
		allEngulfings = new Engulfing[(int)(candleCount * 0.08)]; // estimated max array size (8% of total candle count)
		for(int i=1; i<candleCount-10; i++) // leave enough room for data analysis
		{
			char en = history[i].compare(history[i-1]);
			if(en!='-')						// found an engulfing
			{
				double p;
				int b = 2;					// best holding day (2~9)
				double sign;				// profit/loss prevention multiplier
				
				if(en=='L')					// profit
				{
					sign=1.0;		
				}
				else 						// loss prevention
				{
					sign=-1.0;				
				}
				double maxProfit = -1000.0;
				
				for(int j=1; j<10; j++)	// check for best holding time
				{
					p = sign * 100.0 * (history[i+1+j].adjOpen / history[i+1].adjOpen - 1.0);
					if(p > maxProfit)
					{
						maxProfit = p;
						b = j+1;
					}
				}
				
				allEngulfings[enCount] = new Engulfing(history[i].today, en, history[i+1].adjOpen, maxProfit, b, i);
				enCount++;					// instantiate Engulfing object
			}
		}
		return enCount;
	}
//--------------------------------------------------------------
	public void exportEngulfings() throws IOException
	{
		String[] field = new String[3];		// setting up second column field names
		field[0] = "DATE"; field[1] = "PRICE[$]"; field[2] = "PROFIT[%]";
		String filePath = importPath+"eng_"+fileName;
		File f = new File(filePath);
		
		if(!f.exists())
		{
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		System.out.println("Exporting data into " + filePath);
		
		char t;	// engulfing type
		char SMA;
		bw.write("TYPE,SMA,ENG#,FIELD,DAY 0,DAY 1,DAY 2,DAY 3,DAY 4,DAY 5,DAY 6,DAY 7,DAY 8,DAY 9,DAY 10\n");	// table header
		
		for(int eng=0; eng<enCount; eng++)	// writing the engulfings data in a file
		{
			double profit;
			
			if(history[allEngulfings[eng].index].isGreen())
			{
				t='L';
			}
			else 
			{
				t = 'S';
			}
			
			for(int i = 0; i <=10; i++)
			{
				history[allEngulfings[eng].index+i].SMA3();
				history[allEngulfings[eng].index+i].SMA10();
				
				if(i % 3 == 0)
				{
					history[allEngulfings[eng].index+i].SMA3Avg();
				}
				
				if(i == 10)
				{
					history[allEngulfings[eng].index+i].SMA10Avg();
				}
			}
			
			if(history[allEngulfings[eng].index].SMA())
			{
				SMA = 'S';
			}
			else
			{
				SMA = 'L';
			}
			
			for(int i=0; i<3; i++)	// three lines per engulfing
			{
				bw.write(t + "," + SMA + "," + (eng+1) + "," + field[i] + ",");
				
				for(int j=0; j<=10; j++)	// 11 days
				{
					switch(i)
					{
						case 0:	// writing dates
								bw.write(history[allEngulfings[eng].index+j].today.month + "/");
								bw.write(history[allEngulfings[eng].index+j].today.day + "/");
								bw.write(history[allEngulfings[eng].index+j].today.year + ",");
								break;
						case 1:	// writing prices
								bw.write(history[allEngulfings[eng].index+j].adjOpen + ",");
								break;
						case 2:	// writing profits
								if(j == 0)
								{
									bw.write("N/A,");
								}
								else if(j == 1)
								{
									bw.write("0,");
								}
								else
								{
									profit = 100.0 * (history[allEngulfings[eng].index+j].adjOpen /
											history[allEngulfings[eng].index+1].adjOpen - 1.0);
									if(allEngulfings[eng].engulfingType == "Bearish")
									{
										profit *= -1.0;
									}
									bw.write(profit + ",");
								}
								break;
					}
				}
				bw.write("\n");
			}
		}
		System.out.println("Exporting complete.");
		bw.close();
	}
//--------------------------------------------------------------
	public void showEngulfings()			// display all engulfings found
	{
		for(int i=0; i<enCount; i++)
		{
			allEngulfings[i].display();
		}
	}
} // end of class CandleHistory
////////////////////////////////////////////////////////////////
