package persistencia;

/**
 * Clase para empaquetar las parejas de valores de JSON para una búsqueda en Mongo
 * @author danielsoto
 *
 */
public class KeyValueSearch{

	public enum SearchType{
		EQUALS, GREATER, LESS, ID;
	}
	
	private String key;
	private Object value;
	private SearchType type;
	
	/**
	 * Crea un objeto que representa una entrada JSON de busqueda.
	 * @param key - La llave de la columna que se desea filtrar
	 * @param value - El valor de comparacion para el filtro
	 * @param type - El tipo de comparacion del filtro, definida por el enum SearchType.
	 */
	public KeyValueSearch(String key, Object value, SearchType type){
		this.key = key;
		this.value = value;
		this.type = type;
	}
	
	public String getKey(){
		return key;
	}
	
	public Object getValue(){
		return value;
	}
	
	public SearchType getType(){
		return type;
	}
}
