package org.uth.hub2;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.List;
import java.net.URL;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import org.uth.hub2.currency.Entry;
import org.uth.hub2.currency.Hub;
import org.uth.hub2.finder.HubIndexer;
import org.uth.hub2.finder.HubSearcher;
import org.uth.hub2.utils.BookmarkExportLoader;

/**
 * Test to see if I can add the extensive tests to Quarkus RESTful endpoints (for
 * eventual Containerisation)
 * @author uther
 */
@Path("/hub2tests")
public class Hub2Tests
{
  @GET
  @Path("/hubindextest")
  @Produces(MediaType.TEXT_PLAIN)
  public String performTest(@QueryParam("filename") String filename, 
                            @QueryParam("index") String index,
                            @QueryParam("term") String term )
  {
      // Load the file
    BookmarkExportLoader loader = new BookmarkExportLoader();
    
    long start = System.currentTimeMillis();
    
    StringBuilder output = new StringBuilder();
    
    try
    {
      loader.loadFile(filename);    
      Map<String,String> links = loader.getLinks();
      long end = System.currentTimeMillis();
      
      output.append("Found " + links.size() + " links in " + ( end - start ) + "ms.\n");

      // Build the Hub
      Hub hub = new Hub();

      // Add all the bookmarks as entries in the default pool
      for( String link : links.keySet())
      {
        Entry entry = new Entry( new URL(link), links.get(link), null, null, System.currentTimeMillis() );
        hub.addEntry(entry);
      }

      output.append("Hub completed...\n");

      // Output the Hub contents as Hub Objects
      List<Entry> entries = hub.getAllEntries();

      output.append("Outputting Hub contents...\n");
      
      // Creating the Lucene Index
      HubIndexer indexer = new HubIndexer( index );
      indexer.index(hub, true);

      // Run a simple search for the terms provided
      HubSearcher searcher = new HubSearcher( index );
      searcher.initialiseSearcher();

      start = System.currentTimeMillis();
      TopDocs results = searcher.search( HubSearcher.NAME, term.toLowerCase());
      end = System.currentTimeMillis();
      
      int totalHits = Math.toIntExact(results.totalHits.value);
      
      output.append("Searched for " + term + "\n");
      output.append("Search completed in " + (end - start) + "ms.\n");
      output.append("Search found " + totalHits + " hits.\n");
      
      ScoreDoc[] hits = results.scoreDocs;

      for( int loop = 0; loop < hits.length; loop++ )
      {
        output.append( hits[loop].doc + ":" + hits[loop].score + "\n");

        Document doc = searcher.getDoc( hits[loop].doc);
        output.append("Result (" + loop + ")\n");
        output.append("  " + doc.getField(HubSearcher.URL).stringValue() + "\n");
        output.append("  - " + doc.getField(HubSearcher.NAME).stringValue() + "\n");
      }
    }
    catch( Exception exc )
    {
      output.append("Test failed due to " + exc.toString() + "\n");

      exc.printStackTrace();
    }
    
    return output.toString();
  }
}
