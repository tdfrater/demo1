package labs;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class lab4Tfrater extends JFrame
{
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JTextField aTextField = new JTextField();
	private JButton beginButton = new JButton("Begin Quiz");
	
	public lab4Tfrater()
	{
		super("Amino Acid Quiz");
		setLocationRelativeTo(null);
		setSize(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(beginButton, BorderLayout.SOUTH);
		getContentPane().add(aTextField, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new lab4Tfrater();
	}
}