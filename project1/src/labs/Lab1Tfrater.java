package labs;

import java.util.Random;
//import java.util.List;
//import java.util.ArrayList;

public class Lab1Tfrater

{
	public static void main(String[] args) 
	{
		String s = "ATGC";
		String o = "";
		
		Random random = new Random();
		
		int check = 0;
		
		for(int x = 0; x < 1001; x++)
			for(int y = 0; y < 3; y++)
				o = o + s.charAt(random.nextInt(4));
				// Add a way to keep track of AAA
				if(o == "AAA")
					++check;
		System.out.println(check);
		
//		System.out.println(choice);
//		System.out.println(s.charAt(choice));
		}
	}