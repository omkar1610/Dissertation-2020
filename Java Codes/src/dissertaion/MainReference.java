package dissertaion;

import java.util.HashMap;
import java.util.Map;

public class MainReference {
	public static void main(String[] args) throws Exception {
		Map<String, String> FILE_MAP = new HashMap<String, String>();
		String PATH_PREFIX = "D:\\develop\\eclipse-workspace\\omkarProject\\files\\";
		String DISSERTATION_DATA = "D:\\develop\\Dissertation-2020\\data\\";

		FILE_MAP.put("COLLECTION_PATH", DISSERTATION_DATA + "citeseerx-partial-papers");
		FILE_MAP.put("CLUSTERID_DOI", PATH_PREFIX + "clustersIdToDOI.txt");
		FILE_MAP.put("STOPWORDS_FILE", DISSERTATION_DATA + "smart-stopwords");
		
		FILE_MAP.put("INDEX_PATH_REFERENCE", DISSERTATION_DATA + "index\\reference_index_rd_file");
		FILE_MAP.put("MERGED_FILE_REFERENCE", DISSERTATION_DATA + "rd_file");
		
		FILE_MAP.put("QUERY_FILE", DISSERTATION_DATA + "query\\v1\\queries.v1.txt");
//		FILE_MAP.put("QUERY_FILE", DISSERTATION_DATA + "query\\v2\\query.v2.txt");
		FILE_MAP.put("RESULT_FILE_REFERENCE", DISSERTATION_DATA + "result\\result_reference_rd_file_v1_remove_original_paper");
//		FILE_MAP.put("RESULT_FILE_REFERENCE", DISSERTATION_DATA + "result\\result_reference_rd_file_v2");
		FILE_MAP.put("USE_QUERY_TITLE", "false");
		FILE_MAP.put("USE_QUERY_ABSTRACT", "false");
		
//		Indexer indexer = new Indexer(FILE_MAP);
//		indexer.indexFromMergedReference();
		
		Searcher searcher = new Searcher(FILE_MAP);
		searcher.search(FILE_MAP.get("INDEX_PATH_REFERENCE"), FILE_MAP.get("RESULT_FILE_REFERENCE"));
		System.out.println("Done");
		//TO-DO
		// Check file name and location names before starting index and starting writing result
		// If folder is non empty ask yes/no

	}
}
