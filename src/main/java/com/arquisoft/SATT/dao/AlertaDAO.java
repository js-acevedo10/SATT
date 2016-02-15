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
					json = JSON.serialize(documentos);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
		}
		
		return ResponseSATT.buildResponse(json);
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
						json = alertaDoc.toJson();
						alertaDTO = new Gson().fromJson(json, AlertaDTO.class);
					} else {
						json = "{\"exception\":\"Alerta not added.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
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
