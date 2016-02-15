package com.arquisoft.SATT.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.arquisoft.SATT.mundo.AlertaDTO;
import com.arquisoft.SATT.mundo.EventoSismicoDTO;
import com.arquisoft.SATT.mundo.SensorDTO;
import com.arquisoft.SATT.utilidades.GeoAsistant;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
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
		SensorDTO s = buscarSensorMasCercano(evento.getLat(), evento.getLng(), sensores );
		
		
		/////////////////////////////////
		//Buscar zona correspondiente al sensor mas cercano
		/////////////////////////////////
		
		String zona = ZoneFinderDAO.getZonaDeEvento(evento);
		
		/////////////////////////////////
		//Comparar datos con Escenario Premodelado
		/////////////////////////////////		
		
		Double altura = s.getAltura();
		Double distancia = Math.abs(evento.getDistancia()-GeoAsistant.getDistanceBetween(evento.getLat(), evento.getLng(), s.getLat(), s.getLng()));
		Double tiempo =  distancia/ s.getVelocidad();
		String perfil = EscenarioPremodeladoDAO.getPerfilAlerta(altura, distancia, zona);
		//TODO Soto hace este metodo
		
		/////////////////////////////////
		//Crear Alerta, persistirla y retornarla
		/////////////////////////////////	
		
		AlertaDTO alerta;
		//TODO Soto hace este metodo
		
		return ResponseSATT.buildResponse(json);
	}

	//TODO Soto hace este metodo
	private SensorDTO buscarSensorMasCercano(double lat, double lng, List<Object> sensores) {
		SensorDTO sensorCercano = null;
		double distanciaMinima = Double.MAX_VALUE;
		for (Object object : sensores) {
			SensorDTO sensor = (SensorDTO) object;
			double distancia = GeoAsistant.getDistanceBetween(lat, lng, sensor.getLat(), sensor.getLng());
			if (distancia < distanciaMinima){
				distanciaMinima = distancia;
				sensorCercano = sensor;
			}
		}
		return sensorCercano;
	}

}
