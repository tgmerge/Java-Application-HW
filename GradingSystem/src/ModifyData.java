import java.util.Scanner;


public class ModifyData {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String command;
		
		System.out.print("Name of .csv file(data.csv): ");
		command = scanner.nextLine();
		
		GradingDb db = new GradingDb(command);
		System.out.print(">>");
		while(!(command = scanner.nextLine()).equals("")) {
			String[] wordList = command.split(",");
			db.modifyData(wordList[0], wordList[1], Integer.parseInt(wordList[2]));
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
