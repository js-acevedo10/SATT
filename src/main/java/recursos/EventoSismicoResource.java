
package recursos;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.EventoSismicoDAO;
import mundo.EventoSismicoDTO;

// The Java class will be hosted at the URI path "/eventos"
@Path("/eventos")
public class EventoSismicoResource {

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEventos() {
		return EventoSismicoDAO.getAllEventos();
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
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
