// Candle.java
// Contains a set of data on a given trading day.
// The fields store the day's date as well as low, high, open and close prices
// Based on the adjClose value adjOpen adjHigh and adjLow prices are calculated
// The trade volume per day is also stored
//
////////////////////////////////////////////////////////////////
public class Candle
{
	public Date today;
	public double open, high, low, close, adjOpen, adjHigh, adjLow, adjClose;
	public int volume;
	public double average3=0;
	public double average15;
	public int period1;
	public int period2;
	public double percentage;
	public double SMAclose15[] = new double [10];
	public double SMAclose3[] = new double [3];
//--------------------------------------------------------------
	public Candle(Date d, double o, double h, double l, double c, int v, double a) // constructor
	{
		today=d; open=o; high=h; low=l; close=c; volume=v; adjClose=a;
		double ratio = adjClose / close;
		adjOpen = open * ratio; adjHigh = high * ratio; adjLow = low * ratio;
	}
//--------------------------------------------------------------
	public boolean isGreen() // returns true if the candle is green, and false if red
	{
		return (close > open);
	}
//--------------------------------------------------------------
	public boolean is3percent() 
	{
		return (((Math.abs(high-low))/open)*100 >= 3 && (Math.abs(open-close))/(high-low) >= 0.5);
	}
//--------------------------------------------------------------
	public void SMA3()
	{
		period2 += close;
	}
	
	public void SMA3Avg()
	{
		average3 += (period2/3);
	}
	
	public void SMA10()
	{
		period1 += close;
	}
	
	public void SMA10Avg()
	{
		average15 = period1/10;
	}
	
	public boolean SMA()
	{
		percentage = ((average3 - average15)/average3)*100;
		
		return(percentage <= 99);
	}
	/**
	 * This method takes as a parameter the previous candle.
	 * It compares the current candle's data with the previous candle's data in order to find engulfings.
	 * @param prev: A candle preceding the current candle.
	 * @return: The method returns 'L' if it finds a bullish engulfing, 'S' for bearish and '-' otherwise.
	 */
	public char compare(Candle prev)
	{		
		if(this.isGreen() && !prev.isGreen()) // found green candle following red
		{
			if(this.high>=prev.high && this.low<=prev.low && this.open<=prev.close && this.close>=prev.open)
			{
				if(this.is3percent())
				{
					return 'L'; // bullish engulfing
				}
			}
		}
		
		if(!this.isGreen() && prev.isGreen()) // found red candle following green
		{
			if(this.high>=prev.high && this.low<=prev.low && this.open>=prev.close && this.close<=prev.open)
			{
				if(this.is3percent())
				{
					return 'S'; // bearish engulfing
				}
			}
		}
		
		
		return '-'; // no engulfing
	}
//--------------------------------------------------------------
	public void display() // displays the candle data
	{
		today.display();
		System.out.println(": Open=" + open + ", High=" + high + ", Low=" + low +
				", Close=" + close + ", Volume=" + volume + ", Adj.Open=" + adjOpen +
				", Adj.High=" + adjHigh + ", Adj.Low=" + adjLow + ", Adj.close=" + adjClose);
	}
} // end of class Candle
////////////////////////////////////////////////////////////////
