import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理csv数据库类
 * @author tgmerge
 * 读csv
 * 从csv中读取数据
 * 写回csv
 */
public class GradingDb {
	
	// 存储从csv中读取的数据
	ArrayList<CsvItem> csv;
	String file;
	
	/**
	 * 从csv文件构造数据库类,若不存在则新建
	 * @param fileName csv文件名
	 */
	public GradingDb(String fileName) {
		csv = new ArrayList<CsvItem>();
		file = fileName;
		try {
			importCsv(fileName);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 获取匹配csv单元的正则表达式
	 * @return 表达式字符串
	 */
	private String getRegExp() {
		StringBuffer strRegExps = new StringBuffer();
		
		String SPECIAL_CHAR_A = "[^\",\\n 　]";
		String SPECIAL_CHAR_B = "[^\",\\n]";
		
		strRegExps.append("\"((");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*[,\\n 　])*(");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"{2})*)*");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"[ 　]*,[ 　]*");
		strRegExps.append("|");
		strRegExps.append(SPECIAL_CHAR_B);
		strRegExps.append("*[ 　]*,[ 　]*");
		strRegExps.append("|\"((");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*[,\\n 　])*(");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"{2})*)*");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"[ 　]*");
		strRegExps.append("|");
		strRegExps.append(SPECIAL_CHAR_B);
		strRegExps.append("*[ 　]*");
		return strRegExps.toString();
	}
	
	/**
	 * 从csv导入数据
	 * @param fileName csv文件名
	 * @throws IOException
	 */
	private void importCsv(String fileName) throws Exception {
		// 匹配csv一个数据单元的正则表达式
		Pattern regex = Pattern.compile(getRegExp());
		
		Matcher m;
		
		// 读文件
		FileReader reader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		
		// 临时字符串
		String student;
		String course;
		int mark;
		
		// 跳过行首
		line = bufferedReader.readLine();
		if (!line.equals("student,course,mark")) {
			bufferedReader.close();
			throw new Exception("[importCsv]first line error: "+line);
		}
		
		// 读之后的每一行
		while ((line = bufferedReader.readLine()) != null) {
			// 尝试匹配
			m = regex.matcher(line);
			if (m.find()) {
				student = csvToStr(m.group(0));
			} else {
				bufferedReader.close();
				throw new Exception("[importCsv]no valid value");
			}
			if (m.find()) {
				course = csvToStr(m.group(0));
			} else {
				bufferedReader.close();
				throw new Exception("[importCsv]no valid value");
			}
			if (m.find()) {
				mark = Integer.parseInt(m.group(0));
			} else {
				bufferedReader.close();
				throw new Exception("[importCsv]no valid value");
			}
			csv.add(new CsvItem(student, course, mark));
		}
		bufferedReader.close();
		System.out.println("csv imported.");
		return;
	}
	
	/**
	 * 将字符串(student, course)转换为csv单元
	 * @param str
	 * @return
	 */
	private String strToCsv(String str) {
		String result = str;
		
		if (result.matches(".*[ ,\"].*")){
			result = result.replaceAll("\"", "\"\"");
			result = '"' + result + '"';
		}
		result += ',';
		
		return result;
	}
	
	/**
	 * 将csv字符串(student, course)转换为str
	 * @param csv
	 */
	private String csvToStr(String csv) {
		String result = csv;
		
		if (result.matches("^\".*\",$")) {
			result = result.substring(1, result.length()-2);
			result = result.replaceAll("\"\"", "\"");
		}
		else {
			result = result.substring(0, result.length()-1);
		}
		
		return result;
	}
	
	/**
	 * 进行查询。
	 * @param command
	 */
	public void query(String command) {
		String[] wordList = command.split(",");
		
		if( wordList.length == 1 ) {
			boolean found = false;
			if (isStudent(wordList[0])) {
				found = true;
				queryStudent(wordList[0]);
			}
			if (isCourse(wordList[0])) {
				found = true;
				queryCourse(wordList[0]);
			}
			if (!found) {
				System.out.println(wordList[0] + " not found.");
			}
		}
		else if( wordList.length == 3 ) {
			modifyData(wordList[0], wordList[1], Integer.parseInt(wordList[2]));
		}
		else {
			System.out.println("Error in command.");
		}
	}
	
	/**
	 * 判断某名称是否是学生名
	 * @param name
	 * @return 是否是学生名
	 */
	private boolean isStudent(String name) {
		Iterator<CsvItem> i = csv.iterator();
		while (i.hasNext()) {
			if (i.next().getStudent().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断某名称是否是课程名
	 * @param name
	 * @return 是否是课程名
	 */
	private boolean isCourse(String name) {
		Iterator<CsvItem> i = csv.iterator();
		while (i.hasNext()) {
			if (i.next().getCourse().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询学生信息
	 * @param name
	 */
	public void queryStudent(String name) {
		System.out.println("Info of student [" + name + "]:");
		System.out.println("----------");
		System.out.println("COURSE\tMARK");
		
		Iterator<CsvItem> i = csv.iterator();
		CsvItem item;
		while (i.hasNext()) {
			item = i.next();
			if (item.getStudent().equals(name)) {
				System.out.println(item.getCourse() + "\t" + item.getScore());
			}
		}
		
		System.out.println("==========");
		return;
	}
	
	/**
	 * 查询课程信息
	 * @param name
	 */
	public void queryCourse(String name) {
		System.out.println("Info of course [" + name + "]:");
		System.out.println("----------");
		System.out.println("STUDENT\tMARK");
		
		Iterator<CsvItem> i = csv.iterator();
		CsvItem item;
		int count = 0;
		double total = 0.;
		while (i.hasNext()) {
			item = i.next();
			if (item.getCourse().equals(name)) {
				System.out.println(item.getStudent() + "\t" + item.getScore());
				count ++;
				total += item.getScore();
			}
		}
		
		System.out.println("----------");
		System.out.println("COUNT\t" + count);
		System.out.println("AVG\t" + total / count);
		System.out.println("==========");
		return;
	}
	
	/**
	 * 修改数据
	 * @param student
	 * @param course
	 * @param mark
	 */
	public void modifyData(String student, String course, int mark) {
		Iterator<CsvItem> i = csv.iterator();
		CsvItem item;
		// 查找现有记录
		while (i.hasNext()) {
			item = i.next();
			if (item.getStudent().equals(student) && item.getCourse().equals(course)) {
				item.setScore(mark);
				System.out.println("Modified student [" + student + "]'s mark of course [" + course + "] to " + mark);
				return;
			}
		}
		
		// 找不到则新增记录
		item = new CsvItem(student, course, mark);
		csv.add(item);
		System.out.println("Added student [" + student + "]'s mark of course [" + course + "] to " + mark);
		return;
	}

	/**
	 * 将数据写入到csv文件。
	 * @throws Exception
	 */
	public void writeData() throws Exception {
		// 清空原来文件
		FileWriter writer = new FileWriter(file);

		writer.write("student,course,mark\n");
		
		Iterator<CsvItem> i = csv.iterator();
		CsvItem item;
		String str;
		while(i.hasNext()) {
			item = i.next();
			str = strToCsv(item.getStudent()) + strToCsv(item.getCourse()) + item.getScore() + "\n";
			writer.write(str);
		}
		
		writer.close();
		
		System.out.println("[Write to " + file + " done]");
		
		return;
	}
	
}