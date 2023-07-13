package org.uth.hub2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * File access utilities.
 * @author Ian Lawson
 */
public class FileUtils
{
  private FileUtils()
  {
    
  }
  
  public static String loadString( String fileName )
  {
    try
    {
      File targetLocation = new File( fileName );
      FileInputStream inputStream = new FileInputStream(targetLocation);

      Scanner scanner = new Scanner(inputStream);

      StringBuilder buffer = new StringBuilder();
      
      //first use a Scanner to get each line
      while( scanner.hasNextLine() )
      {
        String dataComponent = scanner.nextLine();
        
        buffer.append(dataComponent);
      }
      
      inputStream.close();
      return buffer.toString();
    }
    catch( IOException exc )
    {
      System.err.println( "File read exception occured attempting to read " + fileName + " " + exc.toString());
      
      return null;
    }    
  }
}
