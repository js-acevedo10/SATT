package com.arquisoft.SATT.mundo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventoSismico {
	
	private ObjectId _id;
	
	private long lat;
	
	private long lng;
	
	public EventoSismico(){
		
	}
	
//	public EventoSismico(ObjectId id){
		
//	}

}
