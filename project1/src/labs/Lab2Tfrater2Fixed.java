package labs;

import java.util.Random;

public class Lab2Tfrater2Fixed
{
	//Character string that contains the amino acid short names
	public static String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };
	//Character string that contains the amino acid long names
	public static String[] FULL_NAMES = 
	{
	"alanine","arginine", "asparagine", 
	"aspartic acid", "cysteine",
	"glutamine",  "glutamic acid",
	"glycine" ,"histidine","isoleucine",
	"leucine",  "lysine", "methionine", 
	"phenylalanine", "proline", 
	"serine","threonine","tryptophan", 
	"tyrosine", "valine"};
	
	//Method that randomly selects an amino acid short name
	public static void ranAmino_time(int userTimeSec)
	{
		System.out.println("You will be given amino acid full names. Your job is to give the one letter code case is not an issue");
		Random random = new Random();
		//Tracks number of correct answers
		int corrAns = 0;
		//Tracks incorrect answers
		int incorrAns = 0;
		//Tracks the start time
		long startTime = System.currentTimeMillis();
		//Supposed to be a way to break out of the while loop
		int exit_int = 0;
		while (exit_int == 0)
		{
			
			//Also supposed to help with breaking out of the while loop but the loop only runs once
			long testTime = System.currentTimeMillis();
			if((testTime-startTime)/1000f >= userTimeSec) {
				exit_int++;}
			
			//Tracks the amino acid position of choice
			int aminoPosition = random.nextInt(20);
			//chooses amino acid
			String aminoChoice_question = FULL_NAMES[aminoPosition];
			System.out.println("\n"+aminoChoice_question);
			//prompts the user for their answer
			String quizAnswer_user = System.console().readLine().toUpperCase();
			
			//if else that handles if the user is correct or not
			if(quizAnswer_user.equals(SHORT_NAMES[aminoPosition]))
			{
				corrAns++;
				long endTime = System.currentTimeMillis();
				System.out.println("Correct " + (endTime-startTime)/1000f + " seconds out of " + userTimeSec + " seconds left");
			}
			else {
				incorrAns++;
				long endTime = System.currentTimeMillis();
				System.out.println("Incorrect the correct answer was " + SHORT_NAMES[aminoPosition] + "\n" + (endTime-startTime)/1000f + " seconds out of " + userTimeSec + " seconds left");
			}
		}
		System.out.println("\nYou got " + corrAns + " out of " + (corrAns+incorrAns));
	}
	//Main method
	public static void main(String[] args) 
	{
		System.out.println("Lab_2 Theodore Frater tfrater@uncc.edu");
		System.out.println("Amino Acid Review Quiz");
		System.out.println("Enter the amount of time you would like in seconds. Only enter the number.");
		String timeChoice_user = System.console().readLine();
		ranAmino_time(Integer.parseInt(timeChoice_user));
		
		}

}