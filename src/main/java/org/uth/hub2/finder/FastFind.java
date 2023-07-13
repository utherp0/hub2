package org.uth.hub2.finder;

import java.util.ArrayList;
import java.util.List;
import org.uth.hub2.currency.Entry;
import org.uth.hub2.currency.Hub;

/**
 * FastFind examples.
 * @author Ian Lawson
 */
public class FastFind 
{
  public List<Entry> simpleSearch( String field, List<String> tokens, boolean andLogic, Hub hub )
  {
    // andLogic defines behaviour - 'and' will search for *all* the tokens together in the target field,
    // 'or' searches for any occurance of the tokens in the target field
    
    List<Entry> working = new ArrayList<>();
    
    for( Entry entry : hub.getAllEntries())
    {
      String targetField = null;
      switch( field )
      {
        case "url" : targetField = entry.getURL().toString();
        break;
          
        case "name" : targetField = entry.getName();
        break;
          
        case "text" : targetField = entry.getText();
        break;
        
        default: targetField = entry.getText();
        break;          
      }

      targetField = targetField.toLowerCase();
      
      // Search logic
      if( andLogic )
      {
        boolean discovered = true;
        
        int tokenPos = 0;
        while( discovered && tokenPos < tokens.size())
        {
          if( targetField.indexOf(tokens.get(tokenPos)) == -1 )
          {
            discovered = false;
          }
          
          tokenPos++;
        }
        
        if( discovered )
        {
          working.add(entry);
        }
      }
      else
      {
        boolean discovered = false;
        
        int tokenPos = 0;
        while( !discovered && tokenPos < tokens.size() )
        {
          if( targetField.indexOf(tokens.get(tokenPos)) != -1 )
          {
            discovered = true;
          }
          
          tokenPos++;
        }
        
        if( discovered )
        {
          working.add(entry);
        }
      }
    }
    
    return working;
  }
}
