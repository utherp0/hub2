package org.uth.hub2.currency;

import java.util.LinkedList;
import java.util.List;

/**
 * Leaf Descriptor class. This is an object that attaches to a leaf and describes it.
 * @author Ian 'Uther' Lawson
 */
public class Descriptor 
{
  private String _name = null;
  private List<String> _tags = new LinkedList<String>();
  private List<String> _categories = new LinkedList<String>();
  private String _text = null;
  private boolean _sync = false;
  
  public Descriptor( String name )
  {
    _name = name;
  }
  
  public Descriptor( String name, String text )
  {
    _name = name;
    _text = text;
  }
  
  public Descriptor( String name, String text, List<String> tags, List<String> categories )
  {
    _name = name;
    _text = text;
    _tags = tags;
    _categories = categories;
  }
  
  public String getName() { return _name; }
  public String getText() { return _text; }
  public List<String> getTags() { return _tags; }
  public List<String>getCategories() { return _categories; }
  
  public boolean hasTag( String tag )
  {
    return _tags.contains(tag);
  }
  
  public boolean hasCategory( String category )
  {
    return _categories.contains(category);
  }
  
  public boolean addTag( String tag )
  {
    if( _tags.contains(tag)) return false;
    
    _tags.add(tag);
    _sync = false;
    
    return true;
  }
  
  public boolean addCategory( String category )
  {
    if( _categories.contains(category)) return false;
    
    _categories.add(category);
    _sync = false;
    
    return true;
  }
  
  public boolean removeTag( String tag )
  {
    if( !( _tags.contains(tag))) return false;
    
    _tags.remove(tag);
    _sync = false;
    
    return true;
  }
  
  public boolean removeCategory( String category )
  {
    if( !( _categories.contains(category))) return false;
    
    _categories.remove(category);
    _sync = false;
    
    return true;
  }
  
  public void resetTags()
  {
    _tags = new LinkedList<String>();
  }
  
  public void resetCategories()
  {
    _categories = new LinkedList<String>();
  }
  
  public boolean isSync() { return _sync; }
  
  public void setSync() { _sync = true; }  
}