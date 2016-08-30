package models;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

public class XmlWriter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			System.out.println(encoder());
			System.out.println(decoder(encoder()));
	}
	public static String encoder(){
		Map<String, String> map = new HashMap<String, String>();  
		map.put("color", "red");  
		map.put("symbols", "{,=&*?}");  
		map.put("empty", "");  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		XMLEncoder xmlEncoder = new XMLEncoder(bos);  
		xmlEncoder.writeObject(map);  
		xmlEncoder.close();  
		  
		String serializedMap = bos.toString();  
		System.out.println(serializedMap);
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			         new FileOutputStream("E:/Sandeep/xmlwriter.xml"), "utf-8"));
			writer.write(serializedMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
	       
		return serializedMap;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, String> decoder(String serializedMap){
		XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(serializedMap.getBytes()));  
		Map<String, String> parsedMap = (Map<String, String>) xmlDecoder.readObject();  
		xmlDecoder.close();  
		/*for (String key : map.keySet()) {  
		 Assert.assertEquals(parsedMap.get(key), map.get(key));  
		}*/
		return parsedMap;
				}
}
