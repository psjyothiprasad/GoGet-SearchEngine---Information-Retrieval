package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class DocVectorFile {

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> qlist = new ArrayList<String>();
		Map<String,Double> map1 = new HashMap<String,Double>();
		Map<String,Double> map2 = new HashMap<String,Double>();
		Map<String,Map<String,Double>> mapofmap = new HashMap<String,Map<String,Double>>();
		map1.put("term1", 0.1);
		map1.put("term2", 0.2);
		map2.put("term3", 0.3);
		map2.put("term4", 0.4);
		mapofmap.put("map1", map1);
		mapofmap.put("map2", map2);
		qlist.add("term");
		String file_path ="C:/Users/sande/workspace/GoGet/WebContent/Table/dummy.txt";
		toFile(mapofmap,file_path);
		System.out.println(fromFile(file_path,qlist));
	}*/
	public static  void toFile(Map<String,Map<String,Double>> map, String file_path){
		Writer writer = null;
		StringBuilder sb = null;
		try{
			sb = new StringBuilder();
			writer = new BufferedWriter(new OutputStreamWriter(
		             new FileOutputStream(file_path), "utf-8"));
			sb.append("<");
			for(Map.Entry<String, Map<String,Double>> entry : ((Map<String, Map<String,Double>>)map).entrySet()){
				String fn = entry.getKey();
				Map<String,Double> imap = new HashMap<String,Double>();
				imap.putAll(entry.getValue());
				sb.append(fn+"{");
				for(Map.Entry<String, Double> ientry : ((Map<String,Double>) imap).entrySet()){
					String term = ientry.getKey();
					Double weight = ientry.getValue();
					sb.append(term+":"+weight+";");
				}
			//	sb.substring(sb.length());
				sb.append("},");
			}
			sb.replace(sb.length()-1,sb.length(),"");
			sb.append(">");
			
		    //String mapstring = map.toString(); 
			String mapstring = sb.toString(); 
		    //System.out.println(mapstring);
			writer.write(mapstring);
			writer.close();
			//System.out.println("written");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static  Map<String,Map<String,Double>> fromFile(String file_path, List<String> qlist){
		Map<String,Map<String,Double>> mapofmap = null;
		int ch;
		Map parsedMap = null;
		byte[] encoded;
		StringBuffer sb = null;
		FileInputStream fin = null;
		Map<String,Map<String,Double>> outmap = null;
		try {
			mapofmap = new HashMap<String,Map<String,Double>>();
			sb = new StringBuffer();
			File f = new File(file_path);
			fin = new FileInputStream(f);
			
			while( (ch = fin.read()) != -1){
				sb.append((char)ch);
			}
			fin.close();
			String smap = sb.toString();
			encoded = smap.getBytes();
			//encoded = Files.readAllBytes(Paths.get(xml_path));
			String serializedMap= new String(encoded, "UTF-8");
			serializedMap = sb.toString();
			String subMap = serializedMap.substring(1,serializedMap.length()-1);
			StringTokenizer st = new StringTokenizer(subMap, ",");
			outmap = new HashMap<String,Map<String,Double>>();
			while(st.hasMoreTokens()){
				Iterator<String> qitr = qlist.iterator();
				String dmap = st.nextToken().trim();
				boolean b = false;
				while(qitr.hasNext()){
					String qterm = qitr.next().trim();
					if(
							( 
									(dmap.contains("{"+qterm+":")) 
									&&
									(!dmap.contains("{"+qterm+":0.0"))
							)
							||
							(
									(dmap.contains(";"+qterm+":"))
									&&
									(!dmap.contains(";"+qterm+":0.0"))
							)
						){
						System.out.println(dmap.substring(0, 10)+"--------------------------"+qterm);
						b = true;
						//break;
					}
				}
				if(b){
					 
					StringTokenizer dst = new StringTokenizer(dmap, "{");
					while(dst.hasMoreTokens()){
						String fn = dst.nextToken();
						String dv = dst.nextToken();
						String dvector = dv.substring(0, dv.length()-1);
						Map<String, Double> inmap = new HashMap<String,Double>();
						StringTokenizer dvst = new StringTokenizer(dvector, ";");
						while(dvst.hasMoreTokens()){
							String dvmap = dvst.nextToken();
							StringTokenizer dvmst = new StringTokenizer(dvmap, ":");
							while(dvmst.hasMoreTokens()){
								String term = dvmst.nextToken();
								Double weight = Double.parseDouble(dvmst.nextToken());
								inmap.put(term, weight);
								//System.out.println(dvmst.nextToken());
							}
							outmap.put(fn,inmap);
						}
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(outmap);
		return outmap;
	}
	
	
	public void toObject(String fpath, Map<String,Map<String,Double>> doc_vectors){
		try {
			FileOutputStream fos = new FileOutputStream(fpath);
			
			ObjectOutputStream obj = new ObjectOutputStream(fos);
			
			obj.writeObject(doc_vectors);
			
			obj.flush();
			obj.close();
			fos.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public Map<String,Map<String,Double>> fromObject(String fpath){
		Map<String,Map<String,Double>> doc_vectors = new HashMap<String,Map<String,Double>>();
		Instant startTime2 = Instant.now();
		try {
			FileInputStream fis = new FileInputStream(fpath);
			
			ObjectInputStream obj = new ObjectInputStream(fis);
			
		
			doc_vectors.putAll((Map<String,Map<String,Double>>) obj.readObject());
			
			
			obj.close();
			fis.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instant endTime2 = Instant.now();
		System.out.println("DocVector fromObject -- Time Taken : "+Duration.between(startTime2, endTime2));
			
		return doc_vectors;
	}



}
