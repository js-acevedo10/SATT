package com.arquisoft.SATT.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.arquisoft.SATT.mundo.EventoSismicoDTO;
import com.arquisoft.SATT.mundo.SensorDTO;
import com.arquisoft.SATT.utilidades.KeyValueSearch;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.KeyValueSearch.SearchType;
import com.arquisoft.SATT.utilidades.KeyValueUpdate;
import com.arquisoft.SATT.utilidades.KeyValueUpdate.UpdateType;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

public class SensorDAO {
	
	private static String json;
	
	private static Response.Status status = Response.Status.ACCEPTED;
	
	private static final String COLECCION = "sensores";
	
	private static ArrayList<Document> documentos = new ArrayList<Document>();
	
	private static List<Object> listaSensores = new ArrayList<Object>();
	
	private static SensorDTO sensor = null;
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public static Response getAllSensores() {
		json = "";
		documentos = new ArrayList<Document>();
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					documentos = manager.queryByFilters(COLECCION, null).into(new ArrayList<Document>());
					if(documentos != null && !documentos.isEmpty()) {						
						json = JSON.serialize(documentos);
						status = Response.Status.OK;
					} else {
						status = Response.Status.NOT_FOUND;
						json = "{\"exception\":\"no sensores found.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return ResponseSATT.buildResponse(json, status);
	}
	
	
	public static List<Object> getListSensores(String zona) {
		json = "";
		documentos = new ArrayList<Document>();		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					filters.add(new KeyValueSearch("nombreZona", zona, SearchType.EQUALS));
					documentos = manager.queryByFilters(COLECCION, filters).into(new ArrayList<Document>());
					listaSensores = ResponseSATT.transformDocumentList(documentos, SensorDTO.class);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
		}
		
		return listaSensores;
	}
	
	public static Response getSensorJson(String id){
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					filters.add(new KeyValueSearch("_id", id, SearchType.ID));
					Document sensorDoc = manager.queryByFilters(COLECCION, filters).first();
					if(sensorDoc != null) {
						json = sensorDoc.toJson();
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Sensor not found.\"}";
						status = Response.Status.NOT_FOUND;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return ResponseSATT.buildResponse(json, status);
	}
	
	public static SensorDTO getSensor(String id){
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					filters.add(new KeyValueSearch("_id", id, SearchType.ID));
					Document sensorDoc = manager.queryByFilters(COLECCION, filters).first();
					sensor = new Gson().fromJson(sensorDoc.toJson(), SensorDTO.class);
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensor;
	}
	
	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	public static Response addSensor(SensorDTO sensor) {
		MongoConnection connection = SATTDB.requestConecction();
		Gson gson = new Gson();
		sensor.setNombreZona(ZoneFinderDAO.getZonaDeEvento(new EventoSismicoDTO(null, sensor.getLat(), sensor.getLng(), 0)));
		json = gson.toJson(sensor);
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document sensorDoc = Document.parse(json);
					if(manager.persist(sensorDoc, COLECCION)) {
						json = sensorDoc.toJson();
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Sensor not added.\"}";
						status = Response.Status.NOT_MODIFIED;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return ResponseSATT.buildResponse(json, status);
	}

	//----------------------------------------------------------------------
	//PUT
	//----------------------------------------------------------------------
	
	public static Response addLecturaToSensor(SensorDTO sensor) {
		MongoConnection connection = SATTDB.requestConecction();
		try {
			ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
			filters.add(new KeyValueSearch("lat", sensor.getLat(), SearchType.EQUALS));
			filters.add(new KeyValueSearch("lng", sensor.getLng(),SearchType.EQUALS));
			ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();
			
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document sensorDoc = manager.queryByFilters(COLECCION, filters).first();
					if(sensorDoc != null) {
						updates.add(new KeyValueUpdate("altura", sensor.getAltura(), UpdateType.SET));
						updates.add(new KeyValueUpdate("velocidad", sensor.getVelocidad(), UpdateType.SET));
						updates.add(new KeyValueUpdate("historial", new Document().append("_id", new ObjectId()).append("altura", sensorDoc.getDouble("altura")).append("velocidad", sensorDoc.getDouble("velocidad")), UpdateType.INSERT));
						if(manager.updateFirst(COLECCION, filters, updates)) {
							json = "{\"exception\":\"Lectura added correctly.\"}";
							status = Response.Status.OK;
						} else {
							json = "{\"exception\":\"Lectura not added.\"}";
							status = Response.Status.NOT_MODIFIED;
						}
					} else {
						json = "{\"exception\":\"Lectura not added.\"}";
						status = Response.Status.NOT_MODIFIED;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return ResponseSATT.buildResponse(json, status);
	}
	
	public static void main(String[] args) {
		int i=0;
		
		try {
			ZoneFinderDAO.loadPuntosCardinales();
			BufferedReader br = new BufferedReader(new FileReader("./data/sensores.csv"));
			String line = br.readLine();
			while (i<4000){
				SensorDTO sensor = new SensorDTO();
				line = br.readLine();
				String[] values = line.split(",");
				sensor.setLat(Double.parseDouble(values[0]));
				sensor.setLng(Double.parseDouble(values[1]));
				sensor.setAltura(Double.parseDouble(values[2]));
				sensor.setVelocidad(Double.parseDouble(values[3]));
				i++;
				SensorDAO.addSensor(sensor);
				System.out.println("Added sensor "+i);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
