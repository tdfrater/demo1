package labs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class lab4Tfrater extends JFrame
{
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JLabel instructions = new JLabel("Give the one letter code based on the full amino acid name");
	private JLabel aminoAcid = new JLabel("Alanine");
	private JTextField answer = new JTextField(10);
	private JPanel panel = new JPanel();
	
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
	public static void ranAmino_time(Long userTimeSec)
	{
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
			if(testTime > userTimeSec)
				exit_int++;
			
			//Tracks the amino acid position of choice
			int aminoPosition = random.nextInt(20);
			//chooses amino acid
			String aminoChoice_question = FULL_NAMES[aminoPosition];
			System.out.println(aminoChoice_question);
			//prompts the user for their answer
			String quizAnswer_user = System.console().readLine().toUpperCase();
			
			//if else that handles if the user is correct or not
			if(quizAnswer_user.equals(SHORT_NAMES[aminoPosition]))
			{
				corrAns++;
				long endTime = System.currentTimeMillis();
				System.out.println("\n Correct " + (endTime-startTime)/1000f + " seconds out of " + userTimeSec + " seconds left");
			}
			else {
				incorrAns++;
				long endTime = System.currentTimeMillis();
				System.out.println("\n Incorrect " + (endTime-startTime)/1000f + " seconds out of " + userTimeSec + " seconds left");
			}
		}
		System.out.println("You got " + corrAns + " out of " + (corrAns+incorrAns));
	}
	
	public lab4Tfrater()
	{
		super("Amino Acid Quiz");
		setLocationRelativeTo(null);
		setSize(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		panel.add(instructions);
		panel.add(answer);
		panel.add(aminoAcid);
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new lab4Tfrater();
	}
}