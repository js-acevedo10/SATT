
package com.arquisoft.SATT.recursos;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arquisoft.SATT.dao.SensorDAO;
import com.arquisoft.SATT.mundo.SensorDTO;

// The Java class will be hosted at the URI path "/sensores"
@Path("/sensores")
public class SensorResource {
	
	private SensorDAO dao;

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSensores() {
		return dao.getAllSensores();
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSensor(SensorDTO sensor) {
		return dao.addSensor( sensor );
	}
	
	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//DELETE
	//----------------------------------------------------------------------
}
