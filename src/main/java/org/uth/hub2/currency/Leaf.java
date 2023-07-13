package org.uth.hub2.currency;

import java.util.UUID;

/**
 * Simple, fast tree implementation using only Leaves.
 * @author Ian 'Uther' Lawson
 */
public class Leaf 
{
  private String _uuid = null;
  private Leaf _parent = null;
  private Descriptor _description = null;
  
  private static String getUUID()
  {
    return UUID.randomUUID().toString();
  }
  
  public Leaf( Descriptor description )
  {
    _uuid = getUUID();
    _parent = null;
    _description = description;
  }
  
  public Leaf( Leaf parent, Descriptor description )
  {
    _uuid = getUUID();
    _parent = parent;
    _description = description;
  }
  
  public String getID()
  {
    return _uuid;
  }
  
  public String getParentID()
  {
    return( _parent == null ? null : _parent.getID());
  }
  
  public Descriptor getDescription()
  {
    return _description;
  }
  
  public boolean isARoot()
  {
    return( _parent == null );
  }
  
  public void updateDescription( Descriptor description )
  {
    this._description = description;
  }
  
  public void updateParent( Leaf parent )
  {
    this._parent = parent;
  }
}
