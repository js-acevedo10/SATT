package com.arquisoft.SATT.dao;

import java.util.ArrayList;
import java.util.Collections;

import org.bson.Document;
//import org.bson.types.ObjectId;

import com.arquisoft.SATT.mundo.EventoSismicoDTO;
import com.arquisoft.SATT.mundo.PuntoCardinalDTO;
import com.arquisoft.SATT.utilidades.GeoAsistant;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.SATTDB;
import com.google.gson.Gson;

public class ZoneFinderDAO {
	
	public final static String COLECCION = "puntos-cardinales";
	
	private static ArrayList<PuntoCardinalDTO> puntosCardinales = new ArrayList<PuntoCardinalDTO>();

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
							PuntoCardinalDTO pc = new Gson().fromJson(document.toJson(), PuntoCardinalDTO.class);
							puntosCardinales.add(pc);
						}
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		EventoSismicoDTO evento = new EventoSismicoDTO(new ObjectId(), 2.743488, -78.078211, 31.2);
		System.out.println("Ganador: " + getZonaDeEvento(evento));
	}

	public static String getZonaDeEvento(EventoSismicoDTO evento) {
		PuntoCardinalDTO pcEvento = new PuntoCardinalDTO(null, null, evento.getLat(), evento.getLng());
		for (PuntoCardinalDTO puntoCardinal : puntosCardinales) {
			puntoCardinal.setDistancia(GeoAsistant.getDistanceBetween(puntoCardinal.getLatitud(), puntoCardinal.getLongitud(), pcEvento.getLatitud(), pcEvento.getLongitud()));
		}
		Collections.sort(puntosCardinales);
		if(puntosCardinales != null) {
			if(pcEvento.getLatitud() <= puntosCardinales.get(0).getLatitud())
				return puntosCardinales.get(0).getNombreZona1().getNombre();
			else {
				return puntosCardinales.get(0).getNombreZona2().getNombre();
			}			
		} else {
			return null;
		}
	}
}
