package logica;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.bson.Document;

import org.glassfish.jersey.internal.util.Base64;

import persistencia.SATTDB;
import persistencia.KeyValueSearch;
import persistencia.MongoConnection;
import persistencia.MongoManager;
import persistencia.ResponseSATT;
import persistencia.KeyValueSearch.SearchType;
import persistencia.MongoConnection.MongoQuery;

public class AuthenticationDAO {

	private static String json;
	private static Response.Status status = Response.Status.OK;
	
	public static Response auth(String email, String password) {
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<persistencia.KeyValueSearch>();
					filters.add(new KeyValueSearch("email", email, SearchType.EQUALS));
					filters.add(new KeyValueSearch("password", password, persistencia.KeyValueSearch.SearchType.EQUALS));
					Document usuario = manager.queryByFilters("Usuarios", filters).first();
					if(usuario != null) {
						String id = usuario.getObjectId("_id").toString();
						json = "{\"auth\":true,"
								+ "\"role\":\"" + usuario.getString("rol") + "\","
								+ "\"name\":\"" + usuario.getString("name") + "\","
								+ "\"id\":\"" + id + "\","
								+ "\"accessToken\":\"Basic " + Base64.encodeAsString(id + ":" + password) + "\"}";
						status = Response.Status.ACCEPTED;
					} else {
						json = "{\"auth\":false}";
						status = Response.Status.FORBIDDEN;	
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseSATT.buildResponse(json, status);
	}
	
	public static String authByID(String id, String token) {
		MongoConnection connection = SATTDB.requestConecction();
		try {
			SATTDB.executeQueryWithConnection(connection, new MongoQuery() {
				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<persistencia.KeyValueSearch>();
					filters.add(new KeyValueSearch("_id", id, SearchType.ID));
					filters.add(new KeyValueSearch("password", token, persistencia.KeyValueSearch.SearchType.EQUALS));
					Document usuario = manager.queryByFilters("Usuarios", filters).first();
					if(usuario != null) {
						json = usuario.getString("rol");
					} else {
						json = "{\"auth\":false}";
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
