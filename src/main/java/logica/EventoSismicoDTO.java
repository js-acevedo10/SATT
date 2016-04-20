package logica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventoSismicoDTO {
	
	private ObjectId _id;
	
	private String id;

	private double lat;
	
	private double lng;
	
	private double distancia;

	public EventoSismicoDTO(){
		
	}
	
	public EventoSismicoDTO(ObjectId id, double lat, double lng, double distancia ){
		this._id = id;
		this.lat = lat;
		this.lng = lng;
		this.distancia = distancia;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
