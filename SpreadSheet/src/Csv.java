import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CsvParser
 * @author tgmerge
 * 
 */
public class Csv {
	
	/**
	 * 将str字符串转换成csv字符串
	 */
	static public String strToCsv(String str) {
		
		String result = str;
		
		if (result == null) {
			result = "";
		}
		else if (result.matches(".*[ ,\"].*")){
			result = result.replaceAll("\"", "\"\"");
			result = '"' + result + '"';
		}
		return result;
	}
	
	
	/**
	 * 将csv字符串转换成str字符串
	 */
	static public String csvToStr(String csv) {
		
		String result = csv;
		if (result.matches("^.*,$")) {
			result = result.substring(0, result.length()-1);
		}
		if (result.matches("^\".*\"$")) {
			result = result.substring(1, result.length()-1);
			result = result.replaceAll("\"\"", "\"");
		}
		return result;
	}
	
	
	/**
	 * 获取匹配csv单元的正则表达式字符串
	 */
	static private String getRegExp() {
		
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
	 * 读csv，产生String[][]
	 */
	static public String[][] importFile(String fileName) {
		
		// 匹配csv一个数据单元的正则表达式
		Pattern regex = Pattern.compile(getRegExp());
		Matcher matcher;
		
		// 读文件
		BufferedReader file;
		try {
			file = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("[importFile]Error: file not found:" + fileName);
			return null;
		}
		
		String line;
		String cell;
		
		ArrayList<String> lineData;
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		try {
			while ((line = file.readLine()) != null) {
				matcher  = regex.matcher(line);
				lineData = new ArrayList<String>();
				
				while (matcher.find() && matcher.group(0).length() > 0) {
					cell = csvToStr(matcher.group(0));
					lineData.add(cell);
				}
				data.add(lineData);
			}
			file.close();
		} catch (IOException e) {
			System.out.println("[importFile]Error: I/O error.");
			return null;
		}
		
		// 将ArrayList转换为二维数组
		String[][] result = new String[data.size()][];
		for (int i = 0; i < data.size(); i ++) {
			ArrayList<String> t = data.get(i);
			result[i] = new String[t.size()];
			t.toArray(result[i]);
		}
		
		return result;
	}
	
	
	/**
	 * 从String[][]写csv文件
	 */
	static public boolean exportFile(String fileName, String[][] data) {

		BufferedWriter file;
		try {
			file = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			System.out.println("[exportFile]Error: I/O error while writing " + fileName);
			return false;
		}
		
		try {
			for (int i = 0; i < data.length; i ++) {
				for (int j = 0; j < data[i].length; j ++) {
					if (j > 0)
						file.write(",");
					file.write(strToCsv(data[i][j]));
				}
				file.write("\r\n");
			}
			file.close();
		} catch (IOException e) {
			System.out.println("[exportFile]Error: I/O error while writing data to " + fileName);
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 根据String[][]生成表头String[]
	 */
	static public String[] getHeaders(String[][] data) {
		
		int maxLength = 0;
		for (int i = 0; i < data.length; i ++) {
			maxLength = (data[i].length > maxLength) ? data[i].length : maxLength;
		}
		String[] result = new String[maxLength];
		for (int i = 0; i < maxLength; i ++) {
			result[i] = Integer.toString(i);
		}
		return result;
	}
}
