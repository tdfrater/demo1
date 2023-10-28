package labs;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class lab5Tfrater extends JFrame
{
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JLabel instructions = new JLabel("Enter a large number");
	private JTextField userInputNum = new JTextField(10);
	private JButton startButton = new JButton("start");
	private JButton okButton = new JButton("okay");
	private JButton startCancelButton = new JButton("cancel");
	private JButton programCancelButton = new JButton("cancel");
	private JPanel panel = new JPanel();
	
	public lab5Tfrater()
	{
		super("Prime Number Finder");
		setLocationRelativeTo(null);
		setSize(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		panel.add(instructions);
		panel.add(userInputNum);
		panel.add(okButton);
		panel.add(startCancelButton);
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new lab5Tfrater();
	}
}