package com.arquisoft.SATT.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.arquisoft.SATT.mundo.SensorDTO;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.ResponseSATT;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

public class SensorDAO {
	
	private static String json;
	
	private static final String COLECCION = "sensores";
	
	private static ArrayList<Document> documentos = new ArrayList<Document>();
	
	private static List<Object> listaSensores = new ArrayList<Object>();
	
	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public Response getAllSensores() {
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
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
		}
		
		return ResponseSATT.buildResponse(json);
	}
	
	
	public static List<Object> getListSensores() {
		json = "";
		documentos = new ArrayList<Document>();		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					documentos = manager.queryByFilters(COLECCION, null).into(new ArrayList<Document>());
					listaSensores = ResponseSATT.transformDocumentList(documentos, SensorDTO.class);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
		}
		
		return listaSensores;
	}
	
	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------
	
	public Response addSensor(SensorDTO sensor) {
		MongoConnection connection = SATTDB.requestConecction();
		Gson gson = new Gson();
		json = gson.toJson(sensor);
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document sensorDoc = Document.parse(json);
					if(manager.persist(sensorDoc, COLECCION)) {
						json = sensorDoc.toJson();
					} else {
						json = "{\"exception\":\"Sensor not added.\"}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Sensores Collection.\"}";
		}
		return ResponseSATT.buildResponse(json);
	}

}