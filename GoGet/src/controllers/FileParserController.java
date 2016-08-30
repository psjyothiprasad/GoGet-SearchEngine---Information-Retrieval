package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DocVectorFile;
import models.FileParserModel;
import models.InvertedIndexFile;
import models.Stemmer;
//import models.XmlOperator;
import utilities.FileParserUtility;

/**
 * Servlet implementation class FileParserController
 */
@WebServlet("/FileParserController")
public class FileParserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileParserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("rawtypes")
	private  Map documentvectors = new HashMap();
	@SuppressWarnings("rawtypes")
	private  Map inverted_index = new HashMap();
	@SuppressWarnings("rawtypes")
	public  Map getInverted_index() {
		return inverted_index;
	}
	@SuppressWarnings("rawtypes")
	public  void setInverted_index(Map inverted_index) {
		this.inverted_index = inverted_index;
	}
	@SuppressWarnings("rawtypes")
	public  Map getDocumentvectors() {
		return documentvectors;
	}
	@SuppressWarnings("rawtypes")
	public  void setDocumentvectors(Map documentvectors) {
		this.documentvectors = documentvectors;
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	//protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	public static void main(String args[]) {
	
		// TODO Auto-generated method stub
		System.out.println("fpc started....");
		FileParserController fpc = new FileParserController();
		String originalfpath= "E:/Sandeep/docsnew/";
		String fpath =	"E:/Sandeep/docsWithoutHtmlTags/";
		//String fpath = "C:/Users/sande/workspace/GoGet/WebContent/Extras";
		String dvf_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/docvec.txt";
		String iif_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/index.txt";
		//String docvectors_xml_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/docvectors.xml";
		//String inverted_index__xml_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/inverted_index.xml";
		//public static String fpath =	"C:/Users/sande/workspace/GoGet/WebContent/Extras";	
		//String foutpath = "C:/Users/sande/workspace/GoGet/WebContent/Table/table.txt";
		FileParserModel fpm = new FileParserModel(fpath, originalfpath);
		FileParserUtility fpu = new FileParserUtility();
		Stemmer stem = new Stemmer();
		fpu.htmlTagRemover(fpm);
		stem.stemFile();
		System.out.println("processed....");
		Map fp = new HashMap() ;
		fp.putAll(fpu.FileParser(fpm));
		fpc.getInverted_index().putAll(fp);
		fpc.getDocumentvectors().putAll(fpu.getDocVectors(fpc.getInverted_index(),fpm));
		DocVectorFile dvf = new DocVectorFile();
		InvertedIndexFile iif = new InvertedIndexFile();
		
		dvf.toObject( dvf_path,fpc.getDocumentvectors());
		iif.toObject( iif_path,fpc.getInverted_index());
		//XmlOperator xo = new XmlOperator();
		//xo.encoder(fpc.getDocumentvectors(), docvectors_xml_path);
		//xo.encoder(fpc.getInverted_index(), inverted_index__xml_path);
		//System.out.println(documentvectors);
		//GoGetterModel ggm = new GoGetterModel();
		//ServletContext context = request.getSession().getServletContext();
		//context.setAttribute("docvectors", fpc.getDocumentvectors());
		//context.setAttribute("index", fpc.getInverted_index());
		//DocumentVectors.vectors.putAll(fpc.getDocumentvectors()); 
		//InvertedIndex.index.putAll(fpc.getInverted_index());
		//System.out.println(DocumentVectors.vectors);
		
		System.out.println("Success");
		//System.out.println(fpc);
		//ggm.setInverted_index(inverted_index);
		//ggm.setDocumentvectors(documentvectors);
		//System.out.println(context.getAttribute("index"));
		//System.out.println(fpc.getDocumentvectors());
		//System.out.println(fpc.getInverted_index());
	}


}
