package labs;

import java.util.Random;
//import java.util.List;
//import java.util.ArrayList;

public class Lab1Test

{
	public static void main(String[] args) 
	{
		String s = "ATGC";
		Random random = new Random();
		
		int check = 0;
		int check2 = 0;
		
		for(int x = 0; x < 1000; x++)
		{
			String o = "";
			for(int y = 0; y < 3; y++)
				o = o + s.charAt(random.nextInt(4));
			if(o.equals("AAA"))
				check++;
//			System.out.println("equal: " + o + " " + x);
		}

		
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
//			System.out.println("unequal: " + o + " " + a);
		}
		System.out.println("Uniform Probability: " + check);
		System.out.println("Non-uniform Probability: " + check2);
		}
	}