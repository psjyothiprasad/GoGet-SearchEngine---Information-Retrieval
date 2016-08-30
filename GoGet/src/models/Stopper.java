package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Stopper {

	public boolean isStopWord(String word) {
		// TODO Auto-generated method stub
		boolean b = false;
		List<String> al = null;
		try {
			File f = new File("C:/Users/sande/workspace/GoGet/WebContent/stopwords.txt");
			Scanner s = new Scanner(f);
			al = new ArrayList<String>();
			while(s.hasNext()){
				al.add(s.next().trim());
			}
			if(al.contains(word))
				b = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

}
