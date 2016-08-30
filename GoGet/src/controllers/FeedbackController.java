package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.GoGetterUtility;

/**
 * Servlet implementation class FeedbackController
 */
@WebServlet("/FeedbackController")
public class FeedbackController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		GoGetterUtility ggu =new GoGetterUtility();
		Map<String,Double> queryvector = new HashMap<String,Double>();
		queryvector.putAll((Map<String,Double>)session.getAttribute("queryvector"));
		List<String> rankedlist = new ArrayList<String>();
		rankedlist.addAll((List<String>)session.getAttribute("rankedlist"));
		Map<String,Map<String,Double>> documentvectors = new HashMap<String,Map<String,Double>>();
		documentvectors.putAll((Map<String,Map<String,Double>>)session.getAttribute("documentvectors"));
		System.out.println(queryvector.size()+"--------------"+rankedlist.size());
		//System.out.println(queryvector);
		/*Map<String,Double> sum_vector = new HashMap<String,Double>();
		for(Map.Entry<String, Double> entry : queryvector.entrySet()){
			sum_vector.put(entry.getKey(), 0.0);
		}*/
		//TreeMap<String, Double> sorted_sum_vector = new TreeMap<String,Double>(sum_vector);
		TreeMap<String, Double> sorted_query_vector = new TreeMap<String,Double>(queryvector);
		Enumeration<String> e= request.getParameterNames();
		while(e.hasMoreElements()){
			String [] v=request.getParameterValues(e.nextElement());
			for(int i = 0;i<v.length;i++){
				Map<String,Double> dv = documentvectors.get(v[i]);
				if(dv != null){
					TreeMap<String, Double> dvector = new TreeMap<String, Double>(dv);
					for(Map.Entry<String, Double> entry : sorted_query_vector.entrySet()){
						sorted_query_vector.put(entry.getKey(), entry.getValue()+(dvector.get(entry.getKey())/v.length));
					}
				}
		//		System.out.println(v[i]);
			}
		}
		
		
		//System.out.println(sorted_query_vector);
		Map<String,Map<String,List<Integer>>> inverted_index = (Map<String,Map<String,List<Integer>>>)session.getAttribute("inverted_index");
		Map<String,Map<String,Double>> candidate_vectors = (Map<String,Map<String,Double>>)session.getAttribute("candidate_vectors");
		Map<String,Double> similarities = new HashMap<String,Double>();
		similarities.putAll(ggu.getSimilarities(candidate_vectors, sorted_query_vector));
		Map<String,Double> final_ranks = new HashMap<String,Double>();
		List<String> processed_terms = (List<String> )session.getAttribute("processed_terms");
		final_ranks.putAll((Map<String,Double>)ggu.getFinalRanks(similarities,((Map<String,Double>) ggu.getTermProximities(inverted_index, candidate_vectors,processed_terms ))));
		Map<String, Double> ranked_map = ggu.getRankedResults(similarities);
		System.out.println(ranked_map);
		Set<String> keys = ranked_map.keySet();
		List<String> keylist = new ArrayList<String>();
		keylist.addAll(keys);
		System.out.println(keylist);
		if(keylist != null){
			session.setAttribute("inverted_index", "");
			session.setAttribute("inverted_index", inverted_index);
			session.setAttribute("similarities", "");
			session.setAttribute("similarities", similarities);
			session.setAttribute("proximities","");
			session.setAttribute("proximities", ((Map<String,Double>) ggu.getTermProximities(inverted_index, candidate_vectors, processed_terms)));
			session.setAttribute("rankedlist", "");
			session.setAttribute("rankedlist", keylist);
			session.setAttribute("queryvector", "");
			session.setAttribute("queryvector", queryvector);
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
