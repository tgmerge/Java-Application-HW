import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @brief  解析和处理log文件
 * @author tgmerge
 */
public class Log {
	ArrayList<Record> data;

	/**
	 * 构造,打开和导入log文件数据
	 * @param fileName log文件名
	 */
	public Log(final String fileName) {
		data = new ArrayList<Record>();
		readLog(fileName);
	}
	
	/**
	 * 返回已经处理的记录条数
	 * @return int 记录条数
	 */
	public int dataCount() {
		return data.size();
	}
	
	/**
	 * 打开和导入log文件数据
	 * @param  fileName log文件名
	 */
	private void readLog(final String fileName) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = file.readLine()) != null) {
				data.add(parseLog(line));
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * 导入一条log文件数据
	 * @param  str 一行记录文本
	 * @return Record 一条记录数据的对象
	 * @throws Exception 匹配失败时抛出
	 */
	static private Record parseLog(final String str) throws Exception {
		Pattern p = Pattern.compile("^(\\S+?) (\\S+?) (\\S+?) \\[(.+?)\\] \"(\\S+?) (.*) (.+?)\" (\\S+?) (\\S+?)$");
		//                             ip      id     user       time       method   url  http     stat    len
		Matcher m = p.matcher(str);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
		if(m.find()) {
			Record s = new Record();
			s.setIP(m.group(1));
			s.setTime(dateFormat.parse(m.group(4).replaceAll("\\p{Cntrl}", "")));
			s.setURL(m.group(6));
			s.setStatcode(Integer.parseInt(m.group(8)));
			try {
				s.setLength(Integer.parseInt(m.group(9)));
			} catch(NumberFormatException e) {
				s.setLength(0);
			}
			return s;
		}
		else {
			throw new Exception("Parse Error: "+str);
		}
	}
	
	/**
	 * 统计访问最多的目标URL
	 * @param  length 最多返回条目数
	 * @return Stat 统计结果对象
	 */
	public Stat statTarget(final int length) {
		HashMap<String, Integer> visits = new HashMap<String, Integer>();
		Iterator<Record> recIter = data.iterator();
		
		String url;
		while(recIter.hasNext()) {
			url = recIter.next().getURL();
			visits.put(url, (visits.get(url) == null) ? 1 : visits.get(url)+1);
		}
		
		List<String> keys = new SortMap().sortByValue(visits, length);
		
		Stat result = new Stat(length);
		for(int i = 0; i < length; i ++) {
			url = keys.get(i);
			result.add("[" + visits.get(url) + "]" + url);
		}
		
		return result;
	}

	/**
	 * 统计来访数最多的来源ip
	 * @param  length 最多返回条目数
	 * @return Stat 统计结果对象
	 */
	public Stat statIP(final int length) {
		HashMap<String, Integer> visits = new HashMap<String, Integer>();
		Iterator<Record> recIter = data.iterator();
		
		String ip;
		while(recIter.hasNext()) {
			ip = recIter.next().getIP();
			visits.put(ip, (visits.get(ip) == null) ? 1 : visits.get(ip)+1);
		}
		
		List<String> keys = new SortMap().sortByValue(visits, length);
		
		Stat result = new Stat(length);
		for(int i = 0; i < length; i ++) {
			ip = keys.get(i);
			result.add("[" + visits.get(ip) + "]" + ip);
		}
		
		return result;
	}
	
	/**
	 * 统计被访问最多的不存在URL(返回HTTP 404的请求)
	 * @param  length 最多返回条目数, 如果是0则全部显示
	 * @return Stat 统计结果对象
	 */
	public Stat statBroken(int length) {
		HashMap<String, Integer> broken = new HashMap<String, Integer>();
		Iterator<Record> recIter = data.iterator();
		
		Record r;
		String url;
		while(recIter.hasNext()) {
			r = recIter.next();
			if(r.getStatcode() == 404) {
				url = r.getURL();
				broken.put(url, (broken.get(url) == null) ? 1 : broken.get(url)+1);
			}
		}
		
		if(length == 0) {
			length = broken.size();
		}
		List<String> keys = new SortMap().sortByValue(broken, length);
		
		Stat result = new Stat(length);
		for(int i = 0; i < length; i ++) {
			url = keys.get(i);
			result.add("Broken[" + broken.get(url) + "]" + url);
		}
		
		return result;
	}
	
	/**
	 * 统计总数据量
	 * @return Stat 统计结果
	 */
	public Stat statSize() {
		int totalSize = 0;
		Iterator<Record> recIter = data.iterator();
		while(recIter.hasNext()) {
			totalSize += recIter.next().getLength();
		}
		Stat result = new Stat(1);
		result.add("Total Size: " + totalSize + " bytes.");
		
		return result;
	}

	/**
	 * 统计时间段内访问情况
	 * @param  type 可以为"day" "week" "month"
	 * @return Stat 统计结果，长度随输入不同
	 * @throws Exception 传入type错误时抛出
	 */
	public Stat statBusy(final String type) throws Exception {
		
		if(!type.equals("day") && !type.equals("week") && !type.equals("month")) {
			throw new Exception("invalid type for statBusy");
		}
		
		int typeInt = type.equals("day") ? 1 : ( type.equals("week") ? 2 : 3 );
		
		int[] timeTable;
		if(typeInt == 1) {
			timeTable = new int[24];
		} else if(typeInt == 2) {
			timeTable = new int[8];
		} else {
			timeTable = new int[32];
		}
		
		Iterator<Record> recIter = data.iterator();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("PRC"));
		while(recIter.hasNext()) {
			cal.setTime(recIter.next().getTime());
			if(typeInt == 1) {
				timeTable[cal.get(Calendar.HOUR) + cal.get(Calendar.AM_PM)*12] ++;
			} else if (typeInt == 2) {
				timeTable[cal.get(Calendar.DAY_OF_WEEK)] ++;
			} else {
				timeTable[cal.get(Calendar.DAY_OF_MONTH)] ++;
			}
		}
		
		int resultSize = type.equals("day") ? 24 : ( type.equals("week") ? 8 : 32 );
		Stat result = new Stat(resultSize);
		int i = type.equals("day") ? 0 : ( type.equals("week") ? 1 : 1 );
		for(; i < resultSize; i ++) {
			result.add("[" + i + "]" + timeTable[i] + " visits.");
		}
		
		return result;
	}
	
}