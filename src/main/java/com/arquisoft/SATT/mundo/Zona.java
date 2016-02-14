package com.arquisoft.SATT.mundo;

public enum Zona {
	
	Guajira("Guajira"), 
	Magdalena("Magdalena"), 
	Sucre("Sucre"), 
	Cordoba("Córdoba"), 
	Antioquia("Antioquia"),
	Choco("Chocó"), 
	Valle("Valle del Cauca"), 
	Cauca("Cauca"), 
	Narino("Nariño");
	
	private String nombre;
	
	private Zona(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre(){
		return nombre;
	}
}
