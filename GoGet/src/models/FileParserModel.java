
/**
 * 
 */
package models;
public class FileParserModel {
private String fpath;
private String originalfpath;
public String getFpath() {
	return fpath;
}
public void setFpath(String fpath) {
	this.fpath = fpath;
}
public String getOriginalfpath() {
	return originalfpath;
}
public void setOriginalfpath(String originalfpath) {
	this.originalfpath = originalfpath;
}

public FileParserModel(String fpath, String originalfpath) {
	super();
	this.fpath = fpath;
	this.originalfpath = originalfpath;
}


}
