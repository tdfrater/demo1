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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class FinalProjectTfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	//Text area where results and instructions are appended
	private JTextArea outputArea = new JTextArea(20,20);
	private String[][] itmList = {};
	private String[] colNames = {"Type","Name","Amount","DueDate"};
	private DefaultTableModel model = new DefaultTableModel(itmList, colNames);
	private JTable outputTable = new JTable(model);
	private JTextField typeField = new JTextField(10);
	private JTextField nameField = new JTextField(10);
	private JTextField amountField = new JTextField(10);
	private JTextField dueDate = new JTextField(10);
	
	private class DataDialog extends JDialog {
		private static final long serialVersionUID = 3794059922116115530L;
		public DataDialog(JFrame parent) {
			super(parent, "Data Editor",true);
			JButton addButton = new JButton("Add");
			JButton cancelButton = new JButton("Cancel");
			JPanel buttonPanel = new JPanel(new GridLayout(1,2));
			String[] comboChoices = {"Income","Expense"};
			JComboBox<String> typeBox = new JComboBox<>(comboChoices);
			buttonPanel.add(addButton);
			buttonPanel.add(cancelButton);
			JPanel quesPanel = new JPanel(new GridLayout(4,4,10,4));
			quesPanel.add(new JLabel("Is this Income or an Expense?"));
			quesPanel.add(typeBox);
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(quesPanel,BorderLayout.CENTER);
			getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setSize(500,200);
		}
	}
	
	public FinalProjectTfrater() {
		super("Budget Calculator");
		JButton submitButton = new JButton("Open Budget");
		JButton editButton = new JButton("Edit Budget");
		JButton cancelButton = new JButton("Cancel");
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		buttonPanel.add(submitButton);
		buttonPanel.add(editButton);
		buttonPanel.add(cancelButton);
		setLocationRelativeTo(null);
		setSize(650,300);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(outputArea),BorderLayout.CENTER);
		getContentPane().add(new JScrollPane(outputTable),BorderLayout.CENTER);
		outputArea.setEditable(false);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int incomeTotal = 0;
					int expenseTotal = 0;
					// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
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
							String[] newLine = {type,name,amountString,dueDate};
//							if(type.equals("income")) {
//								int amount = Integer.parseInt(amountString);
//								incomeTotal += amount;
//							}
//							else if(type.equals("expense")) {
//								int amount = Integer.parseInt(amountString);
//								expenseTotal += amount;
//							}
							model.addRow(newLine);
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
		editButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					DataDialog dlg = new DataDialog(null);
					dlg.setVisible(true);
				}
				catch (Exception excep){
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