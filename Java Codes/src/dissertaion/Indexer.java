package dissertaion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	String INDEX_PATH_CONTENT;
	String INDEX_PATH_REFERENCE;
	File COLLECTION_PATH;
	String CLUSTERID_DOI;
	String STOPWORDS_FILE;
	String MERGED_FILE_CONTENT;
	String MERGED_FILE_REFERENCE;
	
	int counter = 0;
	myAnalyzer myanalyzer;
	Analyzer analyzer;
	IndexWriter writer, ref_writer;
	Map<String, String> doiToCid;
	
	public Indexer(Map<String, String> FILE_PATH) throws IOException {
		INDEX_PATH_CONTENT = FILE_PATH.get("INDEX_PATH_CONTENT");
		INDEX_PATH_REFERENCE = FILE_PATH.get("INDEX_PATH_REFERENCE");
		COLLECTION_PATH = new File(FILE_PATH.get("COLLECTION_PATH"));
		CLUSTERID_DOI = FILE_PATH.get("CLUSTERID_DOI");
		STOPWORDS_FILE = FILE_PATH.get("STOPWORDS_FILE");
		MERGED_FILE_CONTENT = FILE_PATH.get("MERGED_FILE_CONTENT");
		MERGED_FILE_REFERENCE = FILE_PATH.get("MERGED_FILE_REFERENCE");
		myanalyzer = new myAnalyzer(STOPWORDS_FILE);
    	analyzer = myanalyzer.analyzer;
    	
    	
	}
    
    public void indexFromXml() throws IOException {
	    System.out.println("Indexing Started...");
	    // Create doi to cid mapping as the qrels show cid
    	doiToCid = new CreateDoiCidMap(CLUSTERID_DOI).getMap();
    	
    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    	writer = new IndexWriter(FSDirectory.open(Paths.get(INDEX_PATH_CONTENT)), iwc);
    	
	    Date start = new Date();
	    Date initTime = new Date();
	    
	    File[] files = COLLECTION_PATH.listFiles();
	    System.out.println("Initialization Finished.");
	    
	    for (File file : files) {
	    	Date tmp = new Date();
	    	if((tmp.getTime()-start.getTime())>5000) {
				System.out.println("Time Passed: " + (tmp.getTime()-initTime.getTime())/1000 + " secs, File Indexed: " + counter);
				start = new Date();
			}
	    	//Indexing each file
	        XmlParser xmlParser = new XmlParser(file);
	        xmlParser.parseXml();
	      
	        Document doc = new Document();
//	        This store cluster id with name doi
	        doc.add(new StringField("doi", doiToCid.get(xmlParser.doi), Field.Store.YES));
	        doc.add(new TextField("title", getTokenizedContent(xmlParser.title).toString(), Field.Store.YES));
	        doc.add(new TextField("abstract", getTokenizedContent(xmlParser.abstrct).toString(), Field.Store.YES));
	        doc.add(new TextField("context", getTokenizedContent(xmlParser.contexts.toString()).toString(), Field.Store.YES));
	        writer.addDocument(doc);
	        counter++;
	    }
	    
	    writer.close();
	    System.out.println("Indexing finished: " + counter + " files indexed");
    }
    
    public void indexFromMergedContent() throws IOException{
    	System.out.println("Indexing Started...");
	    
    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    	writer = new IndexWriter(FSDirectory.open(Paths.get(INDEX_PATH_CONTENT)), iwc);
   
	    Date initTime = new Date();
	    
    	BufferedReader br=new BufferedReader(new FileReader(new File(MERGED_FILE_CONTENT)));
		String line;
		
	    System.out.println("Initialization Finished.");
		while((line=br.readLine())!=null) {
			Date tmp = new Date();
	    	if(counter%10000==0) {
				System.out.println("Time Passed: " + (tmp.getTime()-initTime.getTime())/60000 + " mins, Files Indexed: " + counter);
			}
			Document doc = new Document();
			doc.add(new StringField("doi", line, Field.Store.YES));
			doc.add(new TextField("title", getTokenizedContent(br.readLine()).toString(), Field.Store.YES));
			doc.add(new TextField("abstract", getTokenizedContent(br.readLine()).toString(), Field.Store.YES));
	        doc.add(new TextField("context", getTokenizedContent(br.readLine()).toString(), Field.Store.YES));
	        
	        writer.addDocument(doc);
	        counter++;
		}
		writer.close();
	    System.out.println("Indexing finished: " + counter + " files indexed");
    }
    
    public void indexFromMergedReference() throws IOException{
    	System.out.println("Indexing Started...");
	    
    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        ref_writer = new IndexWriter(FSDirectory.open(Paths.get(INDEX_PATH_REFERENCE)), iwc);
      
	    Date initTime = new Date();
	    
    	BufferedReader br=new BufferedReader(new FileReader(new File(MERGED_FILE_REFERENCE)));
		String line;
		
	    System.out.println("Initialization Finished.");
		while((line=br.readLine())!=null) {
			Date tmp = new Date();
	    	if(counter%10000==0) {
				System.out.println("Time Passed: " + (tmp.getTime()-initTime.getTime())/60000 + " mins, Cluster IDs Indexed: " + counter);
			}
			Document doc = new Document();
			String id = line.split(",", 2)[0];
			String context = line.split(",", 2)[1];

			doc.add(new StringField("doi", id, Field.Store.YES));
	        doc.add(new TextField("context", getTokenizedContent(context).toString(), Field.Store.YES));
	        
	        ref_writer.addDocument(doc);
	        counter++;
		}
		ref_writer.close();
	    System.out.println("Indexing finished: " + counter + " files indexed");
    }
    
    StringBuffer getTokenizedContent(String text) throws IOException {
    	
    	StringBuffer tokenizedContentBuff = new StringBuffer();
    	
    	text = myanalyzer.cleanText(text);
        TokenStream stream = analyzer.tokenStream("title", new StringReader(text));
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
        stream.reset();

        while (stream.incrementToken()) {
            String term = termAtt.toString().toLowerCase();
            tokenizedContentBuff.append(term).append(" ");
        }

        stream.end();
        stream.close();
    	
        return tokenizedContentBuff;
    }
    
}
