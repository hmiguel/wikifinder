package pt.uc.dei.ia.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pt.uc.dei.ia.jena.TripleStoreReader;
import pt.uc.dei.ia.lucene.IndexBuilder;
import pt.uc.dei.ia.models.Article;

public class Lucene {

	private static StandardAnalyzer analyzer;
	private static File index;
	private static File triples;

	public static boolean CreateIndexFromTripleStore(File index, File triple) {

		if(index.isDirectory()){
			 
			if(index.list().length>0){
				// Index Directory must be empty!
				return false;
			}
		}
				
		try {
			// INDEX WRITER CONFIG
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,analyzer);

			// INDEX WRITER CONFIG
			IndexWriter w = new IndexWriter(FSDirectory.open(index), config);
			
			// ONTOLOGY READER
			List<Article> articles = TripleStoreReader.getArticles();

			
			// #ARTICLES
			System.out.println("SIZE: " +  articles.size());
			
			
			// FOR EACH ARTICLE -> CREATE INDEX DOCUMENT
			for (Article article: articles){
				IndexBuilder.addDoc(w, article);
			}

			w.close(); // Close Writer

		} catch (Exception e) {

			return false;
		}

		return true;
	}
	
	
	public JSONObject QuerySearch(String querystr) {
		
		JSONObject results = new JSONObject();
		
		try{
			
			// the "title" arg specifies the default field to use	// when no field is explicitly specified in the query.
			Query q = new QueryParser(Version.LUCENE_40, "abstract", analyzer)
					.parse(querystr);
	
			// SEARCH
			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(FSDirectory.open(index)); // READ	
			IndexSearcher searcher = new IndexSearcher(reader);
	
			// COLLECTOR
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					hitsPerPage, true);
			searcher.search(q, collector);
	
			// SCORE DOCS
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
	
			// BUILD RESPONSE
			results.put("query", querystr); 	// Add Query to Json Result
			results.put("hits", hits.length); 	// Add Hits to Json Result
			
			// Create <Article> JsonArray
			JSONArray data = new JSONArray();
		
			// for each hit
			for (int i = 0; i < hits.length; ++i) {
				
				int docId = hits[i].doc;
	
				Document d = searcher.doc(docId);
			
				JSONObject article = new JSONObject();
				
				article.put("id", docId); // article Id
				article.put("label", d.get("label")); // label
				article.put("abstract", d.get("abstract")); // abstract 
				article.put("resource", d.get("resource")); // resource
				
				// PRINT ALL FIELDS OF DOC
				//d.getFields()
				
				// TOPICS
				JSONArray topics = new JSONArray();
				IndexableField[] index_top = d.getFields("topic"); 
				for (int j = 0;j < index_top.length; j++){
					topics.put(index_top[j].stringValue());
				}
				article.put("topics", topics);
				
				// CATEGORIES
				JSONArray cat = new JSONArray();
				IndexableField[] index_cat = d.getFields("category");
				for (int j = 0;j < index_cat.length; j++){
					cat.put(index_cat[j].stringValue());
				}
				article.put("categories", cat);		
				
				data.put(i, article); // Add Article to JsonArray
	
			}
			
			results.put("data", data); // Add JsonArray to Json Result
	
			reader.close();
			
		}catch(Exception e){
			
			return new JSONObject();
			
		}
		
		return results;

	}
	
	public void init(){
		
	//	System.out.println("Lucene Starting...");
		
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
		index = new File("/media/Media/wikipedia/index/search");
		triples = new File("/media/Media/wikipedia/ontology/wikipedia.owl");
		
	}

	public void buildIndex(){
		
		System.out.print("Creating Index...");
		
		if (!CreateIndexFromTripleStore(index, triples)) { // FIRST TIME
			System.out.println("ERROR");
			return;
		}else{
			System.out.println("OK");
		}
		
	}
	
	
	public Lucene() {

		init();

	}

}