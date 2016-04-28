
package servicios;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import logica.SensorDAO;
import logica.SensorDTO;
import security.Roles;

// The Java class will be hosted at the URI path "/sensores"
@PermitAll
@Path("/sensores")
public class SensorResource {
	

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSensores() {
		return SensorDAO.getAllSensores();
	}
	
	@PermitAll
	@GET
	@Path("/{idSensor}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSensor(@PathParam("idSensor") String idSensor) {
		return SensorDAO.getSensorJson(idSensor);
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	@RolesAllowed(Roles.ADMIN)
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSensor(SensorDTO sensor) {
		return SensorDAO.addSensor( sensor );
	}
	
	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	@PermitAll
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addLecturaToSensor(SensorDTO sensor) {
		return SensorDAO.addLecturaToSensor(sensor);
	}
	
	//----------------------------------------------------------------------
	//DELETE
	//----------------------------------------------------------------------
}
