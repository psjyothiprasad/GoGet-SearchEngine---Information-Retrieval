package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import models.FileParserModel;
import models.Stemmer;
import models.Stopper;
public class FileParserUtility {
	public  String query =" sandeep   ganji ";
	public  List<String> processed_query_terms = new ArrayList<String>();
	@SuppressWarnings("rawtypes")
	public  Map documentvectors = new HashMap();
	




	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  Map getDocVectors(Map inverted_index, FileParserModel fpm) {
		System.out.println("getting docvectors....");
		Map<String,Double> documentvector = new HashMap<String,Double>();
		for(Map.Entry<String, Map> entry : ((Map<String, Map>) inverted_index).entrySet()){
			documentvector.put(entry.getKey(), 0.0);
		}
		Map<String,Map<String,Double>> docvectors = new HashMap<String,Map<String,Double>>();
		try{
			Files.walk(Paths.get(fpm.getFpath()))
			.forEach(filePath -> {
				String fileName ="";
			    if (Files.isRegularFile(filePath)) {
			    	File f = new File(""+filePath);
			    	String fullFilePath = ""+filePath;
			    	fileName = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
					try {
						Map<String,Double> docvector =new HashMap<String,Double>();
						docvector.putAll(documentvector);
						Map<String,List<Integer>> word_positions_map = new TreeMap( getWordsListFromFile(f,""));
						Set<String> hs = new HashSet();
						hs.addAll(word_positions_map.keySet());
						List<String> word_list_from_file = new ArrayList<String>();
						word_list_from_file.addAll(hs);
						Collections.sort(word_list_from_file);
						for(Map.Entry<String, Map> entry : ((Map<String, Map>) inverted_index).entrySet()){
							String dictionery_term = entry.getKey();
							for (int x = 0; x < word_list_from_file.size(); x++) {
								String doc_term = word_list_from_file.get(x);
								if(doc_term.trim().equalsIgnoreCase(dictionery_term.trim())){
									Map innermap = (Map) inverted_index.get(doc_term);
									int df = innermap.size();
									List filelist = getFileList(fpm);
									double idf = Math.log(filelist.size()/df)/Math.log(10);
									
									int tf = ((List) innermap.get(fileName)).size();
									Double weight = tf*idf;
									docvector.put(dictionery_term,weight);
									docvectors.put(fileName,docvector);
								}
							}
							
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			    } 
			    
			});
			    
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		documentvectors.putAll(docvectors);
		System.out.println("doc vectors fetched");
		return docvectors;
	}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
	public  List getFileList(FileParserModel fpm){
			
			List filelist = new ArrayList();
			try {
				Files.walk(Paths.get(fpm.getFpath()))
					.forEach(filePath -> {
					    if (Files.isRegularFile(filePath)) {
					    	String fullFilePath = ""+filePath;
					    	String fileName = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
					    	filelist.add(fileName);
					    } 
					});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return filelist;
		}
		
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public  Map tableGenerator(List fp, FileParserModel fpm){
			String filepath = fpm.getFpath();
			List filelist = getFileList(fpm);
			int filecount = filelist.size();
			Map<String,List> inverted_index_map = new HashMap<String,List>();
			Set<String> hs = new HashSet();
			hs.addAll(fp);
			fp.clear();
			fp.addAll(hs);
			Iterator termIterator=fp.iterator();
			Iterator itr=fp.iterator();
			while(itr.hasNext())
			{
				List df_idf_fn_tf_list = new ArrayList();
				Map<String,Integer> fn_tf_map = new HashMap<String,Integer>(); 
				List innerList = (List) itr.next();
				int df = 1;
				Iterator innerItr=fp.iterator();
				while(innerItr.hasNext())
				{
					List innerInnerList = (List) innerItr.next();
					if(innerList.get(0).equals(innerInnerList.get(0)) && !(innerList.get(1).equals(innerInnerList.get(1))) ){
						df++;
						fn_tf_map.put((String) innerList.get(1), (Integer) innerList.get(2));
						fn_tf_map.put((String) innerInnerList.get(1), (Integer) innerInnerList.get(2));
					}else{
						if(innerList.get(0).equals(innerInnerList.get(0)) && innerList.get(1).equals(innerInnerList.get(1))){
							fn_tf_map.put((String) innerList.get(1), (Integer) innerList.get(2));
						}
					}
				}
				double idf = Math.log(filelist.size()/df)/Math.log(10);
				df_idf_fn_tf_list.add(df);
				df_idf_fn_tf_list.add(idf);
				df_idf_fn_tf_list.add(fn_tf_map);
				inverted_index_map.put((String) innerList.get(0), df_idf_fn_tf_list);
			}
			Map sorted_inverted_index_map = new TreeMap(inverted_index_map);
		
				return sorted_inverted_index_map;
		}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  Map FileParser(FileParserModel fpm){
			System.out.println("getting inv index....");
			Map inv_index = new HashMap();
			try {
				Files.walk(Paths.get(fpm.getFpath()))
					.forEach(filePath -> {
					    if (Files.isRegularFile(filePath)) {
					    	String fullFilePath = ""+filePath;
					    	String fileName = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
							File f = new File(fullFilePath);
					         try {
					        	 Map<String,List<Integer>> word_positions_map = new TreeMap( getWordsListFromFile(f,""));
									Set<String> hs = new HashSet();
									hs.addAll(word_positions_map.keySet());
									List<String> word_list_from_file = new ArrayList<String>();
									word_list_from_file.addAll(hs);
									Collections.sort(word_list_from_file);
					            for (int x = 0; x < word_list_from_file.size(); x++) {
				            		String wordx = word_list_from_file.get(x);
				            		
				            		Map<String,List<Integer>> innermap = ((Map <String,List<Integer>>) inv_index.get(wordx));
				            		List<Integer> plist = word_positions_map.get(wordx);
				            		if(innermap == null){
				            			innermap = new HashMap<String,List<Integer>>();
				            			innermap.put(fileName,plist);
				            			inv_index.put(wordx, innermap);
				            		}
				            		
			            			innermap.put(fileName,plist);
			            			inv_index.put(wordx, innermap);
			            			
					            }
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
					   } 
					});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Map sorted_inverted_index_map = new TreeMap(inv_index);
			return sorted_inverted_index_map;
		}

	    public  Map<String,List<Integer>> getWordsListFromFile(File f, String str)
	       throws Exception {
		      	Stopper stop = new Stopper();
	    		Scanner s;
	    		byte [] encoded;
		        ArrayList<String> al = new ArrayList<String>();
				Map<String,List<Integer>> word_positions = new HashMap<String,List<Integer>>();
		        /*Writer writer = null;
				StringBuilder sb = null;
				int p = 0;
				try{
					sb = new StringBuilder();
					writer = new BufferedWriter(new OutputStreamWriter(
				             new FileOutputStream(""), "utf-8"));
			    */    if(str.length()>0)
			        	s = new Scanner(str);
			        else
			        	s = new Scanner(f);
			    		FileInputStream fis = new FileInputStream(f);
			        
			    		StringBuffer sb = new StringBuffer();
			    		
			    		
			    		
				        BufferedReader br;
						
											br = new BufferedReader(new FileReader(f.getPath()));
										
								        String line;
								        while ( (line=br.readLine()) != null) {
								          sb.append(line);
								        }
								     String nohtml = sb.toString();
								     
								     
								     
						
			    		
			    		
			    		
			    		
			    		
			    		
			    		
			    		
			    		
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
								
							
								List<Integer> plist = new ArrayList<Integer>();
								int pindex = -1;
								     pindex = nohtml.indexOf(finalword);
								     if(pindex != -1){
								  
								    	 plist.add(pindex);
								     }
								     while(pindex > 0){
								    	 pindex = nohtml.indexOf(finalword,pindex+1);
								    	 plist.add(pindex);
								    	 int rm =  -1;
								    	 plist.remove((Object) rm);
								     }
							
							
									word_positions.put(finalword,plist);
									//al.add(finalword);
			            	}
			            }
			        }
			        }
			        
/*	    }catch(Exception e){
			e.printStackTrace();
		}
*/		        }
			        s.close();
		        return word_positions;
	    }
	    
	    public List<Integer> getPositions(FileParserModel fpm, String term, String fpath){
	    	System.out.println("getting positions....");
	    	Map<String, List<Integer>> pmap = new HashMap<String, List<Integer>>();
	    	List<Integer> plist = new ArrayList<Integer>();
	    	try {
				/*Files.walk(Paths.get(fpm.getFpath()))
					.forEach(filePath -> {
						try {
						
					    if (Files.isRegularFile(filePath)) {
*/					    	Writer writer = null;
					    	//Writer stopperwriter = null;
					    	
					    		//String fullFilePath = ""+filePath;
								String fileName = fpath.substring(fpath.lastIndexOf('\\') + 1);
						    	StringBuilder sb = new StringBuilder();
						        BufferedReader br;
								
									br = new BufferedReader(new FileReader(fpath));
								
						        String line;
						        while ( (line=br.readLine()) != null) {
						          sb.append(line);
						        }
						     String nohtml = sb.toString();
						     
						     
						     int pindex = -1;
						     pindex = nohtml.indexOf(term);
						     if(pindex != -1){
						  
						    	 plist.add(pindex);
						     }
						     while(pindex > 0){
						    	 pindex = nohtml.indexOf(term,pindex+1);
						    	 plist.add(pindex);
						    	 int rm =  -1;
						    	 plist.remove((Object) rm);
						     }
						     
						     
						     	
					    	/*}
					    } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				 });*/
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	    	return plist;
	    }	    
	    
	    
	    
	    
	    @SuppressWarnings("resource")
		public  void htmlTagRemover(FileParserModel fpm){
	    	
	    	try {
				Files.walk(Paths.get(fpm.getOriginalfpath()))
					.forEach(filePath -> {
					
					    if (Files.isRegularFile(filePath)) {
					    	Writer writer = null;
					    	//Writer stopperwriter = null;
					    	try {
					    		String fullFilePath = ""+filePath;
					    		String fileName = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
						    	StringBuilder sb = new StringBuilder();
						        BufferedReader br;
								br = new BufferedReader(new FileReader(""+filePath));
						        String line;
						        while ( (line=br.readLine()) != null) {
						          sb.append(line);
						        }
						     String nohtml = sb.toString().replaceAll("\\<script>.*?</script>"," ").replaceAll("\\<style>.*?</style>"," ").replaceAll("\\<.*?>"," ").replaceAll("[\u0000-\u001f]", " ").replaceAll("\\p{Cntrl}", " ").replaceAll("[ÀàÁáÂâÃãÄäÅåÇçÈèÉéÊêËëÌìÍíÎîÏïÑñÒòÓóÔôÕõÖöÙùÚúÛûÜüÿØ§„Ø¹Ø±Ø¨ŠØ©Ð‘ŠÐ»Ð³Ð°€�ÐºÐ®¤®®®¿®´¯¼¿‡­æ–‡¾…²¾†¬Œæ—¥æœ¬ªžâ¢Ã£Â¢Ã¢â¬Ã¢Å]", " ").replaceAll("[^a-zA-Z]", " ");
						     //String opt = ExudeData.getInstance().getSwearWords(nohtml);
						     writer = new BufferedWriter(new OutputStreamWriter(
						             new FileOutputStream(fpm.getFpath()+fileName), "utf-8"));
						       writer.write(nohtml);
						       //String stopperfilepath = "E:/Sandeep/docsWithoutHtmlTags/"+fileName;
						       //String opt = ExudeData.getInstance().filterStoppings(stopperfilepath);
						       //System.out.println(opt);
						       /*stopperwriter = new BufferedWriter(new OutputStreamWriter(
							             new FileOutputStream("E:/Sandeep/docsWithoutHtmlTags/"+fileName), "utf-8"));
							       
						       stopperwriter.write(opt);*/
					    	} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								   try {writer.close();} catch (Exception ex) {/*ignore*/}
							}
					    	}
					
				 });
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	    }
}
