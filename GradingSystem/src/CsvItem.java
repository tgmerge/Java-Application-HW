/**
 * csv项
 * @author tgmerge
 */
public class CsvItem {
	String student;
	String course;
	int    score;
	
	/**
	 * 构造函数
	 * @param s 学生姓名
	 * @param c 课程名
	 * @param n 分数
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