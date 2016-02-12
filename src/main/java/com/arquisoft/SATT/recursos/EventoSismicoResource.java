
package com.arquisoft.SATT.recursos;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arquisoft.SATT.dao.EventoSismicoDAO;
import com.arquisoft.SATT.mundo.EventoSismicoDTO;

// The Java class will be hosted at the URI path "/eventos"
@Path("/eventos")
public class EventoSismicoResource {
	
	private EventoSismicoDAO dao;

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEventos() {
		return dao.getAllEventos();
	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEvento(EventoSismicoDTO evento) {
		return dao.addEvento( evento );
	}
	
	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	//DELETE
	//----------------------------------------------------------------------
}
