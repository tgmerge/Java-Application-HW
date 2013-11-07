import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *  字典文件格式：
 *  key1|key2|key3
 *  answer1
 *  key2|key3|key4
 *  answer2
 *  对于相同问题的随机回答，
 *  最希望出现的回答可以放在最前面。
 *  越靠后的回答越不容易出现。
 */

/**
 * 问答字典类
 * @author tgmerge
 * 从文件中读取问答字典；
 * 处理传入的问句字符串并回答
 */
public class Dictionary {

	// 问题关键字和答句
	ArrayList<ArrayList<String>> questionKey;
	ArrayList<String> answer;
	int itemNum;
	
	/**
	 * 构造函数，可抛出异常
	 * @param filename 字典文件名 
	 */
	public Dictionary(String filename){
		questionKey = new ArrayList<ArrayList<String>>();
		answer = new ArrayList<String>();
		itemNum = 0;
		try {
			importFile(filename);
		} catch (IOException e) {
			System.out.println("[E]IO");
		}
	}
	
	/**
	 * 从字典文件导入数据
	 * @param filename
	 * @throws IOException 
	 */
	private void importFile(String filename) throws IOException {
		// 读文件
		FileReader reader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String keywordLine;
		String answerLine;
		// 读两个字符串,打散并加入字典
		while((keywordLine = bufferedReader.readLine()) != null) {
			answerLine = bufferedReader.readLine();
			
			// 很难看的写法……
			String[] keyList;
			ArrayList<String> keyArrayList;
			keyList = keywordLine.split("\\|");
			keyArrayList = new ArrayList<String>(Arrays.asList(keyList));
			
			// 添加关键词列表和回答
			questionKey.add(keyArrayList);
			answer.add(answerLine);
			itemNum ++;
		}
		bufferedReader.close();
		return;
	}
	
	/**
	 * 回答传入的问题。
	 * @param question 问句字符串
	 * @return String 字符串，答句
	 */
	public String ask(String question) {
		ArrayList<String> keyArrayList;
		String finAnswer;
		
		keyArrayList = getQuestionKeyword(question);
		finAnswer = getAnswer(keyArrayList);
		
		return finAnswer;
	}
	
	/**
	 * 打散问句字符串并修整（大小写，符号等）
	 * @param question
	 * @return ArrayList<String> 关键字列表
	 */
	private ArrayList<String> getQuestionKeyword(String question) {

		// 统一小写
		question = question.toLowerCase();
		
		// 打散字符串
		String[] keyList;
		ArrayList<String> keyArrayList;
		keyList = question.split("\\||\\.| |,|\\?|\\!|\\-");
		keyArrayList = new ArrayList<String>(Arrays.asList(keyList));
		
		return keyArrayList;
	}
	
	/**
	 * 从数据中寻找适合的答句
	 * @param keyList 关键字列表
	 * @return String 字符串，答句
	 */
	private String getAnswer(ArrayList<String> keyList) {
		Random rand = new Random();
		
		// 简单地计算匹配度，已知最大值为maxMatch
		double maxMatch = 0;
		int maxMatchItem = 0;
		
		for(int i = 0; i < itemNum; i ++) {
			
			double match = 0;
			ArrayList<String> kList = questionKey.get(i);
			
			// 简单地计算匹配程度match
			for(int j = 0; j < keyList.size(); j ++) {
				for(int k = 0; k < kList.size(); k ++ ) {
					if(keyList.get(j).equals(kList.get(k))) {
						match += 1.0 / kList.size();
					}
				}
			}
			
			// 记录最高匹配
			if(match > maxMatch) {
				maxMatch = match;
				maxMatchItem = i;
			} else if (match == maxMatch) {
				// 简单地随机0,1,2, 1/3概率替代
				if( rand.nextInt(2) == 0 ) {
					maxMatchItem = i;
				}
			}
		}
		
		if(maxMatch > 0.5) {
			return answer.get(maxMatchItem);
		} else {
			return "Sorry, I can't understand that.";
		}
	}
	
	/**
	 * 调试用，输出字典
	 */
	public void printDict() {
		for(int i = 0; i < itemNum; i ++) {
			System.out.print("[" + i + "]");
			for(int j = 0; j < questionKey.get(i).size(); j ++) {
				System.out.print(questionKey.get(i).get(j));
				System.out.print(",");
			}
			System.out.println();
			System.out.println("[A]" + answer.get(i));
			System.out.println();
		}
	}
}
