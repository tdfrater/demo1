package labs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class lab4TfraterGrayBox extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JTextField answerField = new JTextField(10);
	private JTextField userTimeField = new JTextField(10);
	private JButton submitButton = new JButton("submit");
	private JButton beginButton = new JButton("begin");
	private JButton endDialogButton = new JButton("cancel");
	private JButton cancelQuizButton = new JButton("End Quiz");
	private JTextArea outputArea = new JTextArea(20,40);
	private JLabel dialogLabel = new JLabel("How much Time Would You Like?");
	private JLabel dialogUnitsLabel = new JLabel("sec");
	private JLabel questionLabel = new JLabel();
	private JLabel timerLabel = new JLabel();
	private int aminoPosition;
	private JPanel dialogPanel = new JPanel();
	private JPanel questionPanel = new JPanel();
	
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
				outputArea.append("No response" + "\n");
				outputArea.append(userTimeField.getText() + "\n");
				answerField.setText("");
			} else {
				outputArea.append(answerField.getText() + "\n");
				if(answerField.getText().equals(SHORT_NAMES[aminoPosition])) {
					outputArea.append("Correct" + "\n");
					aminoPosition = random.nextInt(20);
					answerField.setText("");
					questionLabel.setText(FULL_NAMES[aminoPosition]+":");
				} else {
					outputArea.append("Incorrect" + "\n"+questionLabel.getText()+" is "+SHORT_NAMES[aminoPosition]+"\n");
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
			getContentPane().setLayout(new FlowLayout());
//			getContentPane().add(dialogLabel);
//			getContentPane().add(userTimeField);
//			getContentPane().add(beginButton);
			dialogPanel.add(dialogLabel);
//			dialogLabel.setAlignmentX(TOP_ALIGNMENT);
			dialogPanel.add(userTimeField);
			dialogPanel.add(dialogUnitsLabel);
			dialogPanel.add(beginButton);
			getContentPane().add(dialogPanel);
			beginButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Integer.parseInt(userTimeField.getText());
						Random random = new Random();
						aminoPosition = random.nextInt(20);
						questionLabel.setText(FULL_NAMES[aminoPosition]+":");
						
						dispose();
					}
					catch (Exception excep) {
						JOptionPane.showMessageDialog(null, "Please only enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			getContentPane().add(endDialogButton);
			endDialogButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			setSize(500,200);
		}
	}
	private MyDialog dlg = new MyDialog(null);
	
	
	
	public lab4TfraterGrayBox() {
		super("Amino Acid Quiz");
		setLocationRelativeTo(null);
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JOptionPane.showInputDialog(null, "Welcome to the Amino Acid Quiz","Hey",JOptionPane.INFORMATION_MESSAGE);
		dlg.setVisible(true);
		getContentPane().setLayout(new FlowLayout());
//		getContentPane().add(answerField);
//		getContentPane().add(submitButton);
		questionPanel.add(questionLabel);
		questionPanel.add(answerField);
		questionPanel.add(submitButton);
		questionPanel.add(timerLabel);
		submitButton.addActionListener(new InputTest());
		getContentPane().add(questionPanel);
		getContentPane().add(new JScrollPane(outputArea));
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new lab4TfraterGrayBox();
	}
}