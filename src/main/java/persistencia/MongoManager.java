package persistencia;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import persistencia.KeyValueSearch.SearchType;

public class MongoManager {

	private static final String DBURI = System.getenv("PROD_MONGODB");

	private static MongoClient mongo = null;
	private static MongoClientURI URI = null;
	private static MongoDatabase db = null;
	//METODOS

	/**
	 * Inicializa la conexion con la base de datos
	 * @return La base de datos en Mongo para buscar.
	 */
	private static MongoDatabase initMongoDB(){
		if (mongo==null || URI == null || db == null){ 
			URI  = new MongoClientURI(DBURI, MongoClientOptions.builder().minConnectionsPerHost(2).connectionsPerHost(5)); 
			mongo = new MongoClient(URI);
			db = mongo.getDatabase(URI.getDatabase());
		}
		return db;
	}


	/**
	 * Realiza una busqueda simple en la base de datos. Se define como simple una busqueda que 
	 * cumpla con N condiciones especificas. (No maneja que cumpla ALGUNA de las condiciones)
	 * @param COLECCION - El string de la coleccion en Mongo que se desea buscar
	 * @param filters - Un arreglo de clase KeyValueSearch que especifica las condiciones <br> de las busqueda.
	 * @return El objeto de FindIterable para aplicar el bloque ForEach. Un ejemplo del bloque <br> se ecnuentra comentado en la declaracion de este metodo.
	 */
	public FindIterable<Document> queryByFilters(String COLECCION, ArrayList<KeyValueSearch> filters){

		FindIterable<Document> results = null;
		if (filters != null){
			Document jsonFilters = new Document();
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}
			results = MongoManager.initMongoDB().getCollection(COLECCION).find(jsonFilters);
		}
		else{
			results = MongoManager.initMongoDB().getCollection(COLECCION).find();
		}

