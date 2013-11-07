import java.util.Scanner;

/**
 * HW02 - TechSupport
 * 
 * @author tgmerge
 *
 */
public class TechSupport {
	
	public static void main(String[] args) {
		Dictionary dict = new Dictionary("dict.txt");
		
		//dict.printDict();
		
		// ≤‚ ‘
		test(dict, "What the fox say?");
		test(dict, "What the fox say?");
		test(dict, "How can I power off that machine");
		test(dict, "I have an error with the product.");
		test(dict, "Tell me what a longcat is.");
		test(dict, "Dare you follow that sign?");
		System.out.println("-----");

		Scanner input = new Scanner(System.in);
		String question;

		// Ωªª•≤‚ ‘
		while((question = input.nextLine()).equals("") == false) {
			test(dict, question);
		}
		
		input.close();
	}
	
	/**
	 * ≤‚ ‘Dictionary¿‡
	 * @param dict
	 * @param question
	 */
	public static void test(Dictionary dict, String question) {
		System.out.println("[Q]"+question);
		System.out.println("[A]"+dict.ask(question));
		System.out.println();
	}
}
