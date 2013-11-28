import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 对HashMap排序用的
 * 参考了StackOverflow的代码实现
 */
public class SortMap {
	/**
	 * 将HashMap按Value排序，按顺序取一定数量最大Value的Key
	 * @param map 要排序的HashMap
	 * @param len 取最大的len个
	 * @return 最大len个的key的列表
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
	 * 接口
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