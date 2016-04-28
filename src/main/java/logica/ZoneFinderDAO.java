package logica;

import java.util.ArrayList;
import java.util.Collections;

import org.bson.Document;

import persistencia.GeoAsistant;
import persistencia.MongoConnection;
import persistencia.MongoManager;
import persistencia.SATTDB;
import persistencia.MongoConnection.MongoQuery;

import com.google.gson.Gson;

public class ZoneFinderDAO {
	
	public final static String COLECCION = "puntos-cardinales";
	
	public static boolean loaded = false;
	
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
		loaded = true;
		EventoSismicoDTO e = new EventoSismicoDTO(null, 12.225,-74.864,5.0);
		System.out.println(getZonaDeEvento(e));
	}

	public static String getZonaDeEvento(EventoSismicoDTO evento) {
		if(!loaded) {
			loadPuntosCardinales();
		}
		PuntoCardinalDTO pcEvento = new PuntoCardinalDTO(null, null, evento.getLat(), evento.getLng());
		for (PuntoCardinalDTO puntoCardinal : puntosCardinales) {
			puntoCardinal.setDistancia(GeoAsistant.getDistanceBetween(puntoCardinal.getLatitud(), puntoCardinal.getLongitud(), pcEvento.getLatitud(), pcEvento.getLongitud()));
		}
		Collections.sort(puntosCardinales);
		if(puntosCardinales != null) {
			if(pcEvento.getLatitud() <= puntosCardinales.get(0).getLatitud()) {
				if(puntosCardinales.get(0).getNombreZona1() != null) {
					return puntosCardinales.get(0).getNombreZona1().getNombre();
				} else {
					return puntosCardinales.get(0).getNombreZona2().getNombre(); 
				}
			}
			else {
				if(puntosCardinales.get(0).getNombreZona2() != null) {
					return puntosCardinales.get(0).getNombreZona2().getNombre();
				} else {
					return puntosCardinales.get(0).getNombreZona1().getNombre(); 
				}
			}			
		}
		return null;
	}
}
