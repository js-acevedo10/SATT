package com.arquisoft.SATT.dao;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.arquisoft.SATT.mundo.AlertaDTO;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

public class AlertaDAO {
	
	private static String json;
	
	private static final String COLECCION = "alertas";
	
	public static ArrayList<Document> documentos = new ArrayList<Document>();
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public Response getAllAlertas() {
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
	
	public static Response addAlerta(AlertaDTO alerta) {
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
					} else {
						json = "{\"exception\":\"Alerta not added.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
		}
		return ResponseSATT.buildResponse(json);
	}

}
