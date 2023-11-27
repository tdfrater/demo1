package tfrater;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class FinalProjectTfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	//Text area where results and instructions are appended
	private JTextArea outputArea = new JTextArea(20,20);
	
	public FinalProjectTfrater() {
		super("Budget Calculator");
		JButton submitButton = new JButton("Run");
		JButton cancelButton = new JButton("Cancel");
		JPanel buttonPanel = new JPanel(new GridLayout(1,2));
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		setLocationRelativeTo(null);
		setSize(650,300);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(outputArea),BorderLayout.CENTER);
		outputArea.setEditable(false);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int incomeTotal = 0;
					int expenseTotal = 0;
					JFileChooser fileChooser = new JFileChooser();
					int returnVal = fileChooser.showOpenDialog(FinalProjectTfrater.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						BufferedReader reader = new BufferedReader(new FileReader(file));
						//String that holds each line in the file
						String nextLine = "";
						while (( nextLine = reader.readLine()) != null) {
							String[] splitList = nextLine.split(",");
							String type = splitList[0];
							String name = splitList[1];
							String amountString = splitList[2];
							String dueDate = splitList[3];
							String endDate = splitList[4];
							if(type.equals("income")) {
								int amount = Integer.parseInt(amountString);
								incomeTotal += amount;
							}
							else if(type.equals("expense")) {
								int amount = Integer.parseInt(amountString);
								expenseTotal += amount;
							}
						}
						reader.close();
					}
					String spendable = Integer.toString((incomeTotal - expenseTotal));
					outputArea.append("You have "+spendable+" to spend this month");
				}
				catch (Exception excep) {
					JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		getRootPane().setDefaultButton(submitButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}
	//Main that starts the program
	public static void main(String[] args) {
		new FinalProjectTfrater();
	}
}