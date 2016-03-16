
package recursos;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.AlertaDAO;

// The Java class will be hosted at the URI path "/alertas"
@Path("/alertas")
public class AlertaResource {
	

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAlertas() {
		return AlertaDAO.getAllAlertas();
	}
	
	@GET
	@Path("/{idAlerta}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlertaID(@PathParam("idAlerta") String idAlerta){
		return AlertaDAO.getAlerta(idAlerta);
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//DELETE
	//----------------------------------------------------------------------
}
