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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import javax.swing.table.TableModel;


public class FinalProjectTfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	//Used to access the underlying file that all this information gets saved to
	private File file = new File("budget_calculatorFile.csv");
	//Text area where results and instructions are appended
	private JTextArea outputArea = new JTextArea();
	//The itmList and colNames are used to build the JTable model which controls layout of the JTable outputTable
	private String[][] itmList = {};
	private String[] colNames = {"Type","Name","Amount","DueDate"};
	//Sets the default table model and overwrites the users ability to edit the table
	private DefaultTableModel model = new DefaultTableModel(itmList, colNames) {
		//Eclipse suggested this for this class
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public boolean isCellEditable(int row, int column) {
	       //all cells false
	       return false;
	    }
	};
	//Table that shows the user the information that they have inputed
	private JTable outputTable = new JTable(model);
	//Formatter that lets me customize how date and time are shown to the user
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    //Lets me access the local date and time of the users system
	private LocalDateTime now = LocalDateTime.now();
	
	//Function used to save the current state of a JTable to a specified file. This is typically JTable outputTable to File file
	//This function really just rewrites the current save file with the current instance of the table that the user has manipulated
	private void saveTableData(JTable table, File inputFile) {
       try {
            TableModel model = table.getModel();
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile.getAbsolutePath()));
            // Write column names
			writer.write("Type,Name,Amount,DueDate");
			writer.newLine();
            // Write data by looping through the table model to access the values in the underlying array
			//Inefficient but works for now
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    writer.write(model.getValueAt(row, col).toString());
                    if (col < model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            writer.close();
	        outputArea.append("Your table instance was successfully saved to:"+"\n"+inputFile.getAbsolutePath()+"\n\n");
            
        } catch (IOException e) {
            e.printStackTrace();
            outputArea.append("There was an error in saving your table instance"+"\n\n");
        }
    }
	
	//This function updates the JTable by reading through File file
	private void tableUpdateFromFile() {
		try {
			//Check if File file exists and creating a blank version it if it doesn't
			//This triggers if the file does not exist
			if(file.createNewFile()) {
				outputArea.append("A new file was created for you:"+"\n"+file.getAbsolutePath()+"\n\n");
				outputArea.append("Use the add button in the edit tab to start adding to your budget"+"\n\n");
				BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
				writer.write("Type,Name,Amount,DueDate");
				writer.newLine();
				writer.close();
			}
			//This triggers if the file does exist
			else {
				outputArea.append("This program found an existing file:"+"\n"+file.getAbsolutePath()+"\n\n");
				outputArea.append("--------Calculated Budget--------"+"\n");
				outputArea.append(dateFormat.format(now)+"\n\n");
				outputArea.append("Expenses remaining for the month:"+"\n");
				//Integer used to keep track of total income
				int incomeTotal = 0;
				//Integer used to keep track of total expenses
				int expenseTotal = 0;
				//Boolean used to keep track of whether or not there were expenses left in the month
				Boolean expenseMonthBoolean = false;
				BufferedReader reader = new BufferedReader(new FileReader(file));
				//String that holds each line in the file
				String nextLine = "";
				while (( nextLine = reader.readLine()) != null) {
					if(!nextLine.startsWith("Type")) {
						//Parsing through each line of File file
						String[] splitList = nextLine.split(",");
						String type = splitList[0];
						String name = splitList[1];
						String amountString = splitList[2];
						String dueDate = splitList[3];
						String[] newLine = {type,name,amountString,dueDate};
						if(type.equals("Income")) {
							int amount = Integer.parseInt(amountString);
							incomeTotal += amount;
						}
						else if(type.equals("Expense")) {
							int amount = Integer.parseInt(amountString);
							expenseTotal += amount;
						}
						//Adding entries into the JTable if they are not empty
						if(newLine.length != 0)
							model.addRow(newLine);
						if(type.equals("Expense") && (Integer.parseInt(dueDate) > now.getDayOfMonth())) {
								outputArea.append("$"+amountString+" for "+name+" due in "+(Integer.parseInt(dueDate) - now.getDayOfMonth())+" days"+"\n\n");
								expenseMonthBoolean = true;
						}
					}
				}
				reader.close();
				if(expenseMonthBoolean == false)
					outputArea.append("No expenses left for this month"+"\n\n");
				String spendable = Integer.toString((incomeTotal - expenseTotal));
				if((incomeTotal - expenseTotal)<0)
						outputArea.append("You need to find $"+spendable+" to cover your expenses for this month"+"\n\n");
				else {
					outputArea.append("Your budget is $"+spendable+" for this month"+"\n");
					outputArea.append("Use it wiseley!"+"\n\n");
				}
			}
		}
		catch (Exception excep){
			excep.printStackTrace();
			outputArea.append("There was an error in updating your table from the file:"+"\n"+file.getAbsolutePath()+"\n\n");
		}
	}
	
	//This function also calculates Budget but this does not change the File file or the JTable
	private class BudgetCalculate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			outputArea.append("--------Calculated Budget--------"+"\n");
			outputArea.append(dateFormat.format(now)+"\n\n");
			outputArea.append("Expenses remaining for the month:"+"\n");
			//Integer used to keep track of total income
			int incomeTotal = 0;
			//Integer used to keep track of total expenses
			int expenseTotal = 0;
			//Boolean used to keep track of whether or not there were expenses left in the month
			Boolean expenseMonthBoolean = false;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				//String that holds each line in the file
				String nextLine = "";
				while (( nextLine = reader.readLine()) != null) {
					if(!nextLine.startsWith("Type")) {
						//Parsing through each line of File file
						String[] splitList = nextLine.split(",");
						String type = splitList[0];
						String name = splitList[1];
						String amountString = splitList[2];
						String dueDate = splitList[3];
						if(type.equals("Income")) {
							int amount = Integer.parseInt(amountString);
							incomeTotal += amount;
						}
						else if(type.equals("Expense")) {
							int amount = Integer.parseInt(amountString);
							expenseTotal += amount;
						}
						if(type.equals("Expense") && (Integer.parseInt(dueDate) > now.getDayOfMonth())) {
								outputArea.append("$"+amountString+" for "+name+" due in "+(Integer.parseInt(dueDate) - now.getDayOfMonth())+" days"+"\n\n");
								expenseMonthBoolean = true;
						}
					}
				}
				reader.close();
			}
			catch (Exception excep){
				excep.printStackTrace();
				outputArea.append("There was an error in trying to calculate your budget from:"+"\n"+file.getAbsolutePath()+"\n\n");
			}
			if(expenseMonthBoolean == false)
				outputArea.append("No expenses left for this month"+"\n\n");
			String spendable = Integer.toString((incomeTotal - expenseTotal));
			outputArea.append("You can spend $"+spendable+" for the rest of this month"+"\n");
			outputArea.append("Use it wiseley!"+"\n\n");
			validate();
		}
	}
	
	//Custom dialog box that handles adding new entries into the JTable
	private class AddDialog extends JDialog {
		private static final long serialVersionUID = 3794059922116115530L;
		public AddDialog(JFrame parent) {
			super(parent, "Data Editor",true);
			getContentPane().setLayout(new BorderLayout());
			//This section controls the fields and panels for user input
			//Combo box used to build a binary drop down list using the options in comboChoices
			String[] comboChoices = {"Income","Expense"};
			JComboBox<String> typeBox = new JComboBox<>(comboChoices);
			//The text fields used for user input
			JTextField nameField = new JTextField(10);
			JTextField amountField = new JTextField(10);
			JTextField dueDate = new JTextField(10);
			JPanel quesPanel = new JPanel(new GridLayout(4,4,5,4));
			quesPanel.add(new JLabel("Is this Income or an Expense?"));
			quesPanel.add(typeBox);
			quesPanel.add(new JLabel("What name do you want to give this? It must be unique"));
			quesPanel.add(nameField);
			quesPanel.add(new JLabel("What amount? Just enter the numbers"));
			quesPanel.add(amountField);
			quesPanel.add(new JLabel("What day of the month is it due/recieved?"));
			quesPanel.add(dueDate);
			getContentPane().add(quesPanel,BorderLayout.CENTER);
			//This section controls the buttons
			JButton addButton = new JButton("Update");
			JButton removeLastButton = new JButton("Remove Last");
			JButton cancelButton = new JButton("Finish");
			JPanel buttonPanel = new JPanel(new GridLayout(1,3));
			buttonPanel.add(addButton);
			buttonPanel.add(removeLastButton);
			buttonPanel.add(cancelButton);
			//Action listener that controls the add a new entry button
			addButton.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					try {
						//All these checks are to stop the user from inputting unwanted data types
						if(!(Integer.parseInt(amountField.getText()) > 0)) {
							throw new Exception();
						} else if(!(Integer.parseInt(dueDate.getText()) > 0 && Integer.parseInt(dueDate.getText()) < 31)) {
							throw new Exception();
						} else if(nameField.getText().equals("")) {
							throw new Exception();
						}
						else {
							String typeString = (String) typeBox.getSelectedItem();
							String[] newLine = {typeString,nameField.getText(),amountField.getText(),dueDate.getText()};
							model.addRow(newLine);
							model.fireTableDataChanged();
							nameField.setText(null);
							amountField.setText(null);
							dueDate.setText(null);
							saveTableData(outputTable, file);
						}
					}
					catch (Exception excep){
						JOptionPane.showMessageDialog(null, "Unfortunately the amount can only accpet Integers due to mistakes by the creator and please ensure that the due date is between 1 and 31\nThe name field can be anything but it cannot be empty.", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			//Action listener that controls the remove last button
			removeLastButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.removeRow(outputTable.getRowCount()-1);
				}
			});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			getRootPane().setDefaultButton(addButton);
			setSize(500,500);
		}
	}
	
	//The JFrame that controls the main landing page of the GUI
	public FinalProjectTfrater() {
		super("Budget Calculator");
		//The following controls the file menu bar and it's listeners
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem clearOutputMenu = new JMenuItem("Clear Output Area");
		JMenuItem clearDataMenu = new JMenuItem("Clear All Data");
		fileMenu.add(clearOutputMenu);
		clearOutputMenu.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					outputArea.setText(null);
					validate();
				}
				catch (Exception excep){
					excep.printStackTrace();
					outputArea.append("There was an error in trying to clear the outputArea");
				}
			}
		});
		fileMenu.add(clearDataMenu);
		clearDataMenu.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					if(file.delete()) {
						outputArea.setText(null);
						outputArea.append("You're table data was succssessfully cleared"+"\n");
						model.setRowCount(0);
						model.fireTableDataChanged();
						tableUpdateFromFile();
						model.fireTableDataChanged();
						validate();
					}
				}
				catch (Exception excep){
					excep.printStackTrace();
					outputArea.append("There was an error in trying to clear the entire data");
				}
			}
		});
		menuBar.add(fileMenu);
		//The following controls the edit menu tab and it's listeners
		JMenu editMenu = new JMenu("Edit");
		JMenuItem addNewMenu = new JMenuItem("Add New Entry");
		JMenuItem removeMenu = new JMenuItem("Remove Entry");
		editMenu.add(addNewMenu);
		addNewMenu.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					AddDialog dlg = new AddDialog(null);
					dlg.setVisible(true);
				}
				catch (Exception excep){
					excep.printStackTrace();
					outputArea.append("There was an error in trying to load the AddDialog");
				}
			}
		});
		editMenu.add(removeMenu);
		removeMenu.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					String removeChoiceString = JOptionPane.showInputDialog(removeMenu, "Please enter a whole number between 1 and "+ model.getRowCount()+" for the row that you would like to delete");
					int removeChoiceInt = Integer.parseInt(removeChoiceString);
					if(removeChoiceInt < 0 || removeChoiceInt > model.getRowCount())
						throw new Exception();
					model.removeRow(removeChoiceInt-1);
					model.fireTableDataChanged();
					saveTableData(outputTable, file);
					validate();
				}
				catch (Exception excep){
					excep.printStackTrace();
					outputArea.append("There was an error in loading the remove menu");
				}
			}
		});
		menuBar.add(editMenu);
		//Controls the menu and listeners for the calculate tab
		JMenu calculateMenu = new JMenu("Calculate");
		JMenuItem calculateBudgetMenu = new JMenuItem("Calculate Budget");
		calculateMenu.add(calculateBudgetMenu);
		calculateBudgetMenu.addActionListener(new BudgetCalculate());
		menuBar.add(calculateMenu);
		setJMenuBar(menuBar);
		//Controls the buttons and their listeners
		JButton cancelButton = new JButton("Finish");
		JPanel buttonPanel = new JPanel(new GridLayout());
		JPanel outputPanel = new JPanel(new GridLayout(1,2));
		buttonPanel.add(cancelButton);
		setLocationRelativeTo(null);
		setSize(900,400);
		getContentPane().setLayout(new BorderLayout());
		outputPanel.add(new JScrollPane(outputArea));
		outputPanel.add(new JScrollPane(outputTable));
		getContentPane().add(outputPanel,BorderLayout.CENTER);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
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
		outputArea.setEditable(false);
		getRootPane().setDefaultButton(cancelButton);
		outputArea.append("--------Budget Calculator--------"+"\n");
		tableUpdateFromFile();
		setVisible(true);
	}
	//Main that starts the program
	public static void main(String[] args) {
		new FinalProjectTfrater();
	}
}