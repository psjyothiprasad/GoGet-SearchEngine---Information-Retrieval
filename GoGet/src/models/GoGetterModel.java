package models;

import java.util.Map;

public class GoGetterModel {
private String query;
private Map documentvectors;
private Map inverted_index;

public GoGetterModel() {
	super();
}

public GoGetterModel(String query, Map documentvectors, Map inverted_index) {
	super();
	this.query = query;
	this.documentvectors = documentvectors;
	this.inverted_index = inverted_index;
}

public Map getDocumentvectors() {
	return documentvectors;
}

public void setDocumentvectors(Map documentvectors) {
	this.documentvectors = documentvectors;
}

public Map getInverted_index() {
	return inverted_index;
}

public void setInverted_index(Map inverted_index) {
	this.inverted_index = inverted_index;
}

public String getQuery() {
	return query;
}

public void setQuery(String query) {
	this.query = query;
}


}
