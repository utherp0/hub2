package org.uth.hub2.tests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.uth.hub2.utils.FileUtils;

/**
 * Initial test of JSoup parser.
 * @author Ian Lawson
 */
public class JSoupTest1 
{
  public static void main( String[] args )
  {
    if( args.length != 1 )
    {
      System.out.println( "Usage: java JSoupTest1 htmlFile");
      System.exit(0);
    }
    
    new JSoupTest1( args[0] );
  }
  
  public JSoupTest1( String htmlFile )
  {
    String contents = FileUtils.loadString(htmlFile);
    
    if( contents == null )
    {
      System.out.println( "Unable to load file contents, please check file " + htmlFile );
    }
    else
    {
      Document document = Jsoup.parse(contents);
      
      Elements links = document.select("a[href]");
      Elements media = document.select("[src]");
      Elements imports = document.select("link[href]");

      System.out.println( "SRC" );
      for( Element src : media ) 
      {
        System.out.println( "Name: " + src.tagName() + " src: " + src.attr("abs:src"));
      }
        
      System.out.println( "LINKS" );
      for( Element link : imports ) 
      {
        System.out.println( "Name: " + link.tagName() + " href: " + link.attr("abs:href"));
      }

      System.out.println( "SECOND LINKS");
      for( Element link : links ) 
      {
        System.out.println( "Href: " + link.attr("abs:href") + " text: " + link.text() );
      }
    }
  }
}
