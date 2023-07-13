package org.uth.hub2.exceptions;

/**
 * Hub Search Exception. This is an exception thrown if any issues are
 * encountered when using the Lucene Hub Index for searching
 * @author Ian 'Uther' Lawson
 */
public class HubSearchException extends Exception
{
  public HubSearchException()
  {
    
  }
  
  public HubSearchException( String message )
  {
    super( message );
  }
}