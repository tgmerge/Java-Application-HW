import java.util.Iterator;

/**
 * �����ʹ���log�ļ�
 * @author tgmerge
 *
 */
public class Log {
	/**
	 * ����,�򿪺͵���log�ļ�����
	 * @param fileName
	 */
	public Log(final String fileName) {
		readLog(fileName);
	}
	
	/**
	 * �򿪺͵���log�ļ�����
	 * @param  fileName �ļ���
	 * @return �ɹ����
	 */
	private boolean readLog(final String fileName) {
		parseLog("");
		return false;
	}
	
	/**
	 * ����һ��log�ļ�����
	 * @param  str һ���ı�
	 * @return �ɹ����
	 */
	private boolean parseLog(final String str) {
		return false;
	}
	
	/**
	 * ͳ�Ʒ�������Ŀ��
	 * @param  length ��෵����Ŀ��
	 * @return ArrayList<String>,ͳ������
	 */
	private Stat statTarget(final int length) {
		Stat result = new Stat();
		return result;
	}

	/**
	 * ͳ��������������Դip
	 * @param  length ��෵����Ŀ��
	 * @return ArrayList<String>,ͳ������
	 */
	private Stat statError(final int length) {
		Stat result = new Stat();
		return result;
	}
	
	/**
	 * ͳ��������
	 * @return ArrayList<String>,[0]���������ַ���
	 */
	private Stat statSize() {
		Stat result = new Stat();
		return result;
	}

	/**
	 * ͳ��ʱ����ڷ������
	 * @param  type ����Ϊ"day" "week" "month"
	 * @return ArrayList<String>,[i]�����ʱ���[i]�ڵķ�������
	 */
	private Stat statBusy(final String type) {
		Stat result = new Stat();
		return result;
	}
	
	/**
	 * ���Է���
	 */
	public void _test() {
		int length = 10;
		_testPrint(statTarget(length), "statTarget");
		_testPrint(statError(length), "statError");
		_testPrint(statSize(), "statSize");
		_testPrint(statBusy("day"), "statBusy-day");
		_testPrint(statBusy("week"), "statBusy-week");
		_testPrint(statBusy("month"), "statBusy-month");
		_testPrint(statBusy("error"), "statBusy-error-arg");
		return;
	}
	
	/**
	 * �������
	 * @param list
	 * @param info
	 */
	private void _testPrint(final Stat list, final String info) {
		Iterator i = list.iterator();
		System.out.println("-----" + info + "-----");
		while (i.hasNext()) {
			System.out.println(i.next());
		}
		System.out.println();
		return;
	}
	
}
