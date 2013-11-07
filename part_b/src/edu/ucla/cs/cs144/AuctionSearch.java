package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {
	IndexSearcher searcher;
	QueryParser parser;
	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
         * Your code will need to reference the directory which contains your
	 * Lucene index files.  Make sure to read the environment variable 
         * $LUCENE_INDEX with System.getenv() to build the appropriate path.
	 *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		  SearchResult[] resultstore = new SearchResult[0];

                try 
                {
                         searcher = new IndexSearcher(System.getenv("LUCENE_INDEX"));
                         parser = new QueryParser("content", new StandardAnalyzer());

                        Query q = parser.parse(query);        
                        Hits hits = searcher.search(q);

                        // Allocate the results array
                        if (numResultsToReturn != 0 && numResultsToReturn - numResultsToSkip < hits.length())
                                resultstore = new SearchResult[numResultsToReturn];
                        else
                                resultstore = new SearchResult[hits.length()];
                        
                        //Iterator for the hits(matches found)
                        Iterator<Hit> it =  hits.iterator();
                        int i= 0;
                        
                        // Following Code will try to find the hits.
                        //stores hits in resultstore
                        while(it.hasNext() && (numResultsToReturn == 0 || (i < hits.length() && i - numResultsToSkip < numResultsToReturn)))
                        {
                                if (i > numResultsToSkip - 1) {
                                        Hit hit = it.next();
                                        Document doc = hit.getDocument();
                                        resultstore[i] = new SearchResult(doc.get("id"), doc.get("name"));
                                }
                                
                                i++;
                        }
                } 
                catch (Exception e) 
                {
                        System.out.println("xyz");
                        
                }
                
                return resultstore;
		// TODO: Your code here!
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return null;
	}
	
	public String echo(String message) {
		return message;
	}

}

