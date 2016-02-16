package com.arquisoft.SATT.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import org.bson.Document;

import com.arquisoft.SATT.mundo.AlertaDTO;
import com.arquisoft.SATT.mundo.ControlAlarmas;
import com.arquisoft.SATT.mundo.EscenarioPremodelado.PerfilAlerta;
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
	
	private static Response.Status status = Response.Status.ACCEPTED;
	
	private static final String COLECCION = "eventos";
	
	public static ArrayList<Document> documentos = new ArrayList<Document>();
	
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public static Response getAllEventos() {
		json = "";
		documentos = new ArrayList<Document>();		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					documentos = manager.queryByFilters(COLECCION, null).into(new ArrayList<Document>());
					if (documentos!=null && !documentos.isEmpty()) {						
						json = JSON.serialize(documentos);
						status = Response.Status.OK;
					}
					else {
						json = "{\"exception\":\"No eventos found.\"}";
						status = Response.Status.NOT_FOUND;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Eventos Collection.\"}";
			status= Response.Status.INTERNAL_SERVER_ERROR;
		}
		
		return ResponseSATT.buildResponse(json, status);
	}
	
	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	/**
	 * Metodo que ejecuta la logica de negocio una vez se recibe el evento sismico
	 * @param evento Evento sismico que desencadena la creacion de una alerta
	 * @return Respuesta
	 */
	public static Response addEvento(EventoSismicoDTO evento) {
		
		/////////////////////////////////
		//Persistencia del evento sismico
		/////////////////////////////////
//		long time = System.currentTimeMillis();
		
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
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Evento not added.\"}";
						status = Response.Status.NOT_MODIFIED;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Eventos Collection.\"}";
			status= Response.Status.INTERNAL_SERVER_ERROR;
		}
		
//		System.out.println("Guarda el evento en: "+(System.currentTimeMillis()-time));
//		time = System.currentTimeMillis();
		
		String zona = ZoneFinderDAO.getZonaDeEvento(evento);
//		System.out.println("Encuentra la zona en: "+(System.currentTimeMillis()-time));
//		time = System.currentTimeMillis();
		/////////////////////////////////
		//Buscar sensor mas cercano
		/////////////////////////////////
		List<Object> sensores = SensorDAO.getListSensores(zona);
//		System.out.println("Recupera los sensores de Mongo en: "+(System.currentTimeMillis()-time));
//		time = System.currentTimeMillis();
		SensorDTO s = buscarSensorMasCercano(evento.getLat(), evento.getLng(), sensores );
//		System.out.println("Busca el sensor en: "+(System.currentTimeMillis()-time));
//		time = System.currentTimeMillis();
		/////////////////////////////////
		//Buscar zona correspondiente al sensor mas cercano
		/////////////////////////////////
		
		
		/////////////////////////////////
		//Comparar datos con Escenario Premodelado
		/////////////////////////////////		
		
		Double altura = s.getAltura();
//		Double distancia = Math.abs(evento.getDistancia()-GeoAsistant.getDistanceBetween(evento.getLat(), evento.getLng(), s.getLat(), s.getLng()));
		Double distancia = evento.getDistancia();
		Long tiempo =  (long) ((distancia/ s.getVelocidad())*3600*1000);
		String perfil = EscenarioPremodeladoDAO.getPerfilAlerta(altura, distancia, zona);
		
		AlertaDTO alerta = new AlertaDTO();
		alerta.setAltura(altura);
		alerta.setPerfil(perfil);
		alerta.settLlegada(tiempo);
		alerta.setZona(zona);
		
		AlertaDTO alertaSaved = AlertaDAO.addAlerta(alerta);
		
		if (perfil.equals(PerfilAlerta.alarma.getName())){
			Thread nuevaAlerta = new Thread(new ControlAlarmas(s.getId(), alertaSaved.getId(), s.getAltura(), distancia, zona));
			nuevaAlerta.start();
		}
//		System.out.println("Crea la alerta e inicia el thread en: "+(System.currentTimeMillis()-time));

		return ResponseSATT.buildResponse(gson.toJson(alertaSaved), status);

	}

	//TODO Soto hace este metodo
	private static SensorDTO buscarSensorMasCercano(double lat, double lng, List<Object> sensores) {
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
