
package com.arquisoft.SATT.recursos;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

// The Java class will be hosted at the URI path "/myresource"
@Path("/myresource")
public class MyResource {

    @GET 
    @Produces("text/plain")
    public String getIt() {
        return "Got it!";
    }
}
