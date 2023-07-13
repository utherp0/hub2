package org.uth.hub2.exceptions;

/**
 * Hub Index Exception. This is an exception thrown if any issues are
 * encountered when using the Lucene Hub Index
 * @author Ian 'Uther' Lawson
 */
public class HubIndexException extends Exception
{
  public HubIndexException()
  {
    
  }
  
  public HubIndexException( String message )
  {
    super( message );
  }
}