package dissertaion;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	String QUERY_FILE;
	String STOPWORDS_FILE;
	String USE_QUERY_TITLE;
	String USE_QUERY_ABSTRACT;
	Integer NO_OF_HITS = 100;
	Integer TOTAL_HITS = 200;
	
	IndexReader reader;
	IndexSearcher searcher;
	myAnalyzer myanalyzer;
	Analyzer analyzer;

    MultiFieldQueryParser mFQueryParser;
	
	public Searcher(Map<String, String> FILE_PATH) {
//		INDEX_PATH_CONTENT = FILE_PATH.get("INDEX_PATH_CONTENT");
		QUERY_FILE = FILE_PATH.get("QUERY_FILE");
//		RESULT_FILE = FILE_PATH.get("RESULT_FILE");
		STOPWORDS_FILE = FILE_PATH.get("STOPWORDS_FILE");
		USE_QUERY_TITLE = FILE_PATH.get("USE_QUERY_TITLE");
		USE_QUERY_ABSTRACT = FILE_PATH.get("USE_QUERY_ABSTRACT");
//		INDEX_PATH_REFERENCE = FILE_PATH.get("INDEX_PATH_REFERENCE");
	}
	
	
	public void search(String INDEX_PATH, String RESULT_FILE) throws Exception{
		Map<String, String> pnumToCid = new CreateQueryPapernumCid("D:\\develop\\Dissertation-2020\\data\\query\\v1\\paperNum-doi-cid.v1.txt").getMap();
		
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));
	    searcher = new IndexSearcher(reader);
	    
	    searcher.setSimilarity(new LMJelinekMercerSimilarity((float) 0.5));
	    myanalyzer = new myAnalyzer(STOPWORDS_FILE);
    	analyzer = myanalyzer.analyzer;
    	
    	String []fields = new String[]{"abstract", "title", "context"};
        
        mFQueryParser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
    
        ScoreDoc[] hits = null;
        TopDocs topDocs = null;
        FileWriter fw = new FileWriter(RESULT_FILE);
        Date initTime = new Date();
//        Date start = new Date();
        int count = 0;
        
        QueryParser parser = new QueryParser(QUERY_FILE, USE_QUERY_TITLE, USE_QUERY_ABSTRACT);
        List<SingleQuery> queries = parser.getQueriesList();
        System.out.println("Got list of queries... Starting search");
        
        for (SingleQuery query : queries) {
            String query_str;
            
            TopScoreDocCollector collector = TopScoreDocCollector.create(NO_OF_HITS, TOTAL_HITS);
            query_str = query.getBOWQuery(analyzer).replace(":", " ");
//            query_str = myanalyzer.cleanText(query_str);
//            System.out.println(query_str);
            searcher.search(mFQueryParser.parse(query_str), collector);
            
          
            topDocs = collector.topDocs();
            hits = topDocs.scoreDocs;
            StringBuffer buff = new StringBuffer();
            Integer tt = 0;
            for (int i = 0; i < hits.length; ++i) {
            	int docId = hits[i].doc;
            	Document d = searcher.doc(docId);
//            	check
            	Integer paperNum = Integer.parseInt(query.queryNum.toString())/100;
            	
            	if(!(d.get("doi").equals(pnumToCid.get(paperNum.toString())))) {
	            	buff.append(query.queryNum).append("\tQ0\t").
		                append(d.get("doi")).append("\t").
		                append((tt)).append("\t").
		                append(hits[i].score).append("\t").
		                append("first").append("\n");
	            	tt++;
            	}
            }
            fw.write(buff.toString());
            Date tmp = new Date();
            if(count%50==0) {
				System.out.println("Time Passed: " + (tmp.getTime()-initTime.getTime())/60000 + " mins, Query Completed: " + count);
//				start = new Date();
			}
            count++;
            
        }
        fw.close();
            
	}
	
        
}