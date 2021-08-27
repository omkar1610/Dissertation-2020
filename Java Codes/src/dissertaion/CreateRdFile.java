package dissertaion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CreateRdFile {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
//		-------------------------Get File Count-----------------------------------------------------
		File directory=new File("D:\\develop\\Dissertation-2020\\data\\citeseerx-partial-papers");
	    int fileCount=directory.list().length;
//	    There are 630,199 XMLs files
	    System.out.println("Total XML files: "+fileCount);

//		-------------------------Read each XML file and get Cluster_id and context-----------------------------------------------------
	    File[] files = directory.listFiles();
	    Integer count=0;
	    for(File file:files) {
	    	getDataFromXml(file.getAbsolutePath().toString());
	    	count++;
	    	if(count%1000==0) {
	    		System.out.println("Files completed: " + count + " out of " + fileCount);
	    	}
	    }
	    
//	    time started = 1:07PM
//	    time = 5:40PM, completed = 516000
//	    time = 5:48PM, completed = 630000
//	    Total cluster id = 2223308
//	    removed 0 file as it was 350MB. Files now = 2223307
	    directory=new File("D:\\develop\\Dissertation-2020\\data\\file_for_each_cluster_id");
	    fileCount=directory.list().length;
	    System.out.println("Total cluster ids: "+fileCount);


//		-------------------------Create merged file from each of those cluster_id files-----------------------------------------------------

	    BufferedWriter out = new BufferedWriter(new FileWriter("D:\\develop\\Dissertation-2020\\data\\rd_file", true));
	    String[] files = directory.list();
	    Integer count=0;
	    for(String fileName:files) {
	    	String line = fileName + "," + getLine("D:\\develop\\Dissertation-2020\\data\\file_for_each_cluster_id\\" + fileName) + "\n";
	    	out.write(line);
	    	count++;
	    	if(count%10000==0) {
	    		System.out.println("Cluster IDs completed: " + count + " out of " + fileCount);
	    	}
	    }
	    out.close();

//	    time started = 7:12PM
//	    time = 11:00PM, completed = 1,130,000
//	    finished = 1:40AM
//	    Total lines = 2,223,307

	    System.out.println("Total lines in rd_file: " + getTotLine("D:\\develop\\Dissertation-2020\\data\\rd_file"));
	    
	}
	
	public static void getDataFromXml(String fileName) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document doc = dbBuilder.parse(fileName);
		doc.getDocumentElement().normalize();
		
		NodeList nCitations = doc.getElementsByTagName("citation");
		for (int i = 0; i < nCitations.getLength(); i++) {
			Node nCitation = nCitations.item(i);
			
			if(nCitation.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nCitation;
				String clusterId = eElement.getElementsByTagName("clusterid").item(0).getTextContent();
				String context = eElement.getElementsByTagName("contexts").item(0).getTextContent().replace("\n", " ").replaceAll("\\p{Punct}", "").replaceAll(" +[a-zA-Z] +", " ");
				if(!context.equals("None")) {
					BufferedWriter out = new BufferedWriter(new FileWriter("D:\\develop\\Dissertation-2020\\data\\file_for_each_cluster_id\\" + clusterId, true));
					out.write(context);
					out.close();
				}
			}
		}
	}
	
	public static String getLine(String fileName) {
		String line="";
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			Integer count = 0;
			while (myReader.hasNextLine()) {
				count++;
				line = myReader.nextLine();
			}
			myReader.close();
			if(count>1) {
				System.out.println(fileName + ": This file has more than 1 line");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return line;
	}

	public static Integer getTotLine(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		Integer count = 0;
		while (reader.readLine() != null) {
			count++;
			if(count%100000==0) {
	    		System.out.println("Lines completed: " + count);
	    	}
		}
		reader.close();
		return count;
	}
}

