package org.uth.hub2.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.uth.hub2.currency.Entry;
import org.uth.hub2.currency.Descriptor;
import org.uth.hub2.currency.Hub;
import org.uth.hub2.currency.Leaf;
import org.uth.hub2.utils.CommandParser;

/**
 * CLI Test for command line interactions.
 * @author Ian 'Uther' Lawson
 */
public class CLITest 
{
  private Hub _hub = new Hub();
  
  public static void main( String[] args )
  {
    new CLITest();
  }
  
  
  
  public CLITest()
  {
    Scanner input = new Scanner( System.in );
    CommandParser parser = new CommandParser();
    
    List<String> validCommands = new ArrayList<>();
    
    // Add supported commands
    validCommands.add( "quit" );
    validCommands.add( "q" );
    validCommands.add( "describe" );
    validCommands.add( "desc" );
    validCommands.add( "add" );
    validCommands.add( "reset" );
    
    String command = input.nextLine();
    
    while( !( command.equalsIgnoreCase("quit") && !( command.equalsIgnoreCase("q"))))
    {
      parser.parse(command);
      
      if( parser.hasData() && parser.isValidCommand(parser.getCommand()))
      {
        switch( parser.getCommand() )
        {
          case "help" :
          {
            StringBuffer commands = new StringBuffer();
            for( String validCommand : parser.getValidCommands())
            {
              commands.append(validCommand + " ");
            }
            
            log( "Valid commands: " + commands.toString());
            break;
          }
          
          case "reset" :
          {
            _hub = new Hub();
            
            break;
          }
          
          case "add" :
          {
            if( this.handleAdd( parser ) )
            {
              log( "Successful add operation.");
            }
            else
            {
              log( "Unsuccessful add operation.");
            }
            
            break;
          }
        }
      }
      
      command = input.nextLine();
    }
  }
  
  private boolean handleAdd( CommandParser parser )
  {
    // Parse the parameters
    List<String> parameters = parser.getParameters();
    
    // Add allows for adding a Leaf or an Entry - this is indicated by the first parameters
    if( !( parameters.get(0).equals( "leaf" ) && !( parameters.get(0).equals( "entry" ))))
    {
      log( "Invalid parameter for 'add', must be 'leaf' or 'entry'");
      return false;
    }
    
    // Leaf - parameters should be name,text,parentName
    if( parameters.get(0).equals( "leaf"))
    {
      String[] leafParams = parameters.get(1).split(",");
      
      if( leafParams.length != 3 )
      {
        log( "Leaf Add needs name,text,parentName");
        return false;
      }
      
      String name = leafParams[0];
      String text = leafParams[1];
      String parent = leafParams[2];
      
      if( _hub.getLeafByName(name) != null )
      {
        log("Hub contains a leaf with name " + name + " already.");
        return false;
      }
      
      if( _hub.getLeafByName(parent) == null )
      {
        log("No such parent leaf " + parent + " - defaulting to pool");
        parent = "Pool";
      }
      
      _hub.addLeaf(new Leaf( _hub.getLeafByName(parent), new Descriptor( name,text )));
      log( "Added leaf " + name );
      
      return true;
    }
    
    return false;
  }
  
  private void log( String message )
  {
    System.out.println( "[HubCLI] " + message );
  }
}
