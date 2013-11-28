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
 * @brief  �����ʹ���log�ļ�
 * @author tgmerge
 */
public class Log {
	ArrayList<Record> data;

	/**
	 * ����,�򿪺͵���log�ļ�����
	 * @param fileName log�ļ���
	 */
	public Log(final String fileName) {
		data = new ArrayList<Record>();
		readLog(fileName);
	}
	
	/**
	 * �����Ѿ�����ļ�¼����
	 * @return int ��¼����
	 */
	public int dataCount() {
		return data.size();
	}
	
	/**
	 * �򿪺͵���log�ļ�����
	 * @param  fileName log�ļ���
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
	 * ����һ��log�ļ�����
	 * @param  str һ�м�¼�ı�
	 * @return Record һ����¼���ݵĶ���
	 * @throws Exception ƥ��ʧ��ʱ�׳�
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
	 * ͳ�Ʒ�������Ŀ��URL
	 * @param  length ��෵����Ŀ��
	 * @return Stat ͳ�ƽ������
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
	 * ͳ��������������Դip
	 * @param  length ��෵����Ŀ��
	 * @return Stat ͳ�ƽ������
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
	 * ͳ�Ʊ��������Ĳ�����URL(����HTTP 404������)
	 * @param  length ��෵����Ŀ��, �����0��ȫ����ʾ
	 * @return Stat ͳ�ƽ������
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
	 * ͳ����������
	 * @return Stat ͳ�ƽ��
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
	 * ͳ��ʱ����ڷ������
	 * @param  type ����Ϊ"day" "week" "month"
	 * @return Stat ͳ�ƽ�������������벻ͬ
	 * @throws Exception ����type����ʱ�׳�
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