package org.uth.hub2.finder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.uth.hub2.currency.Entry;
import org.uth.hub2.currency.Hub;
import org.uth.hub2.exceptions.HubIndexException;

/**
 * Lucene Indexer Utilities specifically for Hub data.
 * @author Ian 'Uther' Lawson
 */
public class HubIndexer 
{
  private String _indexLocation = null;
  
  public HubIndexer( String targetLocation )
  {
    _indexLocation = targetLocation;
  }
  
  public HubIndexer()
  {
    // Obtain the index location from an ENV variable
    Map<String,String> envs = System.getenv();
    
    _indexLocation = envs.get("HUBINDEX");
  }
  
  /**
   * Index method. For simplicity this performs an entire re-index.
   * @param hub complete hub to index
   * @param create mode of index. If true any existing entries are removed, if false
   * the indexer replaces any existing docs based on uuid
   * @throws HubIndexException if an issue occurs when interacting with the index
   */
  public void index( Hub hub, boolean create ) throws HubIndexException
  {
    // Config health checks
    if( _indexLocation == null ) throw new HubIndexException( "Index Location is null, check config or HUBINDEX env.");
    
    final Path hubDir = Paths.get( _indexLocation );

    if( !Files.isReadable(hubDir) ) throw new HubIndexException( "Index Location is unreachable " + _indexLocation );    
    
    try
    {
      Directory dir = FSDirectory.open(hubDir);
      Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);    
      
      // If create mode set then switch index to creation (wipe all existing docs)
      iwc.setOpenMode( ( create ? OpenMode.CREATE : OpenMode.CREATE_OR_APPEND));
      
      IndexWriter writer = new IndexWriter(dir, iwc);
      
      for( Entry entry : hub.getAllEntries())
      {
        if( create )
        {
          writer.addDocument( entry.toDocument() );
        }
        else
        {
          // Overwrite any doc with the same uuid
          writer.updateDocument(new Term("uuid", entry.getID()), entry.toDocument());
        }
      }
      
      writer.close();
    }
    catch( IOException exc )
    {
      throw new HubIndexException( "IOException occurred during index operation " + exc.getMessage());
    }
  }  
}
