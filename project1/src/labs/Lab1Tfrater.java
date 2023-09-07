package labs;

import java.util.Random;

public class Lab1Tfrater

{
	//Method that handles equal frequency and uniform sampling
	public static int uniformFreq()
	{
		Random random = new Random();
		String s = "ATGC";
		//check is used to keep track of AAA count
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
	//Method in charge of unequal sampling probabilities
	public static String nonuniformFreq()
	{	
		Random random = new Random();
		//The array of doubles is the probabilities of sampling elements in the array of strings
		String a[] = {"A","C","G","T"};
		double b[] = {0.12,0.38,0.39,0.11};
		double d = random.nextDouble(1);
		for(int i = 0; i < a.length; i++)
		{
			if(d < b[i])
				return a[i];
			else {
				d = d - b[i];
			}
		}
		//This return statement is necessary in case the above for loop returns nothing
		return a[random.nextInt(4)];
	}
	//This method takes the output from nonuniformFreq() then builds and tracks the 3mers
	public static int nonuniformFreqCheck()
	{
		int check2 = 0;
		for(int x = 0; x < 1000; x++)
		{
			String o = "";
			for(int y = 0; y < 3; y++)
				o = o + nonuniformFreq();
			if(o.equals("AAA"))
				check2++;
		}
		return check2;
	}
	public static void main(String[] args) 
	{
		System.out.println("Lab_1 Theodore Frater tfrater@uncc.edu");
		System.out.println("1.) The code for this question is inside the uniformFreq method");
		System.out.println("2.) Here is the AAA count for a uniformly sampled and uniform frequency of ATGC: " + uniformFreq() + "\n\t Expected value = (0.25)^3 * 1000 = 16");
		System.out.println("3.) Here is the AAA count for a non-uniformly sampled and uniform frequency of ATGC: " + nonuniformFreqCheck() + "\n\t Code for this question is in nonuniformFreq() and nonuniformFreqCheck() method" + "\n\t Expected value = (0.12)^3 * 1000 = 2");
		}
	}