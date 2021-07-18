package dissertaion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateContentFile {
	static Map<String, String> doiToCid;
	static Map<String, Boolean> uniqCid;
    static XmlParser xmlParser;
    static String DISSERTATION_DATA = "D:\\develop\\Dissertation-2020\\data\\";
    static String DOC_TO_CID_FILE = DISSERTATION_DATA + "clustersIdToDOI.txt";
    static String COLLECTION_PATH = DISSERTATION_DATA + "citeseerx-partial-papers";
    static String CLUSTERID_FILE = DISSERTATION_DATA + "clusterIds";
    static String MERGE_FILE = DISSERTATION_DATA + "merged_content_list";
    static Integer count, tmp;
    static Date init;
    
    
	public static void main(String[] args) throws IOException {
		doiToCid = new CreateDoiCidMap(DOC_TO_CID_FILE).getMap();
		File[] files = new File(COLLECTION_PATH).listFiles();
		count = 0;
		tmp = 0;
		init = new Date();
		System.out.println("Starting...");
 	    for (File file : files) {
 	    	xmlParser = new XmlParser(file);
         	xmlParser.parseXml();
 	    	saveFieldToFile(xmlParser);
 	    	count++; tmp++;
 	    	if(tmp==1000) {
 	    		tmp=0;
 	    		System.out.println(count + " files done - " + (new Date().getTime() - init.getTime())/1000 + " secs passed.");
 	    	}
 	    }
//	    This created 1407926 files - 469308x3 title+abstrct+context - one clusters file - one merged file
 	    
	    System.out.println("Individual files Done.");
	    getUniqueCid();
//	    Total files = 1260399, total unique clusters = 469308
		System.out.println("Merging Started...");
		
	    count = 0;
	    tmp = 0;
	    init = new Date();
	    mergeFiles();
	}
	
	public static void saveFieldToFile(XmlParser xmlParser) throws IOException {
        
        String cluster_id = doiToCid.get(xmlParser.doi);

        writeFile(CLUSTERID_FILE, cluster_id + "\n");
        writeFile(DISSERTATION_DATA + "content_files\\" + cluster_id + "-title", xmlParser.title + " ");
        writeFile(DISSERTATION_DATA + "content_files\\" + cluster_id + "-abstrct", xmlParser.abstrct + " ");
        writeFile(DISSERTATION_DATA + "content_files\\" + cluster_id + "-contexts", xmlParser.contexts + " ");
	}
	
	public static void writeFile(String fileName, String text) throws IOException {
		FileWriter myWriter = new FileWriter(fileName, true);
	    myWriter.write(text);
	    myWriter.close();
	}
	
	public static void getUniqueCid() throws IOException {
		uniqCid = new HashMap<String, Boolean>();
	    BufferedReader br=new BufferedReader(new FileReader(new File(CLUSTERID_FILE)));
		String cluster_id;
		count = 0;
		tmp = 0;
		while((cluster_id=br.readLine())!=null) {
			count++;
			if(!uniqCid.containsKey(cluster_id)){
				tmp++;
				uniqCid.put(cluster_id, true);
			}
		}
		br.close();
		System.out.println("Total files = " + count + ", total unique clusters = " + tmp);
	}
	
	public static void mergeFiles() throws IOException {
		FileWriter myWriter = new FileWriter(MERGE_FILE, true);
		for(String cluster_id: uniqCid.keySet()) {
			String title = readFile(cluster_id, "title").replace("\n", " ").replaceAll("\\p{Punct}", "").replaceAll(" +[a-zA-Z] +", " ");
			String abstrct = readFile(cluster_id, "abstrct").replace("\n", " ").replaceAll("\\p{Punct}", "").replaceAll(" +[a-zA-Z] +", " ");
			String contexts = readFile(cluster_id, "contexts").replace("\n", " ").replaceAll("\\p{Punct}", "").replaceAll(" +[a-zA-Z] +", " ");
			myWriter.write(cluster_id + "\n");
			myWriter.write(title + "\n");
			myWriter.write(abstrct + "\n");
			myWriter.write(contexts + "\n");
			
			count++;
			tmp++;
//			System.out.println(count + "cluster IDs done.");
			if(tmp==1000) {
	    		tmp=0;
	    		System.out.println(count + " clusterIds done - " + (new Date().getTime() - init.getTime())/1000 + " secs passed.");
			}
		}
		
		myWriter.close();
	}
	
	public static String readFile(String cluster_id, String field) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(new File(DISSERTATION_DATA + "content_files\\" + cluster_id + "-" + field)));
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		while((line=br.readLine())!=null) {
			stringBuilder.append(line);
			stringBuilder.append(" ");
		}
		br.close();
		return stringBuilder.toString();
	}
	
	
}
