package org.uth.hub2.tests;

import java.util.ArrayList;
import java.util.List;
import org.uth.hub2.currency.Entry;
import org.uth.hub2.currency.Hub;
import org.uth.hub2.finder.FastFind;
import org.uth.hub2.utils.BookmarkExportLoader;

/**
 * Functionality tests of the FastFind object.
 * @author Ian Lawson
 */
public class FastFindTest1 
{
  public static void main( String[] args )
  {
    if( args.length != 1 )
    {
      System.out.println( "Usage: java FastFindTest exampleHTMLFile" );
      System.exit(0);
    }
    
    new FastFindTest1( args[0] );
  }
  
  public FastFindTest1( String fileLocation )
  {
    // Setup test data
    Hub hub = new Hub();

    BookmarkExportLoader loader = new BookmarkExportLoader();
    
    try
    {
      long start = System.currentTimeMillis();
      
      loader.loadFile(fileLocation);
      
      int linkCount = 0;
      for( Entry entry : loader.getLinksAsEntries(fileLocation))
      {
        hub.addEntry(entry);
        linkCount++;
      }
      
      long end = System.currentTimeMillis();
      
      System.out.println( "Loaded " + linkCount + " links into Hub in " + ( end - start ) + "ms." );
      
      List<String> tokens = new ArrayList<>();
      
      // Simple search tests
      tokens.add("openshift");
      tokens.add("roadmap");
      
      FastFindTest1.performSearch("name", tokens, hub, true);
      FastFindTest1.performSearch("name", tokens, hub, false);      
    }
    catch( Exception exc )
    {
      System.out.println( "Unable to load file due to " + exc.toString());
    }
  }
  
  public static void performSearch( String field, List<String> tokens, Hub hub, boolean and )
  {
    FastFind finder = new FastFind();
    
    StringBuilder tokensDisplay = new StringBuilder( "[ " );
    for( String token : tokens ) tokensDisplay.append( token + " " );
    tokensDisplay.append( "]" );
    
    System.out.println( "Searching in " + field + " for " + tokensDisplay.toString() + " with logic '" + ( and ? "AND" : "OR" ) + "'" );
    System.out.println( "Hub contains " + hub.getAllEntries().size());
    
    long start = System.currentTimeMillis();
    
    List<Entry> results = finder.simpleSearch(field, tokens, and, hub);
    
    long end = System.currentTimeMillis();
    
    System.out.println( "Found " + results.size() + " results in " + (end - start) + "ms." );
    
    for( Entry entry : results )
    {
      System.out.println( "  " + entry.getName() + " : " + entry.getURL().toString());
    }
  }
}
