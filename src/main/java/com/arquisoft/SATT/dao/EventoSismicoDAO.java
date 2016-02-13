package com.arquisoft.SATT.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.arquisoft.SATT.mundo.AlertaDTO;
import com.arquisoft.SATT.mundo.EventoSismicoDTO;
import com.arquisoft.SATT.mundo.SensorDTO;
import com.arquisoft.SATT.utilidades.KeyValueSearch;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.KeyValueSearch.SearchType;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;

public class EventoSismicoDAO {
	
	private static String json;
	
	private static final String COLECCION = "eventos";
	
	public static ArrayList<Document> documentos = new ArrayList<Document>();
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public Response getAllEventos() {
		json = "";
		documentos = new ArrayList<Document>();		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					documentos = manager.queryByFilters(COLECCION, null).into(new ArrayList<Document>());
					json = JSON.serialize(documentos);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Eventos Collection.\"}";
		}
		
		return ResponseSATT.buildResponse(json);
	}
	
	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	/**
	 * Metodo que ejecuta la logica de negocio una vez se recibe el evento sismico
	 * @param evento Evento sismico que desencadena la creacion de una alerta
	 * @return Respuesta
	 */
	public Response addEvento(EventoSismicoDTO evento) {
		
		/////////////////////////////////
		//Persistencia del evento sismico
		/////////////////////////////////
		
		MongoConnection connection = SATTDB.requestConecction();
		Gson gson = new Gson();
		json = gson.toJson(evento);
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document eventoDoc = Document.parse(json);
					if(manager.persist(eventoDoc, COLECCION)) {
						json = eventoDoc.toJson();
					} else {
						json = "{\"exception\":\"Evento not added.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Eventos Collection.\"}";
		}
		
		/////////////////////////////////
		//Buscar sensor mas cercano
		/////////////////////////////////
		List<Object> sensores = SensorDAO.getListSensores();
		SensorDTO s = buscarSensorMasCercano( evento.getLat(), evento.getLng(), sensores );
		
		
		/////////////////////////////////
		//Buscar zona correspondiente al sensor mas cercano
		/////////////////////////////////
		
		String zona;
		//TODO Juancho hace este metodo

		
		/////////////////////////////////
		//Comparar datos con Escenario Premodelado
		/////////////////////////////////		
		
		Double altura = s.getAltura();
		Double tiempo = evento.getDistancia() / s.getVelocidad();
		String perfil;
		//TODO Soto hace este metodo
		
		/////////////////////////////////
		//Crear Alerta, persistirla y retornarla
		/////////////////////////////////	
		
		AlertaDTO alerta;
		//TODO Soto hace este metodo
		
		return ResponseSATT.buildResponse(json);
	}

	//TODO Soto hace este metodo
	private SensorDTO buscarSensorMasCercano(long lat, long lng, List<Object> sensores) {
		for (Object object : sensores) {
			SensorDTO actual = (SensorDTO) object;
			
			
		}
		return null;
		
	}

}
