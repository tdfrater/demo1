package labs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class lab5MethodTest {
	private static List<Integer> resultList = Collections.synchronizedList(new ArrayList<Integer>());

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
	
	private static boolean isPrime(int x) {
		if (x == 1 || x ==0)
			return false;
		for (int i = 2; i < x; i++) {
			if (x % i == 0)
				return false;
		}
		return true;
	}
	
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
	
	private static class AscendSort implements Comparator<Integer> {
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	}
	
	public static void main(String[] args) {
	    List<List<Integer>> distributedLists = distributeNumbers(4,100);
	    Semaphore semaphore = new Semaphore(distributedLists.size());
	    CountDownLatch cdl = new CountDownLatch(distributedLists.size());
	    for (int i = 0; i < distributedLists.size(); i++) {
	        Worker w = new Worker(distributedLists.get(i),semaphore,cdl);
	        new Thread(w).start();
	    }
	    
	    try {
			cdl.await();
		} catch (InterruptedException e) {
			System.out.println("Awaiting countdownlatch had an error");
			e.printStackTrace();
		}
	    
	    Collections.sort(resultList, new AscendSort());
	    System.out.println("resultListSize: "+ resultList.size());
	    System.out.println(resultList);
	}
}