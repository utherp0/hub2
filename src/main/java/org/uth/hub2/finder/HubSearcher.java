package org.uth.hub2.finder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.uth.hub2.exceptions.HubSearchException;

/**
 * Search utilities for the Lucene Hub Index
 * @author Ian 'Uther' Lawson
 */
public class HubSearcher 
{
  private String _indexLocation = null;
  private IndexReader _reader = null;
  private IndexSearcher _searcher = null;
  private Analyzer _analyzer = null;
  
  private int _maxDocs = 1000;
  
  public final static String UUID = "uuid";
  public final static String NAME = "name";
  public final static String URL = "url";
  public final static String DESCRIPTION = "description";
  
  public HubSearcher( String targetLocation )
  {
    _indexLocation = targetLocation;
  }
  
  public HubSearcher()
  {
    // Use the ENV variable to populate the index location
    // Obtain the index location from an ENV variable
    Map<String,String> envs = System.getenv();
    
    _indexLocation = envs.get("HUBINDEX");  
    
    System.out.println( "HUBINDEX from env: " + _indexLocation );
  }
  
  public void overrideMaxDocs( int maxDocs )
  {
    _maxDocs = maxDocs;
  }
  
  public void initialiseSearcher() throws HubSearchException
  {
    // Initial checks on target index location
    if( _indexLocation == null ) throw new HubSearchException( "Index Location is null.");
        
    final Path hubDir = Paths.get( _indexLocation );

    if( !Files.isReadable(hubDir) ) throw new HubSearchException( "Index Location is unreachable " + _indexLocation );    
    
    try
    {
      _reader = DirectoryReader.open(FSDirectory.open(hubDir));

      // Debug
      System.out.println( "Count of docs in index: " + _reader.numDocs());

      _searcher = new IndexSearcher(_reader);
      _analyzer = new StandardAnalyzer();
    }
    catch( IOException exc )
    {
      throw new HubSearchException( "IO Exception occurred during searcher initialise. " + exc.getMessage());
    }
  }
  
  public void close() throws HubSearchException
  {
    if( _reader == null ) throw new HubSearchException( "HubSearcher not active.");
    
    try
    {
      _reader.close();
    }
    catch( IOException exc )
    {
      throw new HubSearchException( "Exception occurred on close - " + exc.toString());  
    }
  }
  
  /**
   * Simple search method. This applies a search to the target field for the target terms.
   * @param field field of hub documents to search ("uuid", "name", "url", "description" )
   * @param terms terms to search for
   * @return list of top documents (unbounded)
   * @throws HubSearchException if an exception occurs during the search
   */
  public TopDocs search( String field, String terms ) throws HubSearchException
  {
    try
    {
      QueryParser parser = new QueryParser( field, _analyzer );
      Query query = parser.parse(terms);
      
      return _searcher.search(query, _maxDocs );
    }
    catch( Exception exc )
    {
      throw new HubSearchException( "Exception occurred during search - " + exc.getMessage());
    }     
  }
  
  /**
   * Search result document accessor. This has changed in Lucene, now you serve the
   * document from the searcher object.
   * @param docNumber document to return (attained via ScoreDocs)
   * @return the document
   * @throws HubSearchException if the document does not exist or another IOException occurs 
   */
  public Document getDoc( int docNumber ) throws HubSearchException
  {
    try
    {
    
      return _searcher.doc(docNumber);
    }
    catch( IOException exc )
    {
      throw new HubSearchException( "Exception occurred serving document - " + exc.toString());
    }
  }
  
  public void main(String[] args )
  {
    if( args.length != 3 )
    {
      System.out.println( "(Simple Test for HubSearcher)");
      System.out.println( "Usage: java HubSearcher (indexLocation) (searchTerms) (maxDocs)");
      
      System.exit(0);
    }
    
    // Quick number test for HubSearcher
    try
    {
      int testNum = Integer.parseInt( args[2] );
    }
    catch( NumberFormatException exc )
    {
      System.out.println( "Third parameter *must* be an Integer.");
      System.exit(0);
    }
    
    HubSearcher searcher = new HubSearcher( args[0] );
    searcher.overrideMaxDocs( Integer.parseInt( args[2] ));
    
    try
    {    
      searcher.initialiseSearcher();
      
      long start = System.currentTimeMillis();
      TopDocs results = searcher.search( HubSearcher.URL, args[1].toLowerCase());
      long end = System.currentTimeMillis();
      
      int totalHits = Math.toIntExact(results.totalHits.value);
      
      System.out.println( "Searched for " + args[1]);
      System.out.println( "Search completed in " + (end - start) + "ms.");
      System.out.println( "Search found " + totalHits + " hits.");
      
      ScoreDoc[] hits = results.scoreDocs;
      
      for( int hitsLoop = 0; hitsLoop < totalHits; hitsLoop++ )
      {
        Document doc = searcher.getDoc(hitsLoop);
        
        System.out.println("-  " + doc.getField(HubSearcher.URL).stringValue());
      }
    }
    catch(Exception exc )
    {
      System.out.println( "Exception occurred during test - " + exc.toString());
      System.exit(0);
    }
  }
}
