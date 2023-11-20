package labs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class lab5Tfrater extends JFrame {
	private static final long serialVersionUID = 3794059922116115530L;
	//Used to store the number of threads and number of partitions to split the search into
	private JTextField allocNum = new JTextField(10);
	//Used to store the length of the users search, example: 1:userChoice
	private JTextField userChoice = new JTextField(10);
	//Text area where results and instructions are appended
	private JTextArea outputArea = new JTextArea(20,20);
	//Boolean used to show the warning message in the main JFrame only once
	private boolean onlyWarning = false;
	//Thread safe and visible list where the results of prime search are stored
	private static List<Integer> resultList = Collections.synchronizedList(new ArrayList<Integer>());
	
	//Method that creates a number of lists based on allocNum then equally distributes the numbers 1:userNum into those lists
	private static List<List<Integer>> distributeNumbers(int allocNum, int userNum) {
	    List<List<Integer>> distributedLists = new ArrayList<>();
	
	    for (int i = 0; i < allocNum; i++) {
	        distributedLists.add(new ArrayList<>());
	    }
	
	    for (int i = 1; i <= userNum; i++) {
	        int listIndex = i % allocNum; // Determine which list to add the number
	        distributedLists.get(listIndex).add(i);
	    }
	
	    return distributedLists;
	}
	//boolean that checks if a number is a prime number (only divisible by 1 and itself)
	private static boolean isPrime(int x) {
		//1 and 0 are automatically not prime numbers
		if (x == 1 || x == 0)
			return false;
		//Loops through all the numbers between 2 and the input and checks if the input is divisible by any of them
		for (int i = 2; i < x; i++) {
			if (x % i == 0)
				return false;
		}
		return true;
	}
	
	//Worker used to assign semaphores to check if each number in a list is prime then add that number to resultsList
	//A countDownLatch is also implemented to keep track of when the work is done
	private static class Worker implements Runnable {
		private final List<Integer> list;
		private final Semaphore semaphore;
		private final CountDownLatch cdl;
		
		public Worker(List<Integer> list, Semaphore semaphore, CountDownLatch cdl) {
			this.list = list;
			this.semaphore = semaphore;
			this.cdl = cdl;
		}
		
		@Override
		public void run() {
			try {
				semaphore.acquire();
				for (int i = 0; i < list.size(); i++) {
					if (isPrime(list.get(i)))
						resultList.add(list.get(i));
				}
				cdl.countDown();
			} catch (Exception e) {
				System.out.println("Worker had an error");
				e.printStackTrace();
			}
		}
	}
	
	//Comparator used to sort the resultList in ascending order
	private static class AscendSort implements Comparator<Integer> {
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	}
	
	//Custom dialog that takes the user inputs, assigns the workers then outputs the results to the main text area
	private class MyDialog extends JDialog {
		private static final long serialVersionUID = 3794059922116115530L;
		public MyDialog(JFrame parent) {
			super(parent, "User Inputs", true);
			JButton submitButton = new JButton("Begin");
			JButton cancelButton = new JButton("Cancel");
			JPanel quesPanel = new JPanel(new GridLayout(2,2,10,2));
			JPanel buttonPanel = new JPanel(new GridLayout(1,2));
			quesPanel.add(new JLabel("You will be searching from 1 to : "));
			quesPanel.add(userChoice);
			quesPanel.add(new JLabel("Number of threads you would like : "));
			quesPanel.add(allocNum);
			buttonPanel.add(submitButton);
			buttonPanel.add(cancelButton);
			getContentPane().setLayout(new BorderLayout(5,5));
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			getContentPane().add(quesPanel,BorderLayout.CENTER);
			getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			//Action listener that does the heavy lifting with the Workers and count down latch
			submitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int userInt = Integer.parseInt(userChoice.getText());
						int allocInt = Integer.parseInt(allocNum.getText());
						List<List<Integer>> distributedLists = distributeNumbers(allocInt,userInt);
					    Semaphore semaphore = new Semaphore(distributedLists.size());
					    CountDownLatch cdl = new CountDownLatch(distributedLists.size());
						//Tracks the start time
						long startTime = System.currentTimeMillis();
						//Assigns the workers to each of the created lists in distributedLists
					    for (int i = 0; i < distributedLists.size(); i++) {
					        Worker w = new Worker(distributedLists.get(i),semaphore,cdl);
					        new Thread(w).start();
					    }
					    
					    //The count down latch that waits for the semaphores to finish
					    try {
							cdl.await();
						} catch (InterruptedException ex) {
							outputArea.append("Awaiting countdownlatch had an error");
							ex.printStackTrace();
						}
					    //Tracks the end time
					    long endTime = System.currentTimeMillis();
					    //Sorts resultList using the AscendSort comparator
					    Collections.sort(resultList, new AscendSort());
					    outputArea.append("Numbers searched: 1-"+userInt+"\n"+"Threads used: "+allocInt+"\n"+"Time taken: "+Float.toString((endTime-startTime)/1000f) + "seconds\n");
					    outputArea.append("Prime numbers found: "+ resultList.size()+"\n");
					    outputArea.append(resultList.toString() + "\n\n");
					    //resets the necessary inputs and variables then closes the custom dialog box
					    resultList.clear();
					    allocNum.setText(null);
					    userChoice.setText(null);
					    dispose();
					} catch (Exception excep) {
						JOptionPane.showMessageDialog(null, "Please only enter numbers for your choices", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
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
			getRootPane().setDefaultButton(submitButton);
			setSize(500,200);
		}
	}
	//Handles the main JFrame for the prime number search
	public lab5Tfrater() {
		super("Prime Number Finder");
		JButton submitButton = new JButton("Run");
		JButton cancelButton = new JButton("Cancel");
		setLocationRelativeTo(null);
		setSize(650,300);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(outputArea),BorderLayout.CENTER);
		outputArea.setEditable(false);
		getContentPane().add(submitButton,BorderLayout.SOUTH);
		//Action listener that displays a warning message only once to the user then starts the custom dialog box
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(onlyWarning == false) {
						JOptionPane.showMessageDialog(null, "Hey there chief, I just wanted to remind you to pay attention to which number is for threads and which is the prime you want to search\nYou could crash your setup by requesting way too many threads, I would put in a way to stop you but I didn't get that far\nThis is the only time you'll see this message.", "Be Careful Requesting Threads!", JOptionPane.WARNING_MESSAGE);
						onlyWarning = true;
					}					
					MyDialog dlg = new MyDialog(null);
					dlg.setVisible(true);
				}
				catch (Exception excep) {
					JOptionPane.showMessageDialog(null, "Please enter a number", "Incorrect Data Type", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		getContentPane().add(cancelButton,BorderLayout.EAST);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		getRootPane().setDefaultButton(submitButton);
		//window listener that closes the program if the JFrame is closed with the x button
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//The initial instructions for the Prime Number Finder
		outputArea.append("Welcome to the Prime Number Finder!\n");
		outputArea.append("This porgram searches for all the prime numbers between 1 and your chosen number\n");
		outputArea.append("you will also be asked how many threads you would like to spawn for this task\n");
		outputArea.append("Press Enter or hit run to begin\n\n\n");
		setVisible(true);
	}
	//Main that starts the program
	public static void main(String[] args) {
		new lab5Tfrater();
	}
}