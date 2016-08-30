package utilities;

import utilities.FileParserUtility;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

import models.*;
import models.Stopper;
public class GoGetterUtility {
	FileParserUtility fu = new FileParserUtility();
	
	
	public  Map<String, Double> getRankedResults(Map<String, Double> final_ranks ){
		Map<String,Double> ranked_results = new LinkedHashMap<String,Double>();
		Stream <Entry<String,Double>> stream = final_ranks.entrySet().stream();
	    stream.sorted(Comparator.comparing(r -> r.getValue())).forEachOrdered(r ->ranked_results.put(r.getKey(),r.getValue()));
		return ranked_results;
	}
	
	
	public Map<String,Double> getFinalRanks(Map<String,Double> similarities, Map<String,Double> proximities){
		Map<String,Double> final_ranks = new HashMap<String,Double>();
		for(Map.Entry<String, Double> entry : similarities.entrySet()){
			String sim_term = entry.getKey();
			Double sim_value = entry.getValue();
			for(Map.Entry<String, Double> e : proximities.entrySet()){
				String prox_term = e.getKey();
				Double prox_value = e.getValue();
				if(sim_term == prox_term){
					final_ranks.put(sim_term, (0.75*sim_value)+(0.25*prox_value));
				}
			}
			
		}
		return final_ranks;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  Map getSimilarities(Map queryResultVectors, Map queryvector){
		
		Map similarities = new HashMap();
		
		for(Map.Entry<String,Map> doc : ((Map<String,Map>)queryResultVectors).entrySet()){
			
			String fileName = doc.getKey();
			Map docvector = doc.getValue();
			Map sorted_docvector = new TreeMap(docvector);
			Map sorted_queryvector = new TreeMap(queryvector);
			double sum = 0;
			double doc_square_sum =0;
			double query_square_sum =0;
			double similarity = 0;
			for(Map.Entry<String,Double> que : ((Map<String,Double>)sorted_queryvector).entrySet()){
				String term = que.getKey();
				double query_weight = que.getValue();
				double doc_weight = (Double)sorted_docvector.get(term);
				sum += query_weight*doc_weight;
				doc_square_sum += doc_weight*doc_weight;
				query_square_sum += query_weight*query_weight;
			}
			similarity = sum/Math.sqrt(doc_square_sum*query_square_sum);
			//System.out.println(similarity);
			similarities.put(fileName, similarity);
		}
		return similarities;
	}

	@SuppressWarnings("rawtypes")
	public  Map<String,Double> getQueryVector(List processed_query_terms, Map<String, Map<String,List<Integer>>> inverted_index, FileParserModel fpm){
		Map<String,Double> queryvector = new HashMap<String,Double>();
		for(Map.Entry<String, Map<String,List<Integer>>> entry : ((Map<String, Map<String,List<Integer>>>) inverted_index).entrySet()){
			queryvector.put(entry.getKey(), 0.0);
		}
		Iterator i = processed_query_terms.iterator();
		while(i.hasNext()){
			String query_term = ((String) i.next()).trim();
			int query_tf = 0;
			Iterator j = processed_query_terms.iterator();
			if(inverted_index.get(query_term) != null){
			while(j.hasNext()){
				if (query_term == ((String) j.next()).trim()){
					query_tf++;
				}
			}
			int q_df = ((Map<String,List<Integer>>)inverted_index.get(query_term)).size();
			FileParserUtility fpu = new FileParserUtility();
			//List filelist = fpu.getFileList(fpm);
			//System.out.println(fpu.getFileList(fpm).size());
			//System.out.println(filelist.size()+"*************************"+q_df);
			double query_idf = Math.log(91/q_df)/Math.log(10);
			if(queryvector.containsKey(query_term)){
				System.out.println(query_term+"------"+query_idf+"-----"+query_tf);
				queryvector.put(query_term,query_tf*query_idf);	
			}
			}
		}
		return queryvector;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  Map getQueryResults(List processed_query_terms, Map docvectors, Map inverted_index){
		Map<String,Map<String,Double>> query_results = new HashMap<String,Map<String,Double>>();
		Iterator i = processed_query_terms.iterator();
		while(i.hasNext()){
			String query_term = (String) i.next();
			Map<String,List<Integer>> term_info = (Map<String,List<Integer>>) inverted_index.get(query_term);
			
			if (term_info != null){
				List filenames = new ArrayList();
				filenames.addAll(((Map<String,List<Integer>>) term_info).keySet());		
				Iterator itr = filenames.iterator();
				while (itr.hasNext()){
					String fname = (String) itr.next();
					Map<String,Double> docvector = (Map<String,Double>) docvectors.get(fname);
					query_results.put(fname, docvector);
				}
			}
		}
		return query_results;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Double> getTermProximities(Map<String,Map<String,List<Integer>>> inverted_index, Map<String, Map<String,Double>> doc_vectors,  List<String> processed_qlist){
		Map<String,Integer> qterm_idf = new HashMap<String,Integer>();
		Iterator qitr = processed_qlist.iterator();
		while(qitr.hasNext()){
			String qterm = (String)qitr.next();
			int df = ((Map<String,List<Integer>>) inverted_index.get(qterm)).size();
			int idf = (int)Math.abs((Math.log(91/df)/Math.log(10))*100000);
			qterm_idf.put(qterm, idf);
		}
		//Map<String,Double> sortedMap = new TreeMap(new ValueComparator(qterm_idf));
		/*Comparator<String> comparator = new ValueComparator(qterm_idf);
		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(comparator);*/
		TreeMap<String, Integer> sortedMap = sortMapByValue(qterm_idf);  
		//System.out.println(sortedMap);
	
 
	
		//sortedMap.putAll(qterm_idf);
		System.out.println(qterm_idf);
		System.out.println(sortedMap);
		System.out.println(sortedMap.keySet());
		Iterator<String> itr = sortedMap.keySet().iterator();
		List<String> imp_terms = new ArrayList<String>();
		imp_terms.add((String) itr.next());
		imp_terms.add((String) itr.next());
		System.out.println(imp_terms);
		Map<String,Double> proximity_map = new HashMap<String,Double>();
		for(Map.Entry<String, Map<String,Double>> entry : doc_vectors.entrySet()){
			proximity_map.put(entry.getKey(), 0.0);
			/*if(entry.getValue().containsKey(imp_terms.get(0)) 
					&&
			   entry.getValue().containsKey(imp_terms.get(1))){
				if(entry.getValue().get(imp_terms.get(0))>0
						&&
					entry.getValue().get(imp_terms.get(1))>0){
					System.out.println("contains all imp terms====="+entry.getKey());
					candidate_docs.add(entry.getKey());
			
				}
			}*/
			
		}
		List<String> first_files_list = new ArrayList<String>();
		first_files_list.addAll(inverted_index.get(imp_terms.get(0)).keySet());
		List<String> second_files_list = new ArrayList<String>();
		second_files_list.addAll(inverted_index.get(imp_terms.get(1)).keySet());
		List<String> intersection_list = new ArrayList<String>();
		intersection_list.addAll(intersection(first_files_list,second_files_list));
		
		System.out.println("#############################"+intersection_list.size());
		if(intersection_list.size()>0){
			for(int i =0; i<intersection_list.size();i++){
				System.out.println("inside for");
				double min_distance = 999999999;
				List<Integer> first_position_list =inverted_index.get(imp_terms.get(0)).get(intersection_list.get(i));
				List<Integer> second_position_list =inverted_index.get(imp_terms.get(1)).get(intersection_list.get(i));
				System.out.println(first_position_list+"^^^^^^^^^^^^^^^"+imp_terms.get(0));
				System.out.println(second_position_list+"^^^^^^^^^^^^^^^"+imp_terms.get(1));
				for(int j=0; j<first_position_list.size();j++){
					
					
					for(int k=0; k<second_position_list.size();k++){
						if(Math.abs(first_position_list.get(j)-second_position_list.get(k)) < min_distance){
							min_distance = Math.abs(first_position_list.get(j)-second_position_list.get(k));
						}
					}
				}
				System.out.println("+++++++++++++++++++++++++++++++"+min_distance);
				proximity_map.put(intersection_list.get(i),(1/min_distance));
				
			}
		}
		System.out.println(proximity_map);
		return proximity_map;
	}
	
	
	public  TreeMap<String, Integer> sortMapByValue(Map<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}
	
	
	
	
	
	public List<String> getWordsList(String str) throws Exception{
		List<String> qlist = new ArrayList<String>();
		Stopper stop = new Stopper();
		Scanner s;
		byte [] encoded;
        ArrayList<String> al = new ArrayList<String>();
        s = new Scanner(str);
        while (s.hasNext()) {
        	String word ="";
        	String nextword = s.next();
        	StringBuffer word_sb = new StringBuffer(nextword);
        	if(word_sb.toString().trim() != null  && word_sb.toString().trim() != ""){
        		if ((word_sb.charAt(0)=='.' && word_sb.charAt(word_sb.length()-1)=='.') 
	        			||(word_sb.charAt(0)==',' && word_sb.charAt(word_sb.length()-1)==',')  
	        			||(word_sb.charAt(0)=='\'' && word_sb.charAt(word_sb.length()-1)=='\'') 
	        			||(word_sb.charAt(0)=='(' && word_sb.charAt(word_sb.length()-1)==')') 
	        			||(word_sb.charAt(0)=='\"' && word_sb.charAt(word_sb.length()-1)=='\"') 
	        			||(word_sb.charAt(0)=='[' && word_sb.charAt(word_sb.length()-1)==']') 
	        			||(word_sb.charAt(0)=='{' && word_sb.charAt(word_sb.length()-1)=='}')
	        			||(word_sb.charAt(0)==';' && word_sb.charAt(word_sb.length()-1)==';')
	        			||(word_sb.charAt(0)=='#' && word_sb.charAt(word_sb.length()-1)=='#')){
	        		if(word_sb.length()-1 > 0){
	        			word = word_sb.substring(1,word_sb.length()-1).toLowerCase();
	        		}
	        	}else if (word_sb.charAt(0)=='.' 
	        			||word_sb.charAt(0)==',' 
	        			||word_sb.charAt(0)=='\'' 
	        			||word_sb.charAt(0)=='(' 
	        			||word_sb.charAt(0)=='\"'  
	        			|| word_sb.charAt(0)=='['  
	        			||word_sb.charAt(0)=='{'
	        			||word_sb.charAt(0)==';'
	        			||word_sb.charAt(0)=='#'){
	        		word = word_sb.substring(1,word_sb.length()).toLowerCase();
	        	}else if (word_sb.charAt(word_sb.length()-1)=='.' 
	        			||word_sb.charAt(word_sb.length()-1)==',' 
	        			||word_sb.charAt(word_sb.length()-1)=='\'' 
	        			||word_sb.charAt(word_sb.length()-1)==')' 
	        			||word_sb.charAt(word_sb.length()-1)=='\"'  
	        			|| word_sb.charAt(word_sb.length()-1)==']'  
	        			||word_sb.charAt(word_sb.length()-1)=='}'
	        			||word_sb.charAt(word_sb.length()-1)==';'
	        			||word_sb.charAt(word_sb.length()-1)=='#'){
	        		word = word_sb.substring(0,word_sb.length()-1).toLowerCase();
	        	}else{
	        		word = word_sb.toString().toLowerCase();
	        	}
	        	encoded = word.getBytes();
	        	String finalword = new String(encoded,"UTF-8");
	            if((finalword != null) && (finalword != "")){
	            	if(!stop.isStopWord(finalword)){
	            		if(!finalword.contains("#160;")){
	            			qlist.add(finalword);
	            		}
	            	}
	            }
        	}
        
	}
        return qlist;	

	}
	public  List<String> intersection(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<String>();

        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}