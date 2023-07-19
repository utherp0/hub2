package org.uth.hub2;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * API interface for accessing Hub2 functionality
 * @author uther
 */
@Path("/hub2")
public class Hub2API
{
  @GET
  @Path("/version")
  @Produces(MediaType.APPLICATION_JSON)
  public String getVersion()
  {
    return "{\"version\":\"0.1\"}";
  }
  
  @GET
  @Path("/paramtest")
  @Produces(MediaType.TEXT_PLAIN)
  public String getParamTest(@QueryParam("param1") String param1, @QueryParam("param2") String param2 )
  {
    return "{\"params\":\"" + param1 + "," + param2 + "\"}";
  }
  
  @GET
  @Path("/test1")
  @Produces(MediaType.TEXT_PLAIN)
  public String getSimpleTest()
  {
    return "Simple Test for checking build change...";
  }
}
