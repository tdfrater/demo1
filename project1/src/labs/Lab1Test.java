package labs;

import java.util.Random;

public class Lab1Test

{
	public static int uniformFreq()
	{
		Random random = new Random();
		String s = "ATGC";
		int check = 0;
		for(int x = 0; x < 1000; x++)
		{
			String o = "";
			for(int y = 0; y < 3; y++)
				o = o + s.charAt(random.nextInt(4));
			if(o.equals("AAA"))
				check++;
		}
		return check;
	}
	
	public static int nonuniformFreq()
	{
		Random random = new Random();
		int check2 = 0;
		for(int a = 0; a < 1000; a++)
		{
			String o = "";
			for( int b = 0; b < 3; b++)
			{
				float f = random.nextFloat(1);
				if(f <= 0.11)
					o = o + "A";
				else if(f > 0.11 && f <= 0.49)
					o = o + "C";
				else if(f > 0.49 && f <= 0.88)
					o = o + "G";
				else if(f > 0.88)
					o = o + "T";
				if(o.equals("AAA"))
					check2++;
			}
		}
		return check2;
	}
	public static void main(String[] args) 
	{
		System.out.println("Lab_1 Theodore Frater tfrater@uncc.edu");
		System.out.println("1.) The code for this question is inside the uniformFreq method");
		System.out.println("2.) Here is the AAA count for a uniformly sampled and uniform frequency of ATGC: " + uniformFreq() + "\n\t Expected value = (0.25)^3 * 1000 = 16");
		System.out.println("3.) Here is the AAA count for a non-uniformly sampled and uniform frequency of ATGC: " + nonuniformFreq() + "\n\t Code for this question is in nonuniformFreq() method" + "\n\t Expected value = (0.12)^3 * 1000 = 2");
		}
	}