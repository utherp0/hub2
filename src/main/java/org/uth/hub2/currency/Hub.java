package org.uth.hub2.currency;

import java.util.LinkedList;
import java.util.List;

/**
 * Data object for a complete Hub.
 * @author Ian 'Uther' Lawson
 */
public class Hub 
{
  public static final String YAML = "YAML";
  public static final String XML = "XML";
  public static final String JSON = "JSON";
  
  private List<Leaf> _tree = null;
  private List<Entry> _entries = null;
  
  public Hub()
  {
    _tree = new LinkedList<>();
    _entries = new LinkedList<>();
    
    // Add the mandatory Root and Pool leaves
    Leaf root = new Leaf( new Descriptor( "Root", "Main Root for Entries"));
    Leaf pool = new Leaf( new Descriptor( "Pool", "Pool for unassigned Entries"));
    
    _tree.add(root);
    _tree.add(pool);
  }
  
  /**
   * Entries accessor for the search components.
   * @return the current list of entries in the Hub
   */
  public List<Entry> getAllEntries()
  {
    return _entries;
  }
  
  /**
   * Add Leaf Method. Every Leaf must be unique by name.
   * @param leaf leaf to add to tree
   * @return false if the tree already has a named leaf, true otherwise
   */
  public boolean addLeaf( Leaf leaf )
  {
    for( Leaf compareLeaf : _tree )
    {
      if( compareLeaf.getDescription().getName().equals( leaf.getDescription().getName()))
      {
        return false;          
      }
    }
    
    _tree.add(leaf);
    return true;
  }
  
  /**
   * Remove Leaf method. This removes the named leaf from the tree, moves all associated 
   * @param name leaf to remove
   * @return 
   */
  public boolean removeLeaf( String name )
  {
    // Cannot remove the Root and Pool leaves
    if( name.equals( "Pool") || 
        name.equals( "Root" ))
    {
      return false;
    }
    
    // Find and remove the Leaf
    for( Leaf leaf : _tree )
    {
      if( leaf.getDescription().getName().equals(name))
      {
        _tree.remove(leaf);
        this.tidy();
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Tidy method. This moves all orphaned Entries to the 'Pool' leaf.
   * @return a list of Entry names that have been tidied to 'Pool'
   */
  public List<String> tidy()
  {
    List<String> moved = new LinkedList<>();
    
    // Fix orphaned entries
    for( Entry entry : _entries )
    {
      if( this.getLeafByName( entry.getLocation()) == null )
      {
        entry.setLocation("Pool");
        moved.add(entry.getName());
      }
    }
    
    // Fix orphaned leaves
    for( Leaf leaf : _tree )
    {
      if( this.getLeafByID( leaf.getParentID()) == null )
      {
        leaf.updateParent( this.getLeafByName("Root"));
      }
    }
    
    return moved;
  }
  
  /**
   * Get named Leaf object.
   * @param name Leaf to return
   * @return Leaf if found, null if not
   */
  public Leaf getLeafByName( String name )
  {
    for( Leaf leaf : _tree )
    {
      if( leaf.getDescription().getName().equals( name ))
      {
        return leaf;
      }
    }
    
    return null;
  }
  
  /**
   * Get Leaf by ID.
   * @param ID ID of Leaf to return
   * @return Leaf if found, null if not
   */
  public Leaf getLeafByID( String ID )
  {
    for( Leaf leaf : _tree )
    {
      if( leaf.getID().equals(ID))
      {
        return leaf;
      }
    }
    
    return null;
  }
  
  /**
   * Get all the leaves.
   * @return the current tree structure
   */
  public List<Leaf> getAllLeaves()
  {
    return _tree;
  }
  
  public List<Entry> getEntriesByLeaf( Leaf leaf )
  {
    return this.getEntriesByLeaf( leaf.getDescription().getName());
  }
  
  public List<Entry> getEntriesByLeaf( String name )
  {
    List<Entry> found = new LinkedList<>();
    
    for( Entry entry : _entries )
    {
      if( entry.getLocation().equals(name))
      {
        found.add(entry);
      }
    }
    
    return found;
  }
  
  /**
   * Add an Entry to the Hub. If it has a named Leaf, add it to that,
   * otherwise place in Pool.
   * @param entry entry to add
   * @return true if added, false if not due to mismatch of Leaf
   */
  public boolean addEntry( Entry entry )
  {
    if( entry.getLocation() == null )
    {
      entry.setLocation("Pool");
      _entries.add(entry);
      
      return true;
    }
    
    if( this.getLeafByName( entry.getLocation()) == null )
    {
      return false;
    }
    
    _entries.add(entry);
    return true;
  }
  
  /**
   * Remove an entry by name.
   * @param name name of Entry to remove
   * @return true if found and removed, false if not in Hub
   */
  public boolean removeEntry( String name )
  {
    for( Entry entry : _entries )
    {
      if( entry.getName().equals( name ))
      {
        _entries.remove(entry);
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Export the Hub object.
   * @param mode mode of export (XML, JSON, YAML)
   * @return the contents of the Hub exported in the appropriate format
   */
  public String export( String mode )
  {
    // Check the mode is a correct acceptable one
    if( !( mode.equals( Hub.JSON)) &&
        !( mode.equals( Hub.XML)) &&
        !( mode.equals( Hub.YAML))) return null;
    
    if( mode.equals( Hub.JSON)) return this.exportJSON();
    if( mode.equals( Hub.XML)) return this.exportXML();
    if( mode.equals( Hub.YAML)) return this.exportYAML();
    
    return null;
  }
  
  private String exportJSON()
  {
    return null;
  }
  
  private String exportXML()
  {
    return null;
  }
  
  private String exportYAML()
  {
    return null;
  }
}
