package com.arquisoft.SATT.utilidades;

import java.util.concurrent.PriorityBlockingQueue;

import com.sun.jersey.server.impl.uri.rules.TerminatingRule;

public class MongoConnection extends Thread implements Runnable {
	
	/**
	 * Una conexión con el manejador de Mongo
	 */
	private MongoManager managerForQuery;
	
	/**
	 * Una cola, para pruebas de múltiples solicitudes, aún en pruebas.
	 */
	private PriorityBlockingQueue<MongoSynchronizedQuery> queryQueue;
	
	/**
	 * Id de la conexión
	 */
	private int id;
	
	/**
	 * Estado que indica si está conectado
	 */
	private boolean connected;
	
	/**
	 * Estado que mide la inactividad de la conexión.
	 */
	private int inactive = -1;
	
	/**
	 * Constructor de la clase.
	 * @param manager - El manejador de mongo que será utilizado por esta conexión.
	 * @param id - El id de la conexión.
	 */
	public MongoConnection(MongoManager manager, int id){
		this.managerForQuery = manager;
		this.id = id;
		this.queryQueue = new PriorityBlockingQueue<MongoSynchronizedQuery>();
		this.connected = true;
	}
	
	/**
	 * Guarda un bloque para correrlo tan pronto sea posible. Aún en pruebas.
	 * @param block - El bloque a ejecutar.
	 */
	public void runQueryWhenAvilable(MongoSynchronizedQuery block){
		this.queryQueue.offer(block);
	}
	
	/**
	 * Ejecuta un bloque de uso del manejador. 
	 * @param block - Un bloque (MongoQuery) que utiliza un manejador genérico para realizar <br> las consultas.
	 * @throws Exception - Si el bloque no puede ser ejecutado ahora mismo. 
	 */
	public void runQuery(MongoQuery block) throws Exception{
		inactive = 0;
		block.query(managerForQuery);
		//terminateConnection();
	}
	
	/**
	 * Termina la conexión de la base de datos.
	 */
	private void terminateConnection(){
		connected = false;
		managerForQuery.finalize();
		System.out.println("Terminating connection...");
	}
	
	/**
	 * Declara la interface de una Query. Simplemente especifica que debe contener un manejador.
	 * @author danielsoto
	 *
	 */
	public interface MongoQuery{
		public void query(MongoManager manager);
	}
	
	/**
	 * Declara la interface de una Query de Background, la cual exige una función de terminación del bloque.
	 * @author danielsoto
	 *
	 */
	public interface MongoBackgroudQuery extends MongoQuery{
		public boolean ready();
	}
	
	/**
	 * @return El id de la conexión
	 */
	public int id(){
		return id;
	}

	@Override
	/**
	 * Ejecuta el Thread, el cual espera a que hayan nuevas solicitudes por un tiempo y luego se desconecta.
	 */
	public void run() {
//		while (connected){
////			System.out.println("Connected");
//			while (!queryQueue.isEmpty()){
//				inactive = -1;
//				MongoBackgroudQuery query = queryQueue.poll().query;
//				query.query(managerForQuery);
//				query.ready();
//			}
//			inactive++;
//			if (inactive>=10){
//				terminateConnection();
//				return;
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				terminateConnection();
//			}
//		}
	}
	
	@Deprecated
	/**
	 * Obliga a cerrar la conexión. No es recomendable usarlo, solo en casos muy específicos.
	 */
	public void forceQuit(){
		terminateConnection();
	}

}
