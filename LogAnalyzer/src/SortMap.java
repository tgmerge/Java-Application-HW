import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ��HashMap�����õ�
 * �ο���StackOverflow�Ĵ���ʵ��
 */
public class SortMap {
	/**
	 * ��HashMap��Value���򣬰�˳��ȡһ���������Value��Key
	 * @param map Ҫ�����HashMap
	 * @param len ȡ����len��
	 * @return ���len����key���б�
	 */
	List<String> sortByValue(HashMap<String, Integer> map, int len) {
		int size = map.size();
		ArrayList<Entry<String, Integer>> list = new ArrayList <Entry<String, Integer>>(size);
		list.addAll(map.entrySet());
		ValueComparator vc = new ValueComparator();
		Collections.sort(list, vc);
		final List<String> keys = new ArrayList<String>(len);
		for (int i = 0; i < len; i++) {
			keys.add(i, list.get(i).getKey());
		}
		return keys;
	}
	
	/**
	 * �ӿ�
	 * @author tgmerge
	 *
	 */
    private class ValueComparator implements Comparator<Map.Entry<String, Integer>>  
    {  
        public int compare(Map.Entry<String, Integer> mp1, Map.Entry<String, Integer> mp2)   
        {
            return mp2.getValue() - mp1.getValue();  
        }
    }
}