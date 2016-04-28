package persistencia;

import persistencia.MongoConnection.MongoBackgroudQuery;
import persistencia.SATTDB.QueryPriority;

/**
 * Clase que maneja se incorpora en una cola de tareas con prioridad.
 * @author danielsoto
 *
 */
public class MongoSynchronizedQuery implements Comparable<MongoSynchronizedQuery>{

	/**
	 * El bloque que está encapsulado por la clase.
	 */
	public MongoBackgroudQuery query;
	
	/**
	 * La prioridad dentro de la cola.
	 */
	private QueryPriority priority;
	
	/**
	 * El constructor de la clase. Recibe un bloque de Background (Muy parecido al normal) y una prioridad.
	 * @param query - El bloque BG que se desea ejecutar cuando sea posible.
	 * @param priority - La prioridad de la tarea a ejecutar.
	 */
	public MongoSynchronizedQuery(MongoBackgroudQuery query, QueryPriority priority) {
		this.query = query;
		this.priority = priority;
	}

	@Override
	/**
	 * El comparador según la prioridad
	 */
	public int compareTo(MongoSynchronizedQuery o) {
		return this.priority.ordinal()-o.priority.ordinal();
	}
}
