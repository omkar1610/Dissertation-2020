package dissertaion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {
	String doi = "";
    String title = "";
    String abstrct = "";
    String contexts = "";
    File file;
    
//    FileWriter csvWriter;
    
    public XmlParser(File f) {
    	file = f;
    }
	
	public void parseXml() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = dbBuilder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			title = doc.getElementsByTagName("title").item(0).getTextContent();
			doi = doc.getElementsByTagName("doi").item(0).getTextContent();
			abstrct = doc.getElementsByTagName("abstract").item(0).getTextContent();
			NodeList nCitations = doc.getElementsByTagName("citation");
			
			concatCitation(nCitations);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void concatCitation(NodeList nCitations) {
		for (int i = 0; i < nCitations.getLength(); i++) {
			Node nCitation = nCitations.item(i);
			
			if(nCitation.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nCitation;
				String context = eElement.getElementsByTagName("contexts").item(0).getTextContent();
				if(!context.equals("None")) {
					contexts += context;
				}
			}
		}
	}
	
	public static void getCitations(Document doc, FileWriter csvWriter) throws NumberFormatException, IOException {
		NodeList nCitations = doc.getElementsByTagName("citation");
		for (int i = 0; i < nCitations.getLength(); i++) {
			Node nCitation = nCitations.item(i);
			
			if(nCitation.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nCitation;
//				String raw = eElement.getElementsByTagName("raw").item(0).getTextContent();
				String context = eElement.getElementsByTagName("contexts").item(0).getTextContent();
				String clusterId = eElement.getElementsByTagName("clusterid").item(0).getTextContent();

				mergeCitation(Integer.parseInt(clusterId), context, csvWriter);
			}
		}
		
	}
	
	public static void mergeCitation(Integer clusterId, String context, FileWriter csvWriter) throws IOException {
		csvWriter.flush();
		csvWriter.append(clusterId.toString());
		csvWriter.append(",");
		csvWriter.append(context);
		csvWriter.append("\n");
		csvWriter.flush();
//		if(!context.equals("None")) {
//			if(clusteridToContext.get(clusterId)!=null) {
//				clusteridToContext.get(clusterId).add(context);
//			}
//			else {
//				ArrayList<String> tmp = new ArrayList<String>();
//				tmp.add(context);
//				clusteridToContext.put(clusterId, tmp);
//			}
//		}
	}
}
