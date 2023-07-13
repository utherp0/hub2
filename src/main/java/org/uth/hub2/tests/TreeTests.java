package org.uth.hub2.tests;

import java.util.LinkedList;
import java.util.List;
import org.uth.hub2.currency.Descriptor;
import org.uth.hub2.currency.Leaf;

/**
 * Tests of the tree implementation.
 * @author Ian 'Uther' Lawson
 */
public class TreeTests 
{
  public static List<String> _working = new LinkedList<>();
  
  public static void main( String[] args )
  {
    new TreeTests();      
  }
  
  public TreeTests()
  {
    // Create a simple tree
    List<Leaf> tree = new LinkedList<>();
    
    // Root
    Leaf root = new Leaf( new Descriptor( "Root Node"));
    
    // Tier 1
    Leaf t1l1 = new Leaf( root, new Descriptor( "T1L1"));
    Leaf t1l2 = new Leaf( root, new Descriptor( "T1L2"));
    
    // Tier 2
    Leaf t2l1 = new Leaf( t1l1, new Descriptor( "T2L1"));
    Leaf t2l2 = new Leaf( t1l1, new Descriptor( "T2L2"));
    Leaf t2l3 = new Leaf( t1l1, new Descriptor( "T2L3"));
    
    Leaf t3l1 = new Leaf( t1l2, new Descriptor( "T3L1"));
    Leaf t3l2 = new Leaf( t1l2, new Descriptor( "T3L2"));
    
    // Tier 3
    Leaf t4l1 = new Leaf( t3l1, new Descriptor( "T4L1"));
    Leaf t4l2 = new Leaf( t3l1, new Descriptor( "T4L2"));
    Leaf t4l3 = new Leaf( t3l2, new Descriptor( "T4L3"));
    
    tree.add(root);
    tree.add(t1l1);
    tree.add(t1l2);
    tree.add(t2l1);
    tree.add(t2l2);
    tree.add(t2l3);
    tree.add(t3l1);
    tree.add(t3l2);
    tree.add(t4l1);
    tree.add(t4l2);
    tree.add(t4l3);
    
    // Recursive traverse
    traverse( root, tree );
    
    System.out.println( "Count: " +_working.size());
    
    for( String report : _working )
    {
      System.out.println( "" + report );  
    }
  }
  
  private void traverse( Leaf root, List<Leaf> tree )
  {
    for( Leaf leaf : tree )
    {
      if( leaf.getParentID() != null  && leaf.getParentID().equals( root.getID()))
      {
        _working.add( root.getDescription().getName() + ":" + leaf.getDescription().getName() );
        
        traverse( leaf, tree );
      }
    }
  }
}
