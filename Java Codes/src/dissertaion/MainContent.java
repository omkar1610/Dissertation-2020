package dissertaion;

import java.util.HashMap;
import java.util.Map;

public class MainContent{
	public static void main(String[] args) throws Exception {
		Map<String, String> FILE_MAP = new HashMap<String, String>();

		String DISSERTATION_DATA = "D:\\develop\\Dissertation-2020\\data\\";
		
		FILE_MAP.put("COLLECTION_PATH", DISSERTATION_DATA + "citeseerx-partial-papers");
		FILE_MAP.put("CLUSTERID_DOI", DISSERTATION_DATA + "clustersIdToDOI.txt");
		FILE_MAP.put("STOPWORDS_FILE", DISSERTATION_DATA + "smart-stopwords");
		
		FILE_MAP.put("INDEX_PATH_CONTENT", DISSERTATION_DATA + "index\\content_index");
		FILE_MAP.put("MERGED_FILE_CONTENT", DISSERTATION_DATA + "merged_content_list");
		
//		FILE_MAP.put("QUERY_FILE", DISSERTATION_DATA + "query\\v1\\queries.v1.txt");
//		FILE_MAP.put("RESULT_FILE", DISSERTATION_DATA + "result\\result_content_v1");
		FILE_MAP.put("QUERY_FILE", DISSERTATION_DATA + "query\\v2\\query.v2.txt");
		FILE_MAP.put("RESULT_FILE", DISSERTATION_DATA + "result\\result_content_v2");
		FILE_MAP.put("USE_QUERY_TITLE", "false");
		FILE_MAP.put("USE_QUERY_ABSTRACT", "false");
		

//		27 mins to index
//		Indexer indexer = new Indexer(FILE_MAP);
//		indexer.indexFromMergedContent();
		
		Searcher searcher = new Searcher(FILE_MAP);
		searcher.search(FILE_MAP.get("INDEX_PATH_CONTENT"), FILE_MAP.get("RESULT_FILE"));
		System.out.println("Done");
		//TO-DO
		// Check file name and location names before starting indexing and starting writing result
		// If folder is non empty ask yes/no

	}
}

