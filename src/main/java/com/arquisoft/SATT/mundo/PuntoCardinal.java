package com.arquisoft.SATT.mundo;

import java.util.ArrayList;

public class PuntoCardinal {
	
	public ArrayList<Zona> nombreZona;
	public double latitud;
	public double longitud;
	
	public PuntoCardinal(String nombreZona1, String nombreZona2, double latitud, double longitud) {
		super();
		nombreZona = new ArrayList<Zona>();
		this.nombreZona.add(Zona.valueOf(nombreZona1));
		if(nombreZona2 != null) {this.nombreZona.add(Zona.valueOf(nombreZona2));}
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public ArrayList<Zona> getNombreZona() {
		return nombreZona;
	}

	public double getPuntoInicio() {
		return latitud;
	}

	public double getPuntoFinal() {
		return longitud;
	}

	public void setNombreZona(ArrayList<Zona> nombreZona) {
		this.nombreZona = nombreZona;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}	
}
