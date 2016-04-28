package security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
 
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
 
import org.glassfish.jersey.internal.util.Base64;

import logica.AuthenticationDAO;

@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;
     
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if( !method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all
            if(method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\":\"Access blocked for all users !!\"}").build());
            }
              
            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
              
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
              
            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"You cannot access this resource\"}").build());
            }
              
            //Get encoded username and password
            if(authorization != null && authorization.get(0) != null) {
            	final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
            	//Decode username and password
                String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;
      
                //Split username and password tokens
                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                final String id = tokenizer.nextToken();
                final String token = tokenizer.nextToken();
                  
                //Verifying Username and password
//                System.out.println(id);
//                System.out.println(token);
                  
                //Verify user access
                if(method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
                      
                    //Is user valid?
                    if(!isUserAllowed(id, token, rolesSet)) {
                        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                .entity("{\"error\":\"You cannot access this resource\"}").build());
                    }
                }
            } else {
            	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"You cannot access this resource\"}").build());
            }
        }
    }
    private boolean isUserAllowed(final String id, final String token, final Set<String> rolesSet) {
        String userRole = AuthenticationDAO.authByID(id, token);
        return rolesSet.contains(userRole);
    }
}
