package crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import crawl.Crawler;

class MultiThreading implements Runnable {
	   private Thread t;
	   private String threadName;
	   private List<String> uniqueLinks2 = new ArrayList<String>();
	   private String fpath2;
	   
	   MultiThreading( String name,List<String> uniqueList, String fpath){
	       threadName = name;
	       uniqueLinks2 = uniqueList;
	       fpath2=fpath;	       
	       System.out.println("Creating " +  threadName );
	   }
	   public void run() {
	      System.out.println("Running " +  threadName );
	      try {
	    	  Crawler.ExtractingText(uniqueLinks2, fpath2,threadName);
	          Thread.sleep(1000);
	         }
	      catch (InterruptedException e) {
	         System.out.println("Thread " +  threadName + " interrupted.");
	     } catch (IOException e) {
			e.printStackTrace();
		}
	     System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start ()
	   {
	      System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }

	}

	