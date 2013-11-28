import java.util.ArrayList;

/**
 * 统计结果的包装类
 * @author tgmerge
 * 以ArrayList<String>的形式存储了统计的结果
 * 可用add()方法添加项目，
 * 可用print()方法全部输出
 */
public class Stat {
	ArrayList<String> data;
	
	/**
	 * 构造，指定最长结果条数
	 * @param count
	 */
	public Stat(int count) {
		data = new ArrayList<String>(count);
	}
	
	/**
	 * 输出统计结果
	 */
	public void print() {
		for(int i = 0; i < data.size(); i ++)
			System.out.println(data.get(i));
	}
	
	/**
	 * 添加一条统计结果
	 * @param str
	 */
	public void add(final String str) {
		data.add(str);
	}
}
