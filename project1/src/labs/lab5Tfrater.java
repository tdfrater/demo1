package labs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class lab5Tfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	private JTextField numChoiceField = new JTextField();
	private JTextField numThreadsField = new JTextField();
	
	
	
	private class primeGenerator implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	//Main that starts the program
	public static void main(String[] args) {
		new lab5Tfrater();
	}
}