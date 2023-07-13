package org.uth.hub2.utils;

import java.util.Arrays;

/**
 * Utilities for sorting strings.
 * @author Ian Lawson
 */
public class SortUtils 
{
  public static String[] parallelSort( String[] input, boolean lowerCase )
  {
    String[] working = new String[input.length];
    
    int pos = 0;
    for( String item : input )
    {
      working[pos] = ( lowerCase ? item.toLowerCase() : item );
      pos++;
    }

    Arrays.parallelSort(working);
    
    return working;
  }
}
