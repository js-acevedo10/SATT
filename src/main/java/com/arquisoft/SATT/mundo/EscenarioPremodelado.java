package com.arquisoft.SATT.mundo;

import org.bson.types.ObjectId;

public class EscenarioPremodelado {
	
	public enum PerfilAlerta{
		
		informativo("Informativo"), 
		precaucion("Precuaucion"), 
		alerta("Alerta"),
		alarma("Alarma");
		
		String perfilString;
		
		PerfilAlerta(String s){
			perfilString = s;
		}
		
		String string(){return perfilString;}
		
	}

	
}
