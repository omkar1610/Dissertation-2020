package dissertaion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

public class myAnalyzer {
	Analyzer analyzer;
	
	public myAnalyzer(String STOPWORDS_FILE) throws IOException {
		analyzer = new EnglishAnalyzer(StopFilter.makeStopSet(getStopwords(STOPWORDS_FILE)));
	}
	
	private List<String> getStopwords(String filePath) throws IOException{
		List<String> stopwords = new ArrayList<>();
		  	 	
	    String line;
//	    System.out.println(filePath);
	    BufferedReader br = new BufferedReader(new FileReader(filePath));
	    while ( (line = br.readLine()) != null ) {
	        stopwords.add(line.trim());
	    }
	    br.close(); 
	    return stopwords;
	}
	
	public String cleanText(String text) {
//		text = text.replaceAll("\\p{Punct}", "");
		return text;
	}
}
