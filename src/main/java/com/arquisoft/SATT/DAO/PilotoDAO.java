package com.arquisoft.SATT.DAO;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.util.JSON;

import mundo.Avion;
import mundo.usuario.Piloto;
import utilidades.CaptainDB;
import utilidades.KeyValueSearch;
import utilidades.KeyValueSearch.SearchType;
import utilidades.KeyValueUpdate;
import utilidades.KeyValueUpdate.UpdateType;
import utilidades.MongoConnection;
import utilidades.MongoConnection.MongoQuery;
import utilidades.MongoManager;
import utilidades.ResponseCaptain;

public class PilotoDAO {

	private static String json;
	
	private static final String COLECCION = "pilotos";
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------Métodos GET-------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Método que retorna un piloto dado su ID.
	 * @param idUsuario - El ID del piloto buscado.
	 * @return - El piloto.
	 */
	public static Response getPiloto(String idUsuario) {
		MongoConnection connection = CaptainDB.requestConecction();
		try {
			CaptainDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					filters.add(new KeyValueSearch("_id", idUsuario, SearchType.ID));
					Document pilotoDoc = manager.queryByFilters(COLECCION, filters).first();
					if(pilotoDoc != null && !pilotoDoc.isEmpty()) {
						json = pilotoDoc.toJson();
					} else {
						json = "{\"exception\":\"Piloto not found.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Pilotos Collection.\"}";
		}
		
		return ResponseCaptain.buildResponse(json);
	}
	
	//---------------------------------------------------------------------------------------------
	//--------------------------------------Métodos POST-------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Método que añade un nuevo Piloto
	 * @param piloto - El objeto Piloto
	 * @return - El objeto agregado con su ID en la base de datos o un error.
	 */
	public static Response addPiloto(Piloto piloto) {
		MongoConnection connection = CaptainDB.requestConecction();
		Gson gson = new Gson();
		json = gson.toJson(piloto);
		try {
			CaptainDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document pilotoDoc = Document.parse(json);
					if(manager.persist(pilotoDoc, COLECCION)) {
						json = pilotoDoc.toJson();
					} else {
						json = "{\"exception\":\"Piloto not added.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Pilotos Collection.\"}";
		}
		return ResponseCaptain.buildResponse(json);
	}
	
	/**
	 * Método que añade una busqueda al historial de busquedas de un piloto.
	 * @param idUsuario - El ID del piloto.
	 * @param value - La busqueda.
	 * @return - Un mensaje con el resultado.
	 */
	public static Response addToPilotoHistorial(String key, String idUsuario, String value) {
		MongoConnection connection = CaptainDB.requestConecction();
		try {
			ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
			filters.add(new KeyValueSearch("_id", idUsuario, SearchType.ID));
			
			ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();
			updates.add(new KeyValueUpdate(key, value, UpdateType.INSERT));
			
			CaptainDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					if(manager.updateFirst(COLECCION, filters, updates)) {
						json = "{\"exception\":\"Value added correctly.\","
								+ "\"added\":true}";
					} else {
						json = "{\"exception\":\"Value not added.\","
								+ "\"added\":false}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Pilotos Collection.\","
					+ "\"added\":false}";
		}
		return ResponseCaptain.buildResponse(json);
	}
	
	public static Response addAvion(String idUsuario, Avion avion) {
		MongoConnection connection = CaptainDB.requestConecction();
		avion.setFechaDeRegistroNow();
		try {
			CaptainDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					json = JSON.serialize(avion);
					Document avionesDoc = Document.parse(json);
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					filters.add(new KeyValueSearch("_id", idUsuario, SearchType.ID));
					ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();
					updates.add(new KeyValueUpdate("aviones", avionesDoc, UpdateType.INSERT));
					
					if(manager.updateFirst(COLECCION, filters, updates)) {
						json = avionesDoc.toJson();
					} else {
						json = "{\"exception\":\"Error adding Avion to Piloto.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Pilotos Collection.\"}";
		}
		return ResponseCaptain.buildResponse(json);
	}
	
	//---------------------------------------------------------------------------------------------
	//--------------------------------------Métodos PUT--------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Método que edita la información de un Piloto
	 * @param idUsuario - El ID del usuario a modificar.
	 * @param info - La información a modificar.
	 * @return
	 */
	public static Response updatePiloto(String idUsuario, Document infoDoc) {
		ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();
		if(infoDoc.getString("clave") != null) {
			updates.add(new KeyValueUpdate("clave", infoDoc.getString("clave"), UpdateType.SET));
		}
		if(infoDoc.getString("correo") != null) {
			updates.add(new KeyValueUpdate("correo", infoDoc.getString("correo"), UpdateType.SET));
		}
		if(infoDoc.getString("nombre") != null) {
			updates.add(new KeyValueUpdate("nombre", infoDoc.getString("nombre"), UpdateType.SET));
		}
		if(infoDoc.getString("telefono") != null) {
			updates.add(new KeyValueUpdate("telefono", infoDoc.getString("telefono"), UpdateType.SET));
		}
		if(updates != null && !updates.isEmpty()) {
			MongoConnection connection = CaptainDB.requestConecction();
			try {
				CaptainDB.executeQueryWithConnection(connection, new MongoQuery() {
					@Override
					public void query(MongoManager manager) {
						ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
						filters.add(new KeyValueSearch("_id", idUsuario, SearchType.ID));
						if(manager.updateFirst(COLECCION, filters, updates)) {
							json = JSON.serialize(manager.queryByFilters(COLECCION, filters).first());
						} else {
							json = "{\"exception\":\"Error updating Piloto.\"}";
						}
					}
				});
			} catch(Exception e) {
				e.printStackTrace();
				json = "{\"exception\":\"Error Fetching Pilotos Collection.\"}";
			}
		}
		return ResponseCaptain.buildResponse(json);
	}
	
	/**
	 * Método que edita la información de un Avion
	 * @param idUsuario - El ID del Usuario que tiene el Avion.
	 * @param idAvion - El ID del Avion a modificar.
	 * @param info - La información a modificar del avión.
	 * @return
	 */
	public static Response editPlane(String idUsuario, String idAvion, String info) {
		//TODO
		return ResponseCaptain.buildResponse(json);
	}
	
	//---------------------------------------------------------------------------------------------
	//--------------------------------------Métodos DELETE-----------------------------------------
	//---------------------------------------------------------------------------------------------
	
}
