/**
 * csv��
 * @author tgmerge
 */
public class CsvItem {
	String student;
	String course;
	int    score;
	
	/**
	 * ���캯��
	 * @param s ѧ������
	 * @param c �γ���
	 * @param n ����
	 */
	public CsvItem(String s, String c, int n) {
		student = s;
		course  = c;
		score   = n;
	}
	
	public String getStudent() {
		return student;
	}
	
	public String getCourse() {
		return course;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int s) {
		score = s;
	}
}