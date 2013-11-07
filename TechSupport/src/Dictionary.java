import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *  �ֵ��ļ���ʽ��
 *  key1|key2|key3
 *  answer1
 *  key2|key3|key4
 *  answer2
 *  ������ͬ���������ش�
 *  ��ϣ�����ֵĻش���Է�����ǰ�档
 *  Խ����Ļش�Խ�����׳��֡�
 */

/**
 * �ʴ��ֵ���
 * @author tgmerge
 * ���ļ��ж�ȡ�ʴ��ֵ䣻
 * ��������ʾ��ַ������ش�
 */
public class Dictionary {

	// ����ؼ��ֺʹ��
	ArrayList<ArrayList<String>> questionKey;
	ArrayList<String> answer;
	int itemNum;
	
	/**
	 * ���캯�������׳��쳣
	 * @param filename �ֵ��ļ��� 
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
	 * ���ֵ��ļ���������
	 * @param filename
	 * @throws IOException 
	 */
	private void importFile(String filename) throws IOException {
		// ���ļ�
		FileReader reader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String keywordLine;
		String answerLine;
		// �������ַ���,��ɢ�������ֵ�
		while((keywordLine = bufferedReader.readLine()) != null) {
			answerLine = bufferedReader.readLine();
			
			// ���ѿ���д������
			String[] keyList;
			ArrayList<String> keyArrayList;
			keyList = keywordLine.split("\\|");
			keyArrayList = new ArrayList<String>(Arrays.asList(keyList));
			
			// ��ӹؼ����б�ͻش�
			questionKey.add(keyArrayList);
			answer.add(answerLine);
			itemNum ++;
		}
		bufferedReader.close();
		return;
	}
	
	/**
	 * �ش�������⡣
	 * @param question �ʾ��ַ���
	 * @return String �ַ��������
	 */
	public String ask(String question) {
		ArrayList<String> keyArrayList;
		String finAnswer;
		
		keyArrayList = getQuestionKeyword(question);
		finAnswer = getAnswer(keyArrayList);
		
		return finAnswer;
	}
	
	/**
	 * ��ɢ�ʾ��ַ�������������Сд�����ŵȣ�
	 * @param question
	 * @return ArrayList<String> �ؼ����б�
	 */
	private ArrayList<String> getQuestionKeyword(String question) {

		// ͳһСд
		question = question.toLowerCase();
		
		// ��ɢ�ַ���
		String[] keyList;
		ArrayList<String> keyArrayList;
		keyList = question.split("\\||\\.| |,|\\?|\\!|\\-");
		keyArrayList = new ArrayList<String>(Arrays.asList(keyList));
		
		return keyArrayList;
	}
	
	/**
	 * ��������Ѱ���ʺϵĴ��
	 * @param keyList �ؼ����б�
	 * @return String �ַ��������
	 */
	private String getAnswer(ArrayList<String> keyList) {
		Random rand = new Random();
		
		// �򵥵ؼ���ƥ��ȣ���֪���ֵΪmaxMatch
		double maxMatch = 0;
		int maxMatchItem = 0;
		
		for(int i = 0; i < itemNum; i ++) {
			
			double match = 0;
			ArrayList<String> kList = questionKey.get(i);
			
			// �򵥵ؼ���ƥ��̶�match
			for(int j = 0; j < keyList.size(); j ++) {
				for(int k = 0; k < kList.size(); k ++ ) {
					if(keyList.get(j).equals(kList.get(k))) {
						match += 1.0 / kList.size();
					}
				}
			}
			
			// ��¼���ƥ��
			if(match > maxMatch) {
				maxMatch = match;
				maxMatchItem = i;
			} else if (match == maxMatch) {
				// �򵥵����0,1,2, 1/3�������
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
	 * �����ã�����ֵ�
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
