import java.util.Scanner;


public class QueryCourse {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String command;
		
		System.out.print("Name of .csv file(data.csv): ");
		command = scanner.nextLine();
		
		GradingDb db = new GradingDb(command);
		System.out.print(">>");
		while(!(command = scanner.nextLine()).equals("")) {
			String[] wordList = command.split(",");
			db.queryCourse(wordList[0]);
			System.out.print(">>");
		}
		
		boolean writeSuccess = false;
		while(!writeSuccess) {
			try {
				db.writeData();
				writeSuccess = true;
			}
			catch(Exception e) {
				System.out.println(e.getMessage() + " press Enter to retry.");
				scanner.nextLine();
			}
		}
		
		scanner.close();
		return;
	}
}
