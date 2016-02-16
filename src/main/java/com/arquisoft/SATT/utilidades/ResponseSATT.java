package com.arquisoft.SATT.utilidades;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;

import com.google.gson.Gson;

/**
 * Esta clase sirve para enviar respuestas REST sin tener que armarlas siempre, dependiendo de las necesidades <br>
 * vamos a tener o no que separar los métodos y los headers.
 * @author JuanSantiagoAcev
 */
public class ResponseSATT {

	/**
	 * El asterisco significa que el reques puede venir de cualquier url, se reemplaza por una url en específico
	 */
	public final static String ORIGIN = "*";
	
	/**
	 * Los headers son las operaciones
	 */
	public final static String HEADERS = "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept";
	
	/**
	 * Método que construye la respuesta de cualquier método REST
	 * @param respuesta - el objeto que se vaya a enviar (debe ser String, JSON o un objeto anotado con JAX-RS)
	 * @return la respuesta lista para ser enviada
	 */
	public static Response buildResponse(Object respuesta, Response.Status status) {
		return Response.status(status)
				.entity(respuesta)
				.header("Access-Control-Allow-Headers", HEADERS)
				.header("Access-Control-Allow-Origin", ORIGIN)
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, HEAD, OPTIONS")
				.build();
	}
	
	/**
	 * Método que transforma una lista de documentos en una lista de la clase que entra como parametro
	 * @param documentos - la lista de documentos original
	 * @param c - la clase a la que se quiere transformar la lista
	 * @return la lista con los objetos de la clase que se necesitan
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> transformDocumentList(List<Document> documentos, Class c) {
		List<Object> rta = new ArrayList<Object>();
		Gson gson = new Gson();
		for (Document document : documentos) {
			String json = document.toJson();
			Object o = gson.fromJson(json, c);
			rta.add(o);
		}
		return rta;
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------VERIFICADORES DE CLASES DEL MUNDO---------------------------------------
	//------------------------------------------------------------------------------------------------
	
	//------------------------------------------------------------------------------------------------
	//------------------------UTILIDADES PARA VERIFICACION DE OBJETOS---------------------------------
	//------------------------------------------------------------------------------------------------
	
	/**
	 * Método que revisa la nulidad de un objeto
	 * @param o - el objeto
	 * @return false si es nulo, true si no es nulo
	 */
	public static boolean nullCheck(ArrayList<Object> os) {
		for (Object object : os) {
			if(object == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Método que revisa un String
	 * @param s - el String
	 * @return false si está vacío, true si no está vacío
	 */
	public static boolean stringCheck(ArrayList<String> s) {
		for (String string : s) {
			if(string.equals("") || string.equals(" ")) {
				return false;
			}
		}
		return true;
	}
}
