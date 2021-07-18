package dissertaion;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

public class SingleQuery {
	String doi = "";
	String paperTitle = "";
	String paperAbstract = "";
	String context = "";
	Integer paperNum, queryNum;
	
	public SingleQuery() {
		;
	}
	
	public String getBOWQuery(Analyzer analyzer) throws Exception {
        String q = "";

        String[] terms = analyzeQuery(analyzer, paperTitle, paperAbstract, context).split("\\s+");
        for (String term : terms) {
//        	System.out.println(new TermQuery(new Term(term)).toString());
            q = q+new TermQuery(new Term(term)).toString();
        }
        return q;
    }
	
	private String analyzeQuery(Analyzer analyzer, String title, String abstrct, String context) throws IOException {
//		System.out.println(title + abstrct + context);
        StringBuffer buff = new StringBuffer(); 

        TokenStream stream = analyzer.tokenStream("title", new StringReader(title));
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
        stream.reset();

        while (stream.incrementToken()) {
        	buff.append(termAtt.toString().toLowerCase()).append(" ");
        }
        stream.end();
        stream.close();

        stream = analyzer.tokenStream("abstract", new StringReader(abstrct));
        termAtt = stream.addAttribute(CharTermAttribute.class);
        stream.reset();

        while (stream.incrementToken()) {
        	buff.append(termAtt.toString().toLowerCase()).append(" ");
        }

        stream.end();
        stream.close();

        stream = analyzer.tokenStream("context", new StringReader(context));
        termAtt = stream.addAttribute(CharTermAttribute.class);
        stream.reset();

        while (stream.incrementToken()) {
        	buff.append(termAtt.toString().toLowerCase()).append(" ");
        }

        stream.end();
        stream.close();
//        System.out.println(buff.toString());
        return buff.toString();
    }
}
