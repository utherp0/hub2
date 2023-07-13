package org.uth.hub2.tests;

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

public class BookmarkFileToHubTest1
{
  public static void main( String[] args )
  {
    if( args.length != 3 )
    {
      System.out.println( "Usage: java BookmarkFileToHubTest1 target_file index_location terms" );
      System.exit(0);
    }

    new BookmarkFileToHubTest1( args[0], args[1], args[2] );
  }

  public BookmarkFileToHubTest1( String filename, String index, String term )
  {
    // Load the file
    BookmarkExportLoader loader = new BookmarkExportLoader();
    
    long start = System.currentTimeMillis();
    
    try
    {
      loader.loadFile(filename);    
      Map<String,String> links = loader.getLinks();
      long end = System.currentTimeMillis();
      
      System.out.println( "Found " + links.size() + " links in " + ( end - start ) + "ms.");

      // Build the Hub
      Hub hub = new Hub();

      // Add all the bookmarks as entries in the default pool
      for( String link : links.keySet())
      {
        Entry entry = new Entry( new URL(link), links.get(link), null, null, System.currentTimeMillis() );
        hub.addEntry(entry);
      }

      System.out.println( "Hub completed....");

      // Output the Hub contents as Hub Objects
      List<Entry> entries = hub.getAllEntries();

      System.out.println( "Outputting Hub contents....");

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
      
      System.out.println( "Searched for " + term);
      System.out.println( "Search completed in " + (end - start) + "ms.");
      System.out.println( "Search found " + totalHits + " hits.");
      
      ScoreDoc[] hits = results.scoreDocs;

      for( int loop = 0; loop < hits.length; loop++ )
      {
        System.out.println( hits[loop].doc + ":" + hits[loop].score );

        Document doc = searcher.getDoc( hits[loop].doc);
        System.out.println( "Result (" + loop + ")");
        System.out.println( "  " + doc.getField(HubSearcher.URL).stringValue());
        System.out.println( "  - " + doc.getField(HubSearcher.NAME).stringValue());
      }
    }
    catch( Exception exc )
    {
      System.out.println( "Test failed due to " + exc.toString());

      exc.printStackTrace();
    }
  }
}