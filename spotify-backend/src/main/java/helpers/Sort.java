package helpers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Sort<K, V extends Comparable<? super V>> 
{
	    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) 
	    {
	        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	        
	        Comparator <V> comp = new Comparator <V> () 
	        {
	            @Override
	            public int compare(V first, V second) 
	            {
	               return second.compareTo(first);
	            };
	        };
	        
	        list.sort(Entry.comparingByValue(comp));

	        Map<K, V> result = new LinkedHashMap<>();
	        for (Entry<K, V> entry : list) {
	            result.put(entry.getKey(), entry.getValue());
	        }

	        return result;
	    }
}
