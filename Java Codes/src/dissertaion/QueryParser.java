package dissertaion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class QueryParser {
	String paper_num, paper_title, paper_abstract;
	String query_num, context;
	String USE_QUERY_TITLE;
	String USE_QUERY_ABSTRACT;
	
	List<SingleQuery> queries;
	File file;
	
	public QueryParser(String queryPath, String use_title, String use_abstrct) {
		queries = new ArrayList<SingleQuery>();
		file = new File(queryPath);
		USE_QUERY_TITLE = use_title;
		USE_QUERY_ABSTRACT = use_abstrct;
		
		
	}
	
	public List<SingleQuery> getQueriesList() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = dbBuilder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			NodeList pList = doc.getElementsByTagName("top");
			for (int i = 0; i < pList.getLength(); i++) {
				
				Node page = pList.item(i);
				Element elePage = (Element)page;
				paper_num = elePage.getElementsByTagName("paper_num").item(0).getTextContent();
				if(USE_QUERY_TITLE.equals("true"))
					paper_title = elePage.getElementsByTagName("paper_title").item(0).getTextContent();
				else
					paper_title = "";
				if(USE_QUERY_ABSTRACT.equals("true"))
					paper_abstract = elePage.getElementsByTagName("paper_abstract").item(0).getTextContent();
				else
					paper_abstract = "";
				
				NodeList nQuerieNums = elePage.getElementsByTagName("query_num");
				NodeList nTexts = elePage.getElementsByTagName("text");
				
				for (int j = 0; j < nQuerieNums.getLength(); j++) {
					query_num = nQuerieNums.item(j).getTextContent();
					context = nTexts.item(j).getTextContent().replace("\n", " ").replaceAll("\\p{Punct}", "").replaceAll(" +[a-zA-Z\\d] +", " ");
					
					SingleQuery query = new SingleQuery();
					query.paperNum = Integer.parseInt(paper_num.trim());
					query.paperTitle = paper_title;
					query.paperAbstract = paper_abstract;
					query.queryNum = Integer.parseInt(query_num.trim());
					query.context = context;
					queries.add(query);
					
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return queries;
	}
}

//14.139.222.93
//12ta gnat proshno