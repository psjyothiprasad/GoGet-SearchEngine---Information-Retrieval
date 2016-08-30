package models;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import javax.xml.bind.annotation.XmlElement;


public class XmlDealer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File inputFile = new File("E:/Sandeep/xmlwriter.xml");
	        DocumentBuilderFactory dbFactory 
	           = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			Element doc_element = doc.getDocumentElement();
			doc_element.normalize();
			NamedNodeMap attributes = doc_element.getAttributes();
			Node javaa = doc_element.getParentNode().getFirstChild();
			Node objectt = javaa.getFirstChild().getNextSibling();
			String outerkey =objectt.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getChildNodes().item(0).getNodeValue();
			
			//System.out.println(objectt.);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
