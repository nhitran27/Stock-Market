// Engulfing.java
// Stores critical information about a single engulfing
//
////////////////////////////////////////////////////////////////
public class Engulfing
{
	public Date date;
	public String engulfingType;
	public double price, profit;
	public int bestHolding, index;
//--------------------------------------------------------------
	public Engulfing(Date d, char t, double prc, double p, int b, int i)	// constructor
	{
		date = d; price = prc; profit = p; bestHolding = b; index = i;
		if(t=='L')
		{
			engulfingType="Bullish";
		}
		else 
		{
			engulfingType="Bearish";
		}
	}
//--------------------------------------------------------------
	public void display()
	{
		System.out.print(engulfingType + " Engulfing: "); 
		date.display(); // display the engulfing data
		System.out.println(" Purchase Price: $" + Math.round(price*100.00)/100.00 + " Best Holding Time: " +
				bestHolding + " days, for " + Math.round(profit*100.00)/100.00 + "%");
	}
} // end of class Engulfing
////////////////////////////////////////////////////////////////
