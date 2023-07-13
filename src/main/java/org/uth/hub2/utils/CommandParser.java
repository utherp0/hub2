package org.uth.hub2.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Integrated command parser for HubCLI
 * @author Ian 'Uther' Lawson
 */
public class CommandParser 
{
  private String _command = null;
  private List<String> _parameters = null;
  private List<String> _validCommands = null;

  public CommandParser()
  {
    
  }
  
  public CommandParser( String input )
  {
    this.parse(input);
  }
  
  public boolean hasData() { return( _command != null ); }
  
  public List<String> getValidCommands() { return _validCommands; }
  
  public boolean isValidCommand( String command ) 
  {
    if( _validCommands == null ) return false;
    
    return _validCommands.contains(command.toLowerCase());
  }
  
  public boolean setValidCommands( List<String> commands )
  {
    if( commands == null ) return false;
    _validCommands = new ArrayList<>();
    
    for( String command : commands )
    {
      _validCommands.add( command.toLowerCase());
    }

    return true;
  }
  
  public String getCommand() { return _command; }
  public List<String> getParameters() { return _parameters; }
  
  /**
   * Simple parse - extract the command, lowercase it, store the parameters (if any).
   * @param input command string to parse
   * @return true if parsed correctly, false if command is null or empty
   */
  public boolean parse( String input )
  {
    if( input == null ) return false;
    if( input.equals("")) return false;
    
    String components[] = input.split(" ");
    
    // Do we have parameters?
    if( components.length == 1 )
    {
      // No, just store the command and return
      _command = input.toLowerCase();
      return true;
    }
    
    // Otherwise process each of the parameters into the list
    _parameters = new LinkedList<>();
    
    for( int loop = 1; loop < components.length; loop++ )
    {
      _parameters.add(components[loop]);
    }
    
    return true;
  }
}
