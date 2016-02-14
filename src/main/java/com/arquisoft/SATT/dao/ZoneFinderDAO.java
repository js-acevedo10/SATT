package com.arquisoft.SATT.dao;

import java.util.ArrayList;

import org.bson.Document;

import com.arquisoft.SATT.mundo.PuntoCardinal;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.SATTDB;

public class ZoneFinderDAO {
	
	public final static String COLECCION = "puntos-cardinales";

	public static void loadPuntosCardinales() {
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					ArrayList<Document> puntos = manager.queryByFilters(COLECCION, null)
							.into(new ArrayList<Document>());
					if(puntos != null) {
						for (Document document : puntos) {
							PuntoCardinal pc = new PuntoCardinal(document.getString("nombreZona1"), document.getString("nombreZona2"), document.getDouble("latitud"), document.getDouble("longitud"));
						}
					}
				}
			});
		} catch(Exception e) {
			
		}
	}
	
}
