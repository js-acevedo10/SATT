package servicios;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import logica.AuthenticationDAO;

@PermitAll
@Path("/auth")
public class AuthenticationResource {
	
	@PermitAll
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response auth(String json) {
		Document j = Document.parse(json);
		return AuthenticationDAO.auth(j.getString("email"), j.getString("password"));
	}

}
