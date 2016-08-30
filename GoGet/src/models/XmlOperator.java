package models;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.testng.Assert;

public class XmlOperator {

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String xml_path = "E:/Sandeep/xmlwriter.xml";
		    //String xml_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/docvectors.xml";
			Map inner_map1 = new HashMap();
			inner_map1.put("sandeep", 0.1);
			inner_map1.put("ganji", 0.2);
			Map inner_map2 = new HashMap();
			inner_map2.put("walking", 0.1);
			inner_map2.put("dead", 0.2);
			Map map = new HashMap(); 
			map.put("inner_map1", inner_map1);
			map.put("inner_map2", inner_map2);
			System.out.println(encoder(map,xml_path));
			System.out.println(decoder(xml_path));
	}
*/	public  String encoder(Map map, String xml_path){  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		XMLEncoder xmlEncoder = new XMLEncoder(bos);  
		xmlEncoder.writeObject(map);  
		xmlEncoder.close();   
		String serializedMap = bos.toString();  
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			         new FileOutputStream(xml_path), "UTF-8"));
			writer.write(serializedMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			   try {writer.close();} catch (Exception ex) {ex.printStackTrace();}
		}
	       
		return xml_path;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  Map decoder(String xml_path){
		int ch;
		Map parsedMap = null;
		byte[] encoded;
		StringBuffer sb = null;
		FileInputStream fin = null;
		try {
			sb = new StringBuffer();
			File f = new File(xml_path);
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
			XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(serializedMap.getBytes()));  
			parsedMap = (Map) xmlDecoder.readObject();  
			xmlDecoder.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return parsedMap;
	}
}
