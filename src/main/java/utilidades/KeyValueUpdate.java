package utilidades;


public class KeyValueUpdate {

	/**
	 * Enum de UpdateType para representar el tipo de actualización que se desea.
	 * Leer la implementacion y la documentacion para entender bien.
	 * @author danielsoto
	 *
	 */
	public enum UpdateType{
		ADD, //A un campo numerico adiciona el valor enviado.
		MULTIPLY, //A un campo numerico le multiplica el valor enviado.
		RENAME, //Renombra un campo con el valor enviado.
		SET, //En el campo seleccionado inserta el valor enviado.
		REMOVE, //Deja vacio el campo seleccionado. El valor enviado puede ser cualquier cosa.
		KEEP_MIN, //En el campo seleccionado deja el minimo entre el valor actual y el valor enviado.
		KEEP_MAX, //En el campo seleccionado deja el maximo entre el valor actual y el valor enviado.
		TO_CURRENT_DATE, //En el campo seleccionado inserta la fecha actual en formato Date.
		INSERT; //Agrega todos los elementos envíados al campo.
	}
	
	/**
	 * El nombre de la columna 
	 */
	private String key;
	/**
	 * El valor que se envía para actualizar.
	 */
	private Object value;
	/**
	 * El tipo de actualización a realizar.
	 */
	private UpdateType type;
	
	/**
	 * Crea un objeto que representa una entrada JSON de actualizacion.
	 * @param key - La llave de la columna que se desea filtrar
	 * @param value - El valor de comparacion para el filtro
	 * @param type - El tipo de comparacion del filtro, definida por el enum UpdateType.
	 */
	public KeyValueUpdate(String key, Object value, UpdateType type){
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
	
	public UpdateType getType(){
		return type;
	}
}
