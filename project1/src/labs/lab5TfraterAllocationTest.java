package labs;

public class lab5TfraterAllocationTest {
	public static void main(String[] args) {
		int num = 100;
		for(int x = 1; x <= num; x++) {
			if(x/4 == 0) {
				System.out.println("Modulus Division 0\n"+x);
			}
			else if(x/4 == 1) {
				System.out.println("Modulus Division 1\n"+x);
			}
		}
	}
}