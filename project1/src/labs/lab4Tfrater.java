package labs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class lab4Tfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JTextField answerField = new JTextField(10);
	private JTextField userTimeField = new JTextField(10);
	private JButton submitButton = new JButton("submit");
	private JButton beginButton = new JButton("begin");
	private JButton endDialogButton = new JButton("cancel");
	private JButton endQuizButton = new JButton("End Quiz");
	private JTextArea outputArea = new JTextArea(20,20);
	private JLabel dialogLabel = new JLabel("How much Time Would You Like?");
	private JLabel dialogUnitsLabel = new JLabel("sec");
	private JLabel questionLabel = new JLabel();
	private JLabel timerLabel = new JLabel();
	private int aminoPosition;
	private int correctCount;
	private int incorrectCount;
	private int noResponseCount;
	private MyDialog dlg = new MyDialog(null);
	private List<String> correctList = new LinkedList<String>();
	private List<String> incorrectList = new LinkedList<String>();
	private List<String> noResponseList = new LinkedList<String>();
	private boolean cancel = false;
	
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
	
	private class InputTest implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Random random = new Random();
			if(answerField.getText().isEmpty()) {
				outputArea.append("User Input: No response" + "\n");
				outputArea.append(questionLabel.getText()+" is "+SHORT_NAMES[aminoPosition]+"\n\n");
				noResponseCount++;
				noResponseList.add(questionLabel.getText());
				aminoPosition = random.nextInt(20);
				answerField.setText("");
				questionLabel.setText(FULL_NAMES[aminoPosition]+":");
			} 
			else {
				outputArea.append("User Input: "+answerField.getText() + "\n");
				if(answerField.getText().toUpperCase().equals(SHORT_NAMES[aminoPosition])) {
					outputArea.append("Correct" + "\n\n");
					correctCount++;
					correctList.add(questionLabel.getText());
					aminoPosition = random.nextInt(20);
					answerField.setText("");
					questionLabel.setText(FULL_NAMES[aminoPosition]+":");
				} 
				else {
					outputArea.append("Incorrect" + "\n"+questionLabel.getText()+" is "+SHORT_NAMES[aminoPosition]+"\n\n");
					incorrectCount++;
					incorrectList.add(questionLabel.getText());
					aminoPosition = random.nextInt(20);
					answerField.setText("");
					questionLabel.setText(FULL_NAMES[aminoPosition]+":");
				}
			}
		}
	}
	
	private class MyDialog extends JDialog {
		private static final long serialVersionUID = 3794059922116115530L;
		public MyDialog(JFrame parent) {
			super(parent, "My dialog", true);
			getContentPane().setLayout(new BorderLayout());
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			JPanel northPanel = new JPanel();
			northPanel.setLayout(new GridLayout(0,2));
			northPanel.add(userTimeField);
			northPanel.add(dialogUnitsLabel);
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(beginButton);
			beginButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Integer.parseInt(userTimeField.getText());
						Random random = new Random();
						aminoPosition = random.nextInt(20);
						questionLabel.setText(FULL_NAMES[aminoPosition]+":");
						new Thread(new EndConditionsThread()).start();
						dispose();
					}
					catch (Exception excep) {
						JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			buttonPanel.add(endDialogButton);
			endDialogButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			getContentPane().add(dialogLabel,BorderLayout.NORTH);
			getContentPane().add(northPanel,BorderLayout.CENTER);
			getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			getRootPane().setDefaultButton(beginButton);
			setSize(250,200);
		}
	}
	
	private class EndConditionsThread implements Runnable {
		public void run() {
			int timeTotal = Integer.parseInt(userTimeField.getText());
			int timeCount = 0;
			try {
				while(timeCount != timeTotal && cancel==false) {
					timerLabel.setText("Time Left: "+String.valueOf(timeTotal - timeCount)+"sec");
					timeCount++;
					Thread.sleep(1000);
					}
				} 
			catch (Exception ex) {
					outputArea.append(ex.getMessage());
			}
			submitButton.setEnabled(false);
			timerLabel.setText("Time Left: "+String.valueOf(timeTotal - timeCount)+"sec");
			quizResults();
		}
	}
	
	public void quizResults() {
		outputArea.append("**********Quiz Ended**********\n");
		outputArea.append("Correct: "+correctCount+"\n"+correctList+"\n");
		outputArea.append("Incorrect: "+incorrectCount+"\n"+incorrectList+"\n");
		outputArea.append("No Response(Incorrect): "+noResponseCount+"\n"+noResponseList+"\n");
		int totalScore = correctCount+incorrectCount+noResponseCount;
		double finalScore = ((double) correctCount/totalScore)*100;
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		outputArea.append("Score: "+decimalFormat.format(finalScore)+"%");
		JOptionPane.showMessageDialog(null, "Results of quiz are at the bottom of the text area", "Quiz Ended", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public lab4Tfrater() {
		super("Amino Acid Quiz");
		setLocationRelativeTo(null);
		setSize(700,500);
		dlg.setVisible(true);
		getContentPane().setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1,3));
		northPanel.add(timerLabel);
		northPanel.add(questionLabel);
		northPanel.add(answerField);
		northPanel.add(submitButton);
		submitButton.addActionListener(new InputTest());
		getContentPane().add(northPanel,BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(outputArea),BorderLayout.CENTER);
		outputArea.setEditable(false);
		getContentPane().add(endQuizButton,BorderLayout.SOUTH);
		endQuizButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel=true;
			}
		});
		getRootPane().setDefaultButton(submitButton);
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new lab4Tfrater();
	}
}