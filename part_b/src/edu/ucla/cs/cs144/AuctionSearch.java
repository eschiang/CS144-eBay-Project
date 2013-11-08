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

import java.util.*;
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
                        // Access the lucene index
                        searcher = new IndexSearcher(System.getenv("LUCENE_INDEX"));
                        parser = new QueryParser("content", new StandardAnalyzer());

                        Query q = parser.parse(query);        
                        Hits hits = searcher.search(q);

                        // Allocate the results array
                        if (numResultsToReturn != 0 && numResultsToReturn - numResultsToSkip < hits.length())
                                resultstore = new SearchResult[numResultsToReturn];
                        else
                                resultstore = new SearchResult[hits.length()];
                        
                        // Iterators for the hits(matches found)
                        // And for resultsStore to return
                        Iterator<Hit> it =  hits.iterator();
                        int i = 0;
                        int resultIndex = 0;
                        
                        // Following Code will try to find the hits.
                        // stores hits in resultstore
                        while(it.hasNext() && (numResultsToReturn == 0 || (i < hits.length() && i - numResultsToSkip < numResultsToReturn)))
                        {
                                if (i > numResultsToSkip - 1) {
                                        Hit hit = it.next();
                                        Document doc = hit.getDocument();
                                        resultstore[resultIndex] = new SearchResult(doc.get("id"), doc.get("name"));
                                        resultIndex++;
                                }
                                
                                i++;
                        }
                } 
                catch (Exception e) 
                {
                        System.out.println(e);
                        
                }
                
                return resultstore;
	}

        /* Returns the date from the xml files' format (Dec-04-01 04:03:12)
         * converted to MySQL timestamp readable format.
         */
        static String convertDate(String dateString) {
                String old_format = "MMM-dd-yy HH:mm:ss";
                String new_format = "yyyy-MM-dd HH:mm:ss";

                SimpleDateFormat sdf = new SimpleDateFormat(old_format);
                String out = "";
                try {
                        Date d = sdf.parse(dateString);
                        sdf.applyPattern(new_format);
                        out = sdf.format(d);
                } catch (Exception e) {
                        System.out.println("Could not format date");
                }

                return out;
        }

        // Build the lucene query from the given constraints
        public String buildLuceneQuery(SearchConstraint[] constraints) {
                // Add queries to a list
                Stack<String> fieldQueries = new Stack();

                // Iterate over constraints and push if 
                // on a field that is indexed by lucene
                int numConstraints = constraints.length;
                for(int i = 0; i < numConstraints; i++) {
                        // Pull out Lucene indexed constraints
                        if(constraints[i].getFieldName().equals(FieldName.ItemName))
                                fieldQueries.push("name:" + constraints[i].getValue());
                       
                        else if(constraints[i].getFieldName().equals(FieldName.Category))
                                fieldQueries.push("category:" + constraints[i].getValue());
                                
                        else if(constraints[i].getFieldName().equals(FieldName.Description))
                                fieldQueries.push("description:" + constraints[i].getValue());
                        
                }

                // Build final query String
                StringBuilder query = new StringBuilder();
                if(!fieldQueries.empty()) {
                        query.append(fieldQueries.pop());
                        while(!fieldQueries.empty()) {
                                String next = fieldQueries.pop();
                                query.append(" AND ");
                                query.append(next);
                        }
                }

                return query.toString();
        }

        public String buildSqlQuery(SearchConstraint[] constraints) {
                
                Stack<String> whereClauses = new Stack();
                // Iterate over constraints and push if
                // on a field that are indexed by lucene
                int numConstraints = constraints.length;
                boolean bidJoin = false;
                for(int i = 0; i < numConstraints; i++) {

                        // Pull out MySQL indexed constraints
                        if(constraints[i].getFieldName().equals(FieldName.SellerId))
                                whereClauses.push("Seller = '" + constraints[i].getValue() + "'");
                       
                        else if(constraints[i].getFieldName().equals(FieldName.BuyPrice))
                                whereClauses.push("Buy_Price = " + constraints[i].getValue());
                                
                        else if(constraints[i].getFieldName().equals(FieldName.EndTime))
                                whereClauses.push("Ends = \"" + convertDate(constraints[i].getValue()) + "\"");

                        else if(constraints[i].getFieldName().equals(FieldName.BidderId)) {
                                bidJoin = true;
                                whereClauses.push("UserID = '" + constraints[i].getValue() + "'");
                        }
                        
                }

                StringBuilder query = new StringBuilder();      
                if(!whereClauses.empty()) {
                        query.append("SELECT ItemID, Name FROM Item ");
                        if (bidJoin)
                                query.append("INNER JOIN Bid ON Item.ItemID = Bid.ItemID ");

                        query.append("WHERE " + whereClauses.pop() + " ");

                        while(!whereClauses.empty()) {
                                query.append("AND " + whereClauses.pop() + " ");
                        }
                }
                System.out.println(query.toString());
                return query.toString();
        }

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
                
                // Handle Lucene querying
                Map<String, String> luceneResult = new HashMap<String, String>();
		try {
                        String luceneQuery = buildLuceneQuery(constraints);

                        if(!luceneQuery.isEmpty()) {
                                // Access the lucene index 
                                // Query Parser has no default field because 
                                // query will specify fields.
                                searcher = new IndexSearcher(System.getenv("LUCENE_INDEX"));
                                parser = new QueryParser("content", new StandardAnalyzer());

                                // Execute Query
                                Query q = parser.parse(luceneQuery);        
                                Hits hits = searcher.search(q);

                                Iterator<Hit> it = hits.iterator();

                                // Place all the itemIDs a set for quick intersection later
                                while(it.hasNext()) {
                                        Hit hit = it.next();
                                        Document doc = hit.getDocument();
                                        luceneResult.put(doc.get("id"), doc.get("name"));
                                }
                        }

                } catch (Exception e) {
                        System.out.println(e);
                }
                
                // Handle SQL querying
                Map<String, String> sqlResult = new HashMap<String, String>();
                Connection conn = null;
                try {

                        String sqlQuery = buildSqlQuery(constraints);

                        conn = DbManager.getConnection(true);
                        Statement stmt = conn.createStatement();

                        // Execute query
                        ResultSet items = stmt.executeQuery(sqlQuery);

                        
                        if(!sqlQuery.isEmpty())
                        {
                                
                                while(items.next()) {
                                        sqlResult.put(items.getString("ItemID"), items.getString("Name"));         
                                }
                        }
                }
                catch (Exception e) {
                        System.out.println(e);
                }

                // Create the final SearchResult
                // Determine if we must do an intersection 
                Set<String> itemIds;
                Map<String, String> result;
                if(sqlResult.isEmpty()) {
                        itemIds = luceneResult.keySet();
                        result = luceneResult;
                } else if(luceneResult.isEmpty()) {
                        itemIds = sqlResult.keySet();
                        result = sqlResult;
                } else {
                        itemIds = luceneResult.keySet();
                        itemIds.retainAll(sqlResult.keySet());
                        result = luceneResult;
                }

                // Add the keys and itemIds to the SearchResult array
                SearchResult[] resultstore = new SearchResult[itemIds.size()];

                Iterator<String> i = itemIds.iterator();
                int resultIndex = 0;

                while(i.hasNext()) {
                        String itemId = i.next();
                        resultstore[resultIndex] = new SearchResult(itemId, result.get(itemId));
                        resultIndex++;
                }

		return resultstore;
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return null;
	}
	
	public String echo(String message) {
		return message;
	}

}

