package crawl;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DataExtraction {

	public static String data(String seedUrl) throws IOException{
		
		    Connection connection=Jsoup.connect(seedUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");
		    Document conn = connection.ignoreContentType(true).get();
			if(connection.response().statusCode() == 200){
			 if(connection.response().contentType().contains("text/html")){
			    String plainText = conn.body().text();
			    return plainText;
			 }
			}
			return null;
	}
	
	
	
	
}

