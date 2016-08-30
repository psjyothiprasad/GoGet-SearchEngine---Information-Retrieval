package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.FileParserUtility;
import utilities.GoGetterUtility;
import models.DocVectorFile;
import models.FileParserModel;
import models.InvertedIndexFile;
import models.QueryStemmer;
import models.Stopper;
/**
 * Servlet implementation class GoGetterController
 */
@WebServlet("/GoGetterController")
public class GoGetterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoGetterController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Stopper s = new Stopper();
		String dummy_file_path = "E:/dummy.txt";
		String query =request.getParameter("query");
		System.out.println(query);
		List<String> query_terms = new ArrayList<String>();
		List<String> processed_terms = new ArrayList<String>();
		Map<String,Map<String,Double>> documentvectors = new HashMap<String,Map<String,Double>>();
		Map<String,Map<String,List<Integer>>> inverted_index = new HashMap<String,Map<String,List<Integer>>>();
		String dvf_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/docvec.txt";
		String iif_path = "C:/Users/sande/workspace/GoGet/WebContent/Table/index.txt";
		DocVectorFile dvf = new DocVectorFile();
		InvertedIndexFile iif = new InvertedIndexFile();
		FileParserUtility fpu = new FileParserUtility();
		GoGetterUtility ggu = new GoGetterUtility();
		File f = new File(dummy_file_path);
		try {
			query_terms.addAll(ggu.getWordsList(query));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> itr = query_terms.iterator();
		while(itr.hasNext()){
			String qTerm = itr.next().trim();
			Boolean output = s.isStopWord(qTerm);
			if(output == false){
				processed_terms.add(QueryStemmer.query(qTerm).trim());  
			}
		}
		System.out.println(processed_terms);
		documentvectors.putAll(dvf.fromObject(dvf_path));
		inverted_index.putAll(iif.fromObject(iif_path));
		Map<String,Map<String,Double>> candidate_vectors = new HashMap<String,Map<String,Double>>();
		Iterator<String> qitr = processed_terms.iterator();
		while(qitr.hasNext()){
			String qterm = qitr.next();
			if(inverted_index.get(qterm)!=null){
				Iterator<String> citr = inverted_index.get(qterm).keySet().iterator();
				while(citr.hasNext()){
					String doc = citr.next();
					System.out.println(doc);
					candidate_vectors.put(doc, documentvectors.get(doc));
				}
			}
		}
		System.out.println("candiade size -----------"+candidate_vectors.size());
		//System.out.println(documentvectors);
		//System.out.println(inverted_index);
		System.out.println(inverted_index.size());
		System.out.println(documentvectors.size());
		Map queryvector = new HashMap();
		String originalfpath= "E:/Sandeep/docsnew/";
		String fpath =	"E:/Sandeep/docsWithoutHtmlTags/";
		FileParserModel fpm = new FileParserModel(fpath, originalfpath);
		queryvector.putAll(ggu.getQueryVector(processed_terms, inverted_index, fpm));
		//System.out.println(queryvector);
		Map<String,Double> similarities = new HashMap<String,Double>();
		similarities.putAll(ggu.getSimilarities(candidate_vectors, queryvector));
		Map<String,Double> final_ranks = new HashMap<String,Double>();
		final_ranks.putAll(ggu.getFinalRanks(similarities,ggu.getTermProximities(inverted_index, candidate_vectors, processed_terms)));
		Map<String, Double> ranked_map = ggu.getRankedResults(similarities);
		System.out.println(ranked_map);
		Set<String> keys = ranked_map.keySet();
		List<String> keylist = new ArrayList<String>();
		keylist.addAll(keys);
		System.out.println(keylist);
		if(keylist != null){
			HttpSession session = request.getSession();
			session.setAttribute("similarities", "");
			session.setAttribute("similarities", similarities);
			session.setAttribute("proximities", ggu.getTermProximities(inverted_index, candidate_vectors, processed_terms));
			session.setAttribute("rankedlist", "");
			session.setAttribute("rankedlist", keylist);
			session.setAttribute("queryvector", "");
			session.setAttribute("queryvector", queryvector);
			session.setAttribute("inverted_index", "");
			session.setAttribute("inverted_index", inverted_index);
			session.setAttribute("documentvectors", "");
			session.setAttribute("documentvectors", documentvectors);
			session.setAttribute("candidate_vectors", "");
			session.setAttribute("candidate_vectors", candidate_vectors);
			session.setAttribute("processed_terms", "");
			session.setAttribute("processed_terms", processed_terms);
			RequestDispatcher rd=request.getRequestDispatcher("results.jsp");
			rd.forward(request, response);
		}
		
	}

}