		return results;

	}

	/**
	 * Guarda un documento en la base de datos, en la colección que entra por parámetro.
	 * @param document - El Documento a guardar
	 * @param collection - El nombre de la colección donde se desea guardar.
	 * @return Si se guardó con éxito.
	 */
	public boolean persist(Document document, String COLECCION){
		MongoManager.initMongoDB().getCollection(COLECCION).insertOne(document);
		return true;
	}

	/**
	 * Borra todos los elementos que cumplan con las condiciones dadas por parámetro.
	 * @param collection - El nombre de la colección sobre la cual se desea borrar.
	 * @param filters - Los filtros que se deben cumplir para que un elemento se borre.
	 * @return - True Si se borró algún elemento, False de lo contrario.
	 */
	public boolean deleteAll(String collection, ArrayList<KeyValueSearch> filters){
		DeleteResult deleted = null;
		boolean deleteOne = false;
		if (filters != null){
			Document jsonFilters = new Document();
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					deleteOne = true;
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}
			if (deleteOne){
				deleted = MongoManager.initMongoDB().getCollection(collection).deleteOne(jsonFilters);
				if (deleted.getDeletedCount()>0){
					return true;
				}
			}
			else{
				deleted = MongoManager.initMongoDB().getCollection(collection).deleteMany(jsonFilters);
				if (deleted.getDeletedCount()>0){
					return true;
				}
			}

		}

		return false;

	}

	/**
	 * Borra el primer item de la colección que satizfaga los filtros.
	 * @param collection - El nombre de la colección sobre la cual se desea borrar.
	 * @param filters - Los filtros que se deben cumplir para que un elemento se borre.
	 * @return - True Si se borró algún elemento, False de lo contrario.
	 */
	public boolean deleteFirst(String collection, ArrayList<KeyValueSearch> filters){
		DeleteResult deleted = null;
		if (filters != null){
			Document jsonFilters = new Document();
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}
			deleted = MongoManager.initMongoDB().getCollection(collection).deleteOne(jsonFilters);
			if (deleted.getDeletedCount()>0){
				return true;
			}

		}

		return false;

	}

	/**
	 * Actualiza la información de todos los elementos que cumplan los filtros. Para pasar los updates es <br> igual que pasar los 
	 * filtros pero con otro objeto KeyValueUpdate.
	 * @param collection - El nombre de la coleccion sobre la cual se va a actualizar
	 * @param filters - Los filtros de los elementos que se desean actualizar
	 * @param updates - Las actualizaciones que se desean realizazr sobre esos elementos.
	 * @return - True si se actualizo al menos un elemento, false de lo contrario.
	 */
	public boolean updateAll(String collection, ArrayList<KeyValueSearch> filters, ArrayList<KeyValueUpdate> updates){
		Document jsonFilters = new Document();
		Document jsonUpdates = new Document();
		UpdateResult result = null;
		if (filters != null){
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}
		if (updates != null){
			ArrayList<KeyValueSearch> sets = new ArrayList<KeyValueSearch>();
			for (KeyValueUpdate update : updates){
				switch (update.getType()){
				case ADD:
					jsonUpdates.append("$inc:", new Document(update.getKey(), update.getValue()));
					break;

				case MULTIPLY:
					jsonUpdates.append("$mul", new Document(update.getKey(), update.getValue()));
					break;

				case SET:
					sets.add(new KeyValueSearch(update.getKey(), update.getValue(), SearchType.EQUALS));
					break;

				case REMOVE:
					jsonUpdates.append("$unset", new Document(update.getKey(), "1"));
					break;

				case RENAME:
					jsonUpdates.append("$rename", new Document(update.getKey(), update.getValue()));
					break;

				case KEEP_MIN:
					jsonUpdates.append("$min", new Document(update.getKey(), update.getValue()));
					break;

				case KEEP_MAX:
					jsonUpdates.append("$max", new Document(update.getKey(), update.getValue()));
					break;

				case TO_CURRENT_DATE:
					jsonUpdates.append("$currentDate", new Document(update.getKey(), true));
					break;
				case INSERT:
					jsonUpdates.append("$addToSet", new Document(update.getKey(), update.getValue()));
					break;
				}
			}
			Document jsonSets = new Document();
			boolean seted = false;
			for (KeyValueSearch set : sets) {
				seted = true;
				jsonSets.append(set.getKey(), set.getValue());
			}
			if (seted){
				jsonUpdates.append("$set", jsonSets);
			}

			result = MongoManager.initMongoDB().getCollection(collection).updateMany(jsonFilters, jsonUpdates);
			if (result.getModifiedCount()>0){
				return true;
			}

		}
		return false;
	}

	/**
	 * Actualiza la información del primer elemento que cumpla los filtros. Para pasar los updates es <br> igual que pasar los 
	 * filtros pero con otro objeto KeyValueUpdate.
	 * @param COLECCION - El nombre de la coleccion sobre la cual se va a actualizar.
	 * @param filters - Los filtros del elemento que se desean actualizar.
	 * @param updates - Las actualizaciones que se desean realizazr sobre ese elemento.
	 * @return - True si se actualizo el elemento, false de lo contrario.
	 */
	public boolean updateFirst(String COLECCION, ArrayList<KeyValueSearch> filters, ArrayList<KeyValueUpdate> updates){
		Document jsonFilters = new Document();
		Document jsonUpdates = new Document();
		UpdateResult result = null;
		if (filters != null){
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}
		if (updates != null){
			ArrayList<KeyValueSearch> sets = new ArrayList<KeyValueSearch>();
			for (KeyValueUpdate update : updates){
				switch (update.getType()){
				case ADD:
					jsonUpdates.append("$inc:", new Document(update.getKey(), update.getValue()));
					break;

				case MULTIPLY:
					jsonUpdates.append("$mul", new Document(update.getKey(), update.getValue()));
					break;

				case SET:
					sets.add(new KeyValueSearch(update.getKey(), update.getValue(), SearchType.EQUALS));
					break;

				case REMOVE:
					jsonUpdates.append("$unset", new Document(update.getKey(), "1"));
					break;

				case RENAME:
					jsonUpdates.append("$rename", new Document(update.getKey(), update.getValue()));
					break;

				case KEEP_MIN:
					jsonUpdates.append("$min", new Document(update.getKey(), update.getValue()));
					break;

				case KEEP_MAX:
					jsonUpdates.append("$max", new Document(update.getKey(), update.getValue()));
					break;

				case TO_CURRENT_DATE:
					jsonUpdates.append("$currentDate", new Document(update.getKey(), true));
					break;
				case INSERT:
					jsonUpdates.append("$addToSet", new Document(update.getKey(), update.getValue()));
					break;
				}
			}
			Document jsonSets = new Document();
			boolean seted = false;
			for (KeyValueSearch set : sets) {
				seted = true;
				jsonSets.append(set.getKey(), set.getValue());
			}
			if (seted){
				jsonUpdates.append("$set", jsonSets);
			}

			result = MongoManager.initMongoDB().getCollection(COLECCION).updateOne(jsonFilters, jsonUpdates);
			if (result.getModifiedCount()>0){
				return true;
			}
		}
		return false;
	}

	/**
	 * Metodo que aplica updates a objetos que se encuentran dentro de un Array
	 * @param COLECCION - El nombre de la colección que se desea modificar
	 * @param filters - Los filtros para encontrar el objeto que tiene el arreglo.
	 * @param arrayName - El nombre de la columna que es el arreglo.
	 * @param filtersInsideArray - Filtros para encontrar el objeto que se desea modificar dentro del arreglo.
	 * @param updates - Los updates que se desea realizar en el o los objetos encontrados.
	 * @return True si se actualizó algún objeto, false de lo contrario.
	 */
	public boolean updateInArray(String COLECCION, ArrayList<KeyValueSearch> filters, String arrayName, ArrayList<KeyValueSearch> filtersInsideArray, ArrayList<KeyValueUpdate> updates){
		Document jsonFilters = new Document();
		Document jsonInArrayFilters = new Document();
		Document jsonUpdates = new Document();

		UpdateResult result = null;
		if (filters != null){
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}
		if (filtersInsideArray != null){
			for (KeyValueSearch filter : filtersInsideArray){
				switch (filter.getType()) {
				case EQUALS:
					jsonInArrayFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonInArrayFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonInArrayFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonInArrayFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}
		if (updates != null){
			ArrayList<KeyValueSearch> sets = new ArrayList<KeyValueSearch>();
			for (KeyValueUpdate update : updates){
				switch (update.getType()){
				case ADD:
					jsonUpdates.append("$inc:", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;

				case MULTIPLY:
					jsonUpdates.append("$mul", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;

				case SET:
					sets.add(new KeyValueSearch(arrayName+".$."+update.getKey(), update.getValue(), SearchType.EQUALS));
					break;

				case REMOVE:
					jsonUpdates.append("$unset", new Document(arrayName+".$."+update.getKey(), "1"));
					break;

				case RENAME:
					jsonUpdates.append("$rename", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;

				case KEEP_MIN:
					jsonUpdates.append("$min", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;

				case KEEP_MAX:
					jsonUpdates.append("$max", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;

				case TO_CURRENT_DATE:
					jsonUpdates.append("$currentDate", new Document(arrayName+".$."+update.getKey(), true));
					break;
				case INSERT:
					jsonUpdates.append("$addToSet", new Document(arrayName+".$."+update.getKey(), update.getValue()));
					break;
				}
			}
			Document jsonSets = new Document();
			boolean seted = false;
			for (KeyValueSearch set : sets) {
				seted = true;
				jsonSets.append(set.getKey(), set.getValue());
			}
			if (seted){
				jsonUpdates.append("$set", jsonSets);
			}

			jsonFilters.append(arrayName, new Document("$elemMatch", jsonInArrayFilters));
			result = MongoManager.initMongoDB().getCollection(COLECCION).updateMany(jsonFilters, jsonUpdates);
			if (result.getModifiedCount()>0){
				return true;
			}
		}
		return false;
	}

	/**
	 * Metodo que elimina objetos que se encuentran dentro de un Array
	 * @param COLECCION - El nombre de la colección que se desea modificar
	 * @param filters - Los filtros para encontrar el objeto que tiene el arreglo.
	 * @param arrayName - El nombre de la columna que es el arreglo.
	 * @param filtersInsideArray - Filtros para encontrar el objeto que se desea eliminar dentro del arreglo.
	 * @return True si se eliminó algún objeto, false de lo contrario.
	 */
	public boolean deleteInArray(String COLECCION, ArrayList<KeyValueSearch> filters, String arrayName, ArrayList<KeyValueSearch> filtersInsideArray){
		Document jsonFilters = new Document();
		Document jsonInArrayFilters = new Document();

		UpdateResult result = null;
		if (filters != null){
			for (KeyValueSearch filter : filters){
				switch (filter.getType()) {
				case EQUALS:
					jsonFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}
		if (filtersInsideArray != null){
			for (KeyValueSearch filter : filtersInsideArray){
				switch (filter.getType()) {
				case EQUALS:
					jsonInArrayFilters.append(filter.getKey(), filter.getValue());
					break;

				case GREATER:
					jsonInArrayFilters.append(filter.getKey(), new Document("$gt", filter.getValue()));
					break;

				case LESS:
					jsonInArrayFilters.append(filter.getKey(), new Document("$lt", filter.getValue()));
					break;

				case ID:
					jsonInArrayFilters.append(filter.getKey(), new ObjectId((String)filter.getValue()));
					break;
				default:
					break;
				}
			}

		}

		result = MongoManager.initMongoDB().getCollection(COLECCION).updateMany(jsonFilters, new Document("$pull", jsonInArrayFilters));
		if (result.getModifiedCount()>0){
			return true;
		}

		return false;
	}
	/**
	 * Finaliza la conexión con la base de datos.
	 */
	public void finalize(){
		mongo.close();
		mongo = null;
		URI = null;
	}


}
