package dissertaion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateQueryPapernumCid {
	Map<String, String> pnumToCid;
	String FILE_PATH;
	
	public CreateQueryPapernumCid(String papernum_cid) throws IOException {
		FILE_PATH = papernum_cid;
		createMap();
	}
	
	public void createMap() throws IOException {
		pnumToCid = new HashMap<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(FILE_PATH)));
		String line;
		while((line=br.readLine())!=null) {
			String[] values = line.split(" ");
			String paperNum = values[0];
			String cid = values[2];
			pnumToCid.put(paperNum, cid);
		}
    }
	
	public Map<String, String> getMap() {
		return pnumToCid;
	}
}
