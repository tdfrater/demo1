package tfrater;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class FinalProjectTfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
	private JFileChooser fileChooser = new JFileChooser();
	private int cellHeight = 800;
	private int cellWidth = 400;
	//Text area where results and instructions are appended
	private JTextArea outputArea = new JTextArea(20,cellWidth/12);
	private String[][] itmList = {};
	private String[] colNames = {"Type","Name","Amount","DueDate"};
	private DefaultTableModel model = new DefaultTableModel(itmList, colNames);
	private JTable outputTable = new JTable(model);
//	private JTextField typeField = new JTextField(10);
	private JTextField nameField = new JTextField(10);
	private JTextField amountField = new JTextField(10);
	private JTextField dueDate = new JTextField(10);
	
	private class TableUpdate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int incomeTotal = 0;
				int expenseTotal = 0;
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
						if(type.equals("income")) {
							int amount = Integer.parseInt(amountString);
							incomeTotal += amount;
						}
						else if(type.equals("expense")) {
							int amount = Integer.parseInt(amountString);
							expenseTotal += amount;
						}
						model.addRow(newLine);
					}
					reader.close();
				}
				String spendable = Integer.toString((incomeTotal - expenseTotal));
				outputArea.append("Budget Calculator"+"\n");
				outputArea.append("You have "+spendable+" to spend this month"+"\n");
			}
			catch (Exception excep) {
				JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private class DataDialog extends JDialog {
		private static final long serialVersionUID = 3794059922116115530L;
		public DataDialog(JFrame parent) {
			super(parent, "Data Editor",true);
			JButton addButton = new JButton("Update");
			JButton cancelButton = new JButton("Finish");
			JPanel buttonPanel = new JPanel(new GridLayout(1,2));
			String[] comboChoices = {"Income","Expense"};
			JComboBox<String> typeBox = new JComboBox<>(comboChoices);
			buttonPanel.add(addButton);
			buttonPanel.add(cancelButton);
			JPanel quesPanel = new JPanel(new GridLayout(4,4,5,4));
			quesPanel.add(new JLabel("Is this Income or an Expense?"));
			quesPanel.add(typeBox);
			quesPanel.add(new JLabel("What name do you want to give this? It must be unique"));
			quesPanel.add(nameField);
			quesPanel.add(new JLabel("What amount? Just enter the numbers"));
			quesPanel.add(amountField);
			quesPanel.add(new JLabel("What day of the month is it due/recieved?"));
			quesPanel.add(dueDate);
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(quesPanel,BorderLayout.CENTER);
			getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			addButton.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					try {
						File file = fileChooser.getSelectedFile();
						BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
						String typeString = (String) typeBox.getSelectedItem();
						writer.append(typeString+","+nameField.getText()+","+amountField.getText()+","+dueDate.getText());
						writer.newLine();
						writer.close();
						String[] newLine = {typeString,nameField.getText(),amountField.getText(),dueDate.getText()};
						model.addRow(newLine);
						nameField.setText(null);
						amountField.setText(null);
						dueDate.setText(null);
//						outputArea.append((String) typeBox.getSelectedItem());
					}
					catch (Exception excep){
						JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setSize(500,500);
		}
	}
	
	public FinalProjectTfrater() {
		super("Budget Calculator");
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
//		JButton submitButton = new JButton("Open Budget");
//		JButton editButton = new JButton("Edit Budget");
		JButton cancelButton = new JButton("Finish");
		JPanel buttonPanel = new JPanel(new GridLayout());
		JPanel outputPanel = new JPanel(new GridLayout(1,2));
		JMenuItem openMenu = new JMenuItem("Open Budget");
		JMenuItem saveMenu = new JMenuItem("Save as");
		JMenuItem editExistMenu = new JMenuItem("Edit Existing Entry");
		JMenuItem addNewMenu = new JMenuItem("Add New Entry");
		JMenuItem removeMenu = new JMenuItem("Remove Entry");
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		editMenu.add(editExistMenu);
		editMenu.add(addNewMenu);
		addNewMenu.addActionListener(new ActionListener () {
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
		editMenu.add(removeMenu);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
		openMenu.addActionListener(new TableUpdate());
//		buttonPanel.add(submitButton);
//		buttonPanel.add(editButton);
		buttonPanel.add(cancelButton);
		setLocationRelativeTo(null);
		setSize(cellHeight,cellWidth);
		getContentPane().setLayout(new BorderLayout());
		outputPanel.add(new JScrollPane(outputArea));
		outputPanel.add(new JScrollPane(outputTable));
		getContentPane().add(outputPanel,BorderLayout.CENTER);
		outputArea.setEditable(false);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
//		submitButton.addActionListener(new TableUpdate());
//		editButton.addActionListener(new ActionListener () {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					DataDialog dlg = new DataDialog(null);
//					dlg.setVisible(true);
//				}
//				catch (Exception excep){
//					JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		});
//		getRootPane().setDefaultButton(submitButton);
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