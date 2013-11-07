import java.util.Iterator;

/**
 * 解析和处理log文件
 * @author tgmerge
 *
 */
public class Log {
	/**
	 * 构造,打开和导入log文件数据
	 * @param fileName
	 */
	public Log(final String fileName) {
		readLog(fileName);
	}
	
	/**
	 * 打开和导入log文件数据
	 * @param  fileName 文件名
	 * @return 成功与否
	 */
	private boolean readLog(final String fileName) {
		parseLog("");
		return false;
	}
	
	/**
	 * 导入一条log文件数据
	 * @param  str 一行文本
	 * @return 成功与否
	 */
	private boolean parseLog(final String str) {
		return false;
	}
	
	/**
	 * 统计访问最多的目标
	 * @param  length 最多返回条目数
	 * @return ArrayList<String>,统计数据
	 */
	private Stat statTarget(final int length) {
		Stat result = new Stat();
		return result;
	}

	/**
	 * 统计来访数最多的来源ip
	 * @param  length 最多返回条目数
	 * @return ArrayList<String>,统计数据
	 */
	private Stat statError(final int length) {
		Stat result = new Stat();
		return result;
	}
	
	/**
	 * 统计数据量
	 * @return ArrayList<String>,[0]是数据量字符串
	 */
	private Stat statSize() {
		Stat result = new Stat();
		return result;
	}

	/**
	 * 统计时间段内访问情况
	 * @param  type 可以为"day" "week" "month"
	 * @return ArrayList<String>,[i]存放了时间段[i]内的访问数量
	 */
	private Stat statBusy(final String type) {
		Stat result = new Stat();
		return result;
	}
	
	/**
	 * 测试方法
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
	 * 测试输出
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
