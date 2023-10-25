package labs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class lab4TfraterGrayBox extends JFrame
{
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JLabel instructions = new JLabel("Give the one letter code based on the full amino acid name");
//	private JLabel inputLabel = new JLabel("How much time would you like?");
	private JTextField answer = new JTextField(10);
//	private JLabel units = new JLabel("seconds");
	private JPanel panel = new JPanel();
	private JButton beginButton = new JButton("Begin Quiz");
	
//	private class TextDefault implements FocusListener
//	{
//		@Override
//		public void focusGained(FocusEvent arg0)
//		{
//			if (answer.getText().equals("30"))
//			{
//				answer.setForeground(Color.gray);
//				answer.setText("");
//			}
//		}
//		@Override
//		public void focusLost(FocusEvent arg0)
//		{
//			if (answer.getText().equals(""))
//			{
//				answer.setForeground(Color.gray);
//				answer.setText("30");
//			}
//		}
//	}
	
	public lab4TfraterGrayBox()
	{
		super("Amino Acid Quiz");
		setLocationRelativeTo(null);
		setSize(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(beginButton, BorderLayout.SOUTH);
		panel.add(instructions);
//		panel.add(inputLabel);
//		answer.addFocusListener(new TextDefault());
		panel.add(answer);
//		panel.add(units,new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new lab4Tfrater();
	}
}