package com.arquisoft.SATT.dao;

import java.util.ArrayList;

import org.bson.Document;

import com.arquisoft.SATT.mundo.Zona;
import com.arquisoft.SATT.mundo.EscenarioPremodelado.PerfilAlerta;
import com.arquisoft.SATT.utilidades.KeyValueSearch;
import com.arquisoft.SATT.utilidades.KeyValueSearch.SearchType;
import com.arquisoft.SATT.utilidades.MongoConnection;
import com.arquisoft.SATT.utilidades.MongoConnection.MongoQuery;
import com.arquisoft.SATT.utilidades.MongoManager;
import com.arquisoft.SATT.utilidades.SATTDB;

public class EscenarioPremodeladoDAO {

	private static String COLECCION = "escenariosPremodelados";
	private static String perfil = "";

	public static String getPerfilAlerta(double altura, double distancia, String zona){ 

		MongoConnection connection = SATTDB.requestConecction();
		try {
			connection.runQuery(new MongoQuery() {

				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();

					filters.add(new KeyValueSearch("zona", zona, SearchType.EQUALS));
					filters.add(new KeyValueSearch("altura_min", altura, SearchType.GREATER));
					filters.add(new KeyValueSearch("altura_max", altura, SearchType.LESS));
					filters.add(new KeyValueSearch("distancia_min", distancia, SearchType.GREATER));
					filters.add(new KeyValueSearch("distancia_max", distancia, SearchType.LESS));

					Document perfilDoc = manager.queryByFilters(COLECCION, filters).first();

					perfil = perfilDoc.getString("perfil");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return perfil;

	}

	/**
	 * No me borren esto porfa, es para generar los datos del modelo, que no es un get ni nada porque
	 * es estático entonces este main de abajo es solo para crear los datos en Mongo.
	 */
	public static void main(String[] args) {
		MongoConnection connection = SATTDB.requestConecction();
		try {
			////////////////////////////////////
			//GUAJIRA
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Guajira.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Magdalena
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Magdalena.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Sucre
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Sucre.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Córdoba
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Cordoba.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Antioquia
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Antioquia.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Chocó
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Choco.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Valle del Cauca
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Valle.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Cauca
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Cauca.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
			////////////////////////////////////
			//Nariño
			////////////////////////////////////
			connection.runQuery(new MongoQuery() {
				int alturaMax = 20;
				int distanciaMinima = 5;
				@Override
				public void query(MongoManager manager) {
					PerfilAlerta[] perfiles = {PerfilAlerta.precaucion, PerfilAlerta.informativo, PerfilAlerta.alerta
							, PerfilAlerta.alarma};
					double altura = 0;
					double distancia = 10000;
					while (distancia>distanciaMinima){
						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;
						if (dMin <= distanciaMinima){
							dMin = distanciaMinima+1;
						}
						distancia = dMin - 1;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración
						
						while (altura<alturaMax){
							double aMin = altura;
							double deltaAltura = (Math.random()*2) + 1;
							double aMax = altura + deltaAltura;
							if (aMax >= alturaMax){
								aMax = alturaMax-1;
							}
							altura = aMax+1;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];
							
							PerfilAlerta perfil = perfiles[(int)(Math.random()*5)];
							
							Document modelo = new Document("perfil",perfil.string());
							modelo.append("zona", Zona.Narino.string());
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);
							
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
