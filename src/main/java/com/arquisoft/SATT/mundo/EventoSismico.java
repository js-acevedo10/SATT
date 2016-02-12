package com.arquisoft.SATT.mundo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventoSismico {
	
//	private  _id;
	
	private long lat;
	
	private long lng;
	
	public EventoSismico(){
		
	}
	
//	public EventoSismico(ObjectId id){
		
//	}

}
