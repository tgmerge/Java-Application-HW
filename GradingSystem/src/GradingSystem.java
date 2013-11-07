import java.util.Scanner;

/**
 * 可接受所有指令，由于要求不同的关系暂不使用
 * @author tgmerge
 *
 */
class GradingSystem {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String command;
		
		System.out.print("Name of .csv file: ");
		command = scanner.nextLine();
		
		GradingDb db = new GradingDb(command);
		while(!(command = scanner.nextLine()).equals("")) {
			db.query(command);
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