package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class InvertedIndexFile {

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map <String,List<Object>> map = new HashMap<String,List<Object>>();
		String file_path ="C:/Users/sande/workspace/GoGet/WebContent/Table/index.txt";
		List<Object> ilist = new ArrayList<Object>();
		Map<String,Integer> imap = new HashMap<String,Integer>();
		imap.put("fn1", 1);
		imap.put("fn2", 2);
		ilist.add(1);
		ilist.add(0.1);
		ilist.add(imap);
		map.put("term1", ilist);
		map.put("term2", ilist);
		toFile(map, file_path);
		fromFile(file_path);

	}*/
	@SuppressWarnings("unchecked")
	public static  void toFile(Map<String,List<Object>> map, String file_path){
		Writer writer = null;
		StringBuilder sb = null;
		try{
			sb = new StringBuilder();
			writer = new BufferedWriter(new OutputStreamWriter(
		             new FileOutputStream(file_path), "utf-8"));
			sb.append("<");
			for(Map.Entry<String, List<Object>> entry : ((Map<String, List<Object>>)map).entrySet()){
				String term = entry.getKey();
				List<Object> ilist = new ArrayList<Object>();
				ilist.addAll(entry.getValue());
				sb.append(term+"[");
				Iterator<Object> itr = ilist.iterator();
				while(itr.hasNext()){
					int df = (Integer)itr.next();
					Double idf = (Double)itr.next();
					Map<String,Integer> imap = new HashMap<String,Integer>();
					imap.putAll((Map<String,Integer>)itr.next());
					sb.append(df+","+idf+",{");
					for(Map.Entry<String, Integer> ientry : ((Map<String,Integer>) imap).entrySet()){
						String fn = ientry.getKey();
						Integer tf = ientry.getValue();
						sb.append(fn+":"+tf+";");
					}
					sb.append("}]&");
				}
				
			//	sb.substring(sb.length());
				//sb.append("],");
			}
			sb.replace(sb.length()-1,sb.length(),"");
			sb.append(">");
			
		    //String mapstring = map.toString(); 
			String mapstring = sb.toString(); 
		   // System.out.println(mapstring);
			writer.write(mapstring);
			writer.close();
			//System.out.println("written");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "unused", "rawtypes" })
	public static  Map<String,List<Object>> fromFile(String file_path, List<String> qlist){
		Map<String,List<Object>> mapoflist = null;
		int ch;
		Map parsedMap = null;
		byte[] encoded;
		StringBuffer sb = null;
		FileInputStream fin = null;
		Map<String,List<Object>> outmap = null;
		try {
			mapoflist = new HashMap<String,List<Object>>();
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
			StringTokenizer st = new StringTokenizer(subMap, "&");
			outmap = new HashMap<String,List<Object>>();
			while(st.hasMoreTokens()){
				Iterator<String> qitr = qlist.iterator();
				String dmap = st.nextToken();
				boolean b = false;
				/*while(qitr.hasNext()){
					String qterm = qitr.next().trim();
					if(dmap.contains(qterm+"[")){
						b = true;
						//break;
					}
				}*/
				
	//			System.out.println(dmap);
				//if(b){
					StringTokenizer dst = new StringTokenizer(dmap, "[");
					while(dst.hasMoreTokens()){
						String term = dst.nextToken();
						String dv = dst.nextToken();
						String ilist = dv.substring(0, dv.length()-1);
			//			System.out.println(term+"-----------"+ilist);
						List<Object> inlist = new ArrayList<Object>();
						StringTokenizer dvst = new StringTokenizer(ilist, ",");
						while(dvst.hasMoreTokens()){
							int df = Integer.parseInt(dvst.nextToken());
							Double idf = Double.parseDouble(dvst.nextToken());
							String im = dvst.nextToken();
							String imap = im.substring(1,im.length()-2);
				//			System.out.println(df+"_______________"+idf+"__________"+imap);
							Map<String, Integer> inmap = new HashMap<String,Integer>();
							StringTokenizer dvmst = new StringTokenizer(imap, ";");
							while(dvmst.hasMoreTokens()){
								String fmap = dvmst.nextToken();
					//			System.out.println(fmap);
								StringTokenizer vmst = new StringTokenizer(fmap, ":");
								while(vmst.hasMoreTokens()){
									String fn = vmst.nextToken();
									int tf = Integer.parseInt(vmst.nextToken());
						//			System.out.println(fn+"****************"+tf);
									inmap.put(fn, tf);
								}
							}
							//System.out.println(inmap);
							inlist.add(df);
							inlist.add(idf);
							inlist.add(inmap);
						}
						//System.out.println(inlist);
						outmap.put(term, inlist);
					}
				//}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(outmap);
		return outmap;
	}
	
	public void toObject(String fpath, Map<String,Map<String,List<Integer>>> inv_index){
		try {
			FileOutputStream fos = new FileOutputStream(fpath);
			
			ObjectOutputStream obj = new ObjectOutputStream(fos);
			
			obj.writeObject(inv_index);
			
			obj.flush();
			obj.close();
			fos.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Map<String,List<Integer>>> fromObject(String fpath){
		Map<String,Map<String,List<Integer>>> inv_index = new HashMap<String,Map<String,List<Integer>>>();
		try {
			FileInputStream fis = new FileInputStream(fpath);
			
			ObjectInputStream obj = new ObjectInputStream(fis);
			
		
			inv_index.putAll(((Map<String,Map<String,List<Integer>>>) obj.readObject()));
			
			
			obj.close();
			fis.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inv_index;
	}
}
