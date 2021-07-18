package dissertaion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateDoiCidMap {
	Map<String, String> doiToCid;
	String CLUSTERID_DOI;
	
	public CreateDoiCidMap(String clusterid_doi_path) throws IOException {
		CLUSTERID_DOI = clusterid_doi_path;
		createClusterIdFromDoi();
	}
	
	public void createClusterIdFromDoi() throws IOException {
    	String doi = "";
		String clusterId = "";
		doiToCid = new HashMap<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(CLUSTERID_DOI)));
		String line;
		while((line=br.readLine())!=null) {
			if(line.charAt(line.length()-1)==':') {
				clusterId = line.substring(0, line.length()-1);
			}
			else {
				doi = line.trim();
				doiToCid.put(doi, clusterId);
			}			
		}
    }
	
	public Map<String, String> getMap() {
		return doiToCid;
	}
}
