package labs;

import java.util.ArrayList;
import java.util.List;

public class lab5TfraterAllocationTest {

	public static List<List<Integer>> distributeNumbers() {
	    List<List<Integer>> distributedLists = new ArrayList<>();
	
	    for (int i = 0; i < 4; i++) {
	        distributedLists.add(new ArrayList<>());
	    }
	
	    for (int i = 0; i <= 100; i++) {
	        int listIndex = i % 4; // Determine which list to add the number
	        distributedLists.get(listIndex).add(i);
	    }
	
	    return distributedLists;
	}
	
	public static void main(String[] args) {
	    List<List<Integer>> distributedLists = distributeNumbers();
	
	    for (int i = 0; i < 4; i++) {
	        System.out.println("List " + (i + 1) + ": " + distributedLists.get(i));
	    }
	}
}