// Date.java
// Stores a date in MM/DD/YYYY format
//
////////////////////////////////////////////////////////////////
public class Date
{
	public byte month, day;
	public int year;
//--------------------------------------------------------------
	public Date(int m, int d, int y) // constructor
	{
		month = (byte)m;
		day = (byte)d;
		year = y;
	}
//--------------------------------------------------------------
	public void display() //print the date
	{
		if(month<10)
		{
			System.out.print("0");
		}
		System.out.print(month + "/");
		
		if(day<10)
		{
			System.out.print("0");
		}
		System.out.print(day + "/" + year);
	}
} // end of class Date
////////////////////////////////////////////////////////////////
