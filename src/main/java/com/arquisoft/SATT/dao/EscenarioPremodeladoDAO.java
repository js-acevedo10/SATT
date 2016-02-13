package com.arquisoft.SATT.dao;

import java.util.ArrayList;

import org.bson.Document;

import com.arquisoft.SATT.utilidades.KeyValueSearch;
import com.arquisoft.SATT.utilidades.KeyValueSearch.SearchType;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.SATTDB;

public class EscenarioPremodeladoDAO {
	
	private static String COLECCION = "escenariosPremodelados";
	private static String perfil = "";
	
	public static String getPerfilAlerta(double altura, double distancia, String zona){ 
		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			connection.runQuery(new MongoQuery() {
				
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					
					filters.add(new KeyValueSearch("zona", zona, SearchType.EQUALS));
					filters.add(new KeyValueSearch("altura_min", altura, SearchType.GREATER));
					filters.add(new KeyValueSearch("altura_max", altura, SearchType.LESS));
					filters.add(new KeyValueSearch("distancia_min", distancia, SearchType.GREATER));
					filters.add(new KeyValueSearch("distancia_max", distancia, SearchType.LESS));

					Document perfilDoc = manager.queryByFilters(COLECCION, filters).first();
					
					perfil = perfilDoc.getString("perfil");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return perfil;
				
	}
	
	/**
	 * No me borren esto porfa, es para generar los datos del modelo, que no es un get ni nada porque
	 * es estático entonces este main de abajo es solo para crear los datos en Mongo.
	 */
//	public static void main(String[] args) {
//		MongoConnection connection = SATTDB.requestConecction();
//		try {
//			connection.runQuery(new MongoQuery() {
//				
//				@Override
//				public void query(MongoManager manager) {
//					double altura = 0;
//					double distancia = 10000;
//					
//					Document modelo = new Document("","");
//					manager.persist(document, COLECCION)
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
