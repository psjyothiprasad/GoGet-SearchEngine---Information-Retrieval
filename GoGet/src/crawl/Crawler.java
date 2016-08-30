package crawl;
import java.io.*;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import crawl.DataExtraction;
import crawl.MultiThreading;

public class Crawler {
	
	static List<String> uniqueLinksA = new ArrayList<String>();
	static List<String> uniqueLinksB = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		String fpath ="E:/output.txt";
		String seedUrl = "https://www.wikipedia.org/" ;
		List<String> urlLinks = new LinkedList<String>();
		Set<String> uniqueLinks = new HashSet<String>();
		
		int i = 1;
		try{
			do{
				Connection connection=Jsoup.connect(seedUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");
				Document html = connection.ignoreContentType(true).get();
				Elements urls = html.select("a[href]");
					
	        System.out.println("\n Initial Links from JSOUP connection to seedurl "+ urls.size());
	        System.out.println("Initial Linked List Size "+urlLinks.size());
	        for (Element link : urls) {
	        	if(connection.response().statusCode() == 200){
	        		if(connection.response().contentType().contains("text/html")){
	        		urlLinks.add(link.absUrl("href"));
		           	uniqueLinks.add(link.absUrl("href"));
	        	}
	        	
	        	}       	
	        }
	        seedUrl = urlLinks.get(i);
	        i++;
	        System.out.println("updated i value=" +i);           
    		System.out.println("New Seed URL "+seedUrl);
	        	
	        }while(uniqueLinks.size()<=1000 );
			
	           
	        	        
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		splitSet(uniqueLinks);
		MultiThreading R1 = new MultiThreading( "Thread-1",uniqueLinksA,fpath);
	    R1.start();
	      
	    MultiThreading R2 = new MultiThreading( "Thread-2",uniqueLinksB,fpath);
	    R2.start();
	    
	    System.out.println("Final Total Size of Linked List"+urlLinks.size());
	      
	    System.out.println("Final Unique Links Total Size"+uniqueLinks.size());
		//ExtractingText(uniqueLinks,fpath);
	    System.out.println(uniqueLinks);
	}	
		
	
		//static List<String> urlCheck = new LinkedList<String>();
		static List<String> urlTrash = new LinkedList<String>();
	    public static void ExtractingText(List<String> uniqueList, String fpath,String threadname) throws IOException{
	    
		
	    Iterator<String> loop=uniqueList.iterator();
		
		for(int j=0;loop.hasNext();j++) {
		String list_element =loop.next();
		System.out.println("Current Url:" + list_element + " " + threadname);
		String output = DataExtraction.data(list_element);
		
        String foutpath2 = fpath +j+"_"+threadname+"_"+"file.txt";
       	Writer writer = null;
		//urlCheck.add(loop.next());
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			         new FileOutputStream(foutpath2)));			
			writer.write(output);
			}
		catch (Exception e) {
				e.printStackTrace();
			} finally {
				   try {writer.close(); } catch (Exception ex) {/*ignore*/}
				} 
		
			}
		
	    }     
        
	    public static void splitSet (Set<String> uniqueLinks){
	    	uniqueLinksA.addAll(uniqueLinks);
	    	int size=uniqueLinks.size();
	    	for(int p=0;p<size/2;p++){
	    		uniqueLinksB.add(uniqueLinksA.get(p));
	    		uniqueLinksA.remove(p);
	    	}
	    	System.out.println("uniquelinksA size :"+uniqueLinksA.size());
	    	System.out.println("uniquelinksB size :"+uniqueLinksB.size());
	    }
       

}
