package logica;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.Document;
import org.bson.types.ObjectId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SensorDTO {
	
	private ObjectId _id;
	
	private String id;

	private double lat;
	
	private double lng;
	
	private double altura;
	
	private double velocidad;
	
	public String nombreZona;
	
	private ArrayList<Document> historial = new ArrayList<Document>();
	
	public SensorDTO(){
		
	}

	public SensorDTO(ObjectId _id, double lat, double lng, double altura, double velocidad) {
		super();
		this._id = _id;
		this.lat = lat;
		this.lng = lng;
		this.altura = altura;
		this.velocidad = velocidad;
	}
	
	public void addDocToHistorial(Document doc) {
		historial.add(doc);
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNombreZona() {
		return nombreZona;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public ArrayList<Document> getHistorial() {
		return historial;
	}
	
	public void setNombreZona(String nombreZona) {
		this.nombreZona = nombreZona;
	}
	
	public void setHistorial(ArrayList<Document> historial) {
		this.historial = historial;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}
}
