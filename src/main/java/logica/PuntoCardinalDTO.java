package logica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PuntoCardinalDTO implements Comparable<PuntoCardinalDTO>{
	
	public Zona zonaSuperior;
	public Zona zonaInferior;
	public double latitud;
	public double longitud;
	/**
	 * Este atributo es moldeable a cada evento sísmico.
	 */
	public double distancia;
	
	public PuntoCardinalDTO(String zonaSuperior, String zonaInferior, double latitud, double longitud) {
		super();
		this.zonaSuperior = zonaSuperior != null ? Zona.valueOf(zonaSuperior) : null;
		this.zonaInferior = zonaInferior != null ? Zona.valueOf(zonaInferior) : null;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public Zona getNombreZona1() {
		return zonaSuperior;
	}
	
	public Zona getNombreZona2() {
		return zonaInferior;
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setNombreZona1(Zona zona) {
		this.zonaSuperior = zona;
	}
	
	public void setNombreZona2(Zona zona) {
		this.zonaInferior = zona;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	@Override
	public int compareTo(PuntoCardinalDTO o) {
		return (int)this.distancia - (int)o.distancia;
	}	
}
