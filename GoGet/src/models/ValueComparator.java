package models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
/*public class ValueComparator implements Comparator {
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	@SuppressWarnings({ "unchecked" })
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
}*/
/*public class ValueComparator<String, Double extends Comparable<Double>> implements Comparator<String>{
	 
	Map<String, Double> map = new HashMap<String, Double>();
 
	public ValueComparator(Map<String, Double> map){
		this.map.putAll(map);
	}
 
	@Override
	public int compare(String s1, String s2) {
		return -map.get(s1).compareTo(map.get(s2));//descending order	
	}
}*/


public class ValueComparator implements Comparator<String>{
	 
	Map<String, Integer> map = new HashMap<String, Integer>();
 
	public ValueComparator(Map<String, Integer> map){
		this.map.putAll(map);
	}
 
	@Override
	public int compare(String s1, String s2) {
		if(map.get(s1) >= map.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
}