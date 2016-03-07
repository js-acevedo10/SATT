package mundo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AlertaDTO {
	
	private ObjectId _id;
	
	private String id;
	
	private String perfil;
	
	private String zona;
	
	private long tLlegada;
	
	private double altura;
	
	public AlertaDTO(){
		
	}

	public AlertaDTO(ObjectId _id, String perfil, String zona, long tLlegada, double altura) {
		super();
		this._id = _id;
		this.perfil = perfil;
		this.zona = zona;
		this.tLlegada = tLlegada;
		this.altura = altura;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public Number gettLlegada() {
		return tLlegada;
	}

	public void settLlegada(long tLlegada) {
		this.tLlegada = tLlegada;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

}
