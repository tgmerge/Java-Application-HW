import java.util.ArrayList;

/**
 * ͳ�ƽ���İ�װ��
 * @author tgmerge
 * ��ArrayList<String>����ʽ�洢��ͳ�ƵĽ��
 * ����add()���������Ŀ��
 * ����print()����ȫ�����
 */
public class Stat {
	ArrayList<String> data;
	
	/**
	 * ���죬ָ����������
	 * @param count
	 */
	public Stat(int count) {
		data = new ArrayList<String>(count);
	}
	
	/**
	 * ���ͳ�ƽ��
	 */
	public void print() {
		for(int i = 0; i < data.size(); i ++)
			System.out.println(data.get(i));
	}
	
	/**
	 * ���һ��ͳ�ƽ��
	 * @param str
	 */
	public void add(final String str) {
		data.add(str);
	}
}
