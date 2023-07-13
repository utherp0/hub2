package org.uth.hub2.tests;

public class HubIndexTest1
{
  public HubIndexTest1()
  {
    // First, ENV test....
    String targetIndexLocation = System.getenv("HUBINDEX");

    System.out.println( "Using target index location: " + targetIndexLocation );
  }

  public static void main( String[] args )
  {
    new HubIndexTest1();
  }
}