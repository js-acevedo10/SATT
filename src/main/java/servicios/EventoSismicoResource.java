
package servicios;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import logica.EventoSismicoDAO;
import logica.EventoSismicoDTO;
import security.Roles;

// The Java class will be hosted at the URI path "/eventos"
@Path("/eventos")
public class EventoSismicoResource {

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@RolesAllowed(Roles.ADMIN)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEventos() {
		return EventoSismicoDAO.getAllEventos();
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	@RolesAllowed(Roles.USUARIO)
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEvento(EventoSismicoDTO evento) {
		return EventoSismicoDAO.addEvento( evento );
	}
	
	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//DELETE
	//----------------------------------------------------------------------
}
