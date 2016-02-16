package com.arquisoft.SATT.dao;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.arquisoft.SATT.mundo.AlertaDTO;
import com.arquisoft.SATT.utilidades.KeyValueSearch;
import com.arquisoft.SATT.utilidades.KeyValueUpdate;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.KeyValueSearch.SearchType;
import com.arquisoft.SATT.utilidades.KeyValueUpdate.UpdateType;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

public class AlertaDAO {
	
	private static String json;
	
	private static Response.Status status = Response.Status.ACCEPTED;
	
	private static final String COLECCION = "alertas";
	
	public static ArrayList<Document> documentos = new ArrayList<Document>();
	
	private static AlertaDTO alertaDTO = null;
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public static Response getAllAlertas() {
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
						json = "{\"exception\":\"No alertas found.\"}";
						status = Response.Status.NOT_FOUND;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		
		return ResponseSATT.buildResponse(json, status);
	}
	
	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	public static AlertaDTO addAlerta(AlertaDTO alerta) {
		MongoConnection connection = SATTDB.requestConecction();
		Gson gson = new Gson();
		json = gson.toJson(alerta);
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document alertaDoc = Document.parse(json);
					if(manager.persist(alertaDoc, COLECCION)) {						
						alertaDTO = new AlertaDTO();
						alertaDTO.set_id(alertaDoc.getObjectId("_id"));
						alertaDTO.setId(alertaDoc.getObjectId("_id").toHexString());
						alertaDTO.setAltura(alertaDoc.getDouble("altura"));
						alertaDTO.setPerfil(alertaDoc.getString("perfil"));
						alertaDTO.settLlegada((long)alertaDoc.getInteger("tLlegada"));
						alertaDTO.setZona(alertaDoc.getString("zona"));
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Alerta not added.\"}";
						status = Response.Status.NOT_MODIFIED;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return alertaDTO;
	}
	
	public static void updateAlerta(String id, String perfil, double altura){
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();
					
					filters.add(new KeyValueSearch("_id", id, SearchType.ID));
					updates.add(new KeyValueUpdate("altura", altura, UpdateType.SET));
					if(manager.updateFirst(COLECCION, filters, updates)){
//						alerta = new Gson().fromJson(manager.queryByFilters(COLECCION, filters).first().toJson(), AlertaDTO.class);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		return alerta;
	}

}
