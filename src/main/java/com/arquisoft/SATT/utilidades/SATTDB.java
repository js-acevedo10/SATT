package com.arquisoft.SATT.utilidades;

import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;

/**
 * CaptainDB es la clase de acceso a Mongo. Es el puerto entre el programador y Mongo. 
 * Todas las consultas deben pasar por esta clase. Es estática en su funcionalidad, pero se soporta <br> sobre un singleton.
 * Soporta búsquedas instantáneas y programadas en el background (beta).
 * @author danielsoto
 *
 */
public class SATTDB {

	private static SATTDB captain = null;
	private static MongoConnection connection = null;
	private static MongoManager mongo = null;
	private static int connectionIDs = 0;
	
	/**
	 * Las prioridades de las búsquedas.
	 * @author danielsoto
	 *
	 */
	public static enum QueryPriority{
		Low, Medium, High, Urgent
	}

	/**
	 * Abre una nueva conexión con Mongo. Maneja todos los procedimientos para estar listo <br> para ejecutar un Query.
	 * @return La conexión creada.
	 */
	public static MongoConnection requestConecction(){
		if (captain == null||mongo==null||connection==null){ 
			captain = new SATTDB();
			mongo = captain.createMongoManager();
			connection = new MongoConnection(mongo, ++connectionIDs);
			connection.start();
		}
		return connection;
	}

	/**
	 * Ejecuta un Query sobre una conexión dada por parámetro. Espera en el background por recursos. Aún en pruebas.
	 * @param connection - La conexión por la cuál se ejecuta el bloque. 
	 * @param query - El bloque de código a ejecutar por la conexión.
	 * @throws Exception - Si no existe la conexión.
	 */
	public static void executeQueryWhenAvilableWithConnection(MongoConnection connection, MongoSynchronizedQuery query) throws Exception{

		if (captain == null) throw new Exception("Se debe pedir la conexión con requestConnection primero.");
		if (connection == null) throw new Exception("Se debe pedir la conexión con requestConnection primero.");

		connection.runQueryWhenAvilable(query);

	}

	/**
	 * Ejecuta el bloque de código inmediatamente, no espera recursos ni el backgroud.
	 * @param connection - La conexión por la cuál se ejecuta el bloque. 
	 * @param query - El bloque de código a ejecutar por la conexión.
	 * @throws Exception - Si no existe la conexión.
	 */
	public static void executeQueryWithConnection(MongoConnection connection, MongoQuery query) throws Exception{

		if (captain == null) throw new Exception("Se debe pedir la conexión con requestConnection primero.");
		if (connection == null) throw new Exception("Se debe pedir la conexión con requestConnection primero.");

		connection.runQuery(query);

	}
	
	/**
	 * Inicializa Un nuevo manejador.
	 * @return Un nuevo manejador.
	 */
	private MongoManager createMongoManager(){
		return new MongoManager();
	}
}
