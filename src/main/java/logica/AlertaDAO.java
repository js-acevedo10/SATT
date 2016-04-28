package logica;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.bson.Document;

import persistencia.KeyValueSearch;
import persistencia.KeyValueUpdate;
import persistencia.MongoConnection;
import persistencia.MongoManager;
import persistencia.ResponseSATT;
import persistencia.SATTDB;
import persistencia.KeyValueSearch.SearchType;
import persistencia.KeyValueUpdate.UpdateType;
import persistencia.MongoConnection.MongoQuery;

import com.google.gson.Gson;
import com.mongodb.util.JSON;

public class AlertaDAO {

	private static String json;

	private static Response.Status status = Response.Status.ACCEPTED;

	private static final String COLECCION = "alertas";

	public static ArrayList<Document> documentos = new ArrayList<Document>();

	private static AlertaDTO alertaDTO = null;

	//----------------------------------------------------------------------
	//GET
	//----------------------------------------------------------------------

	public static Response getAllAlertas() {
		json = "";
		documentos = new ArrayList<Document>();		
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					documentos = manager.queryByFilters(COLECCION, null).into(new ArrayList<Document>());
					if(documentos != null && !documentos.isEmpty()) {
						json = JSON.serialize(documentos);
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"No alertas found.\"}";
						status = Response.Status.NOT_FOUND;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return ResponseSATT.buildResponse(json, status);
	}

	public static Response getAlerta(String id){
		json = "";
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					KeyValueSearch kvs = new KeyValueSearch("_id", id, SearchType.ID);
					filters.add(kvs);
					Document alt = manager.queryByFilters(COLECCION, filters).first();
					if(alt != null) {
						Gson gson = new Gson();
						AlertaDTO alerta = new AlertaDTO();
						alerta = new AlertaDTO();
						alerta.set_id(alt.getObjectId("_id"));
						alerta.setId(alt.getObjectId("_id").toHexString());
						alerta.setAltura(alt.getDouble("altura"));
						alerta.setPerfil(alt.getString("perfil"));
						alerta.settLlegada((long)alt.getInteger("tLlegada"));
						alerta.setZona(alt.getString("zona"));
						
						json = gson.toJson(alerta);
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Error Fetching Alerta with ID: " + id + ".\"}";
						status = Response.Status.NOT_FOUND;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching FBO with ID: " + id + ".\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return ResponseSATT.buildResponse(json, status);

	}

	//----------------------------------------------------------------------
	//POST
	//----------------------------------------------------------------------

	public static AlertaDTO addAlerta(AlertaDTO alerta) {
		MongoConnection connection = SATTDB.requestConecction();
		Gson gson = new Gson();
		json = gson.toJson(alerta);
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					Document alertaDoc = Document.parse(json);
					if(manager.persist(alertaDoc, COLECCION)) {						
						alertaDTO = new AlertaDTO();
						alertaDTO.set_id(alertaDoc.getObjectId("_id"));
						alertaDTO.setId(alertaDoc.getObjectId("_id").toHexString());
						alertaDTO.setAltura(alertaDoc.getDouble("altura"));
						alertaDTO.setPerfil(alertaDoc.getString("perfil"));
						alertaDTO.settLlegada((long)alertaDoc.getInteger("tLlegada"));
						alertaDTO.setZona(alertaDoc.getString("zona"));
						status = Response.Status.OK;
					} else {
						json = "{\"exception\":\"Alerta not added.\"}";
						status = Response.Status.NOT_MODIFIED;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"exception\":\"Error Fetching Alertas Collection.\"}";
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return alertaDTO;
	}

	public static void updateAlerta(String id, String perfil, double altura){
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {

				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();
					ArrayList<KeyValueUpdate> updates = new ArrayList<KeyValueUpdate>();

					filters.add(new KeyValueSearch("_id", id, SearchType.ID));
					updates.add(new KeyValueUpdate("altura", altura, UpdateType.SET));
					if(manager.updateFirst(COLECCION, filters, updates)){
						//						alerta = new Gson().fromJson(manager.queryByFilters(COLECCION, filters).first().toJson(), AlertaDTO.class);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		//		return alerta;
	}

}
