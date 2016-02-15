
package com.arquisoft.SATT.recursos;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arquisoft.SATT.dao.AlertaDAO;

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
