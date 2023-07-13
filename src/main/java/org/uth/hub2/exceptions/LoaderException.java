package org.uth.hub2.exceptions;

/**
 * Loader Exception. This covers exceptions thrown during the load
 * and HTML parse of a bookmark file.
 * @author Ian 'Uther' Lawson
 */
public class LoaderException extends Exception
{
  public LoaderException()
  {
    
  }
  
  public LoaderException( String message )
  {
    super( message );
  }
}