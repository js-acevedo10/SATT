package com.arquisoft.SATT.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

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

	public static String getPerfilAlerta(final double altura, final double distancia, final String zona){ 

		MongoConnection connection = SATTDB.requestConecction();
		try {
			connection.runQuery(new MongoQuery() {

				@Override
				public void query(MongoManager manager) {
					ArrayList<KeyValueSearch> filters = new ArrayList<KeyValueSearch>();

					filters.add(new KeyValueSearch("zona", zona, SearchType.EQUALS));
					filters.add(new KeyValueSearch("altura_min", altura, SearchType.LESS));
					filters.add(new KeyValueSearch("altura_max", altura, SearchType.GREATER));
					filters.add(new KeyValueSearch("distancia_min", distancia, SearchType.LESS));
					filters.add(new KeyValueSearch("distancia_max", distancia, SearchType.GREATER));

					Document perfilDoc = manager.queryByFilters(COLECCION, filters).first();

					if (perfilDoc==null){
						perfil = PerfilAlerta.informativo.getName();
					}else{
						perfil = perfilDoc.getString("perfil");
					}
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
		System.out.println("Bienvenido al creador de escenarios premodelados");
		System.out.println("Ingrese la zona para la cual desea crear los valores");
		Zona[] zonasPosibles = Zona.values();
		int i =0;
		for (; i < zonasPosibles.length; i++) {
			System.out.println(i+": "+zonasPosibles[i]);
		}
		System.out.println((i)+": Auto generar todas las zonas");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int seleccion = -1;
		
		try {
			seleccion = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(seleccion == i){
			i =0;
			for (; i < zonasPosibles.length; i++) {
				generarPremodeladoZona(zonasPosibles[i].getNombre());
			}
		}
		else{
			generarPremodeladoZona(zonasPosibles[seleccion].getNombre());
		}

	}
	
	private static void generarPremodeladoZona(final String zona){
		MongoConnection connection = SATTDB.requestConecction();
		try {
			connection.runQuery(new MongoQuery() {
				double alturaMax = 20.0;
				double distanciaMinima = 5.0;
				int rangosAlturas = 4;
				int rangosDistancias = 5;
				@Override
				public void query(MongoManager manager) {
					double altura = 0;
					double distancia = 10000;
					int distancias = 0;
					int alturas = 0;
					while (distancia>distanciaMinima && distancias<rangosDistancias-1){
						ArrayBlockingQueue<PerfilAlerta> perfiles = new ArrayBlockingQueue<PerfilAlerta>(4);
						perfiles.offer(PerfilAlerta.precaucion);
						perfiles.offer(PerfilAlerta.informativo);
						perfiles.offer(PerfilAlerta.alerta);
						perfiles.offer(PerfilAlerta.alarma);
						
						altura = 0;
						alturas = 0;

						double dMax = distancia;
						double deltaDistancia = (Math.random()*2000) + 1;
						double dMin = distancia - deltaDistancia;

						distancia = dMin - 0.00001;
						//Hasta acá el rango de distancia está definido por [dMin, dMax]
						//Distancia es el límite superior en la próxima iteración

						while (altura<alturaMax && alturas<rangosAlturas-1){
							double aMin = altura;
							double deltaAltura = (Math.random()*4) + 1;
							double aMax = altura + deltaAltura;

							altura = aMax+0.00001;
							//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];

							PerfilAlerta perfil = perfiles.poll();

							Document modelo = new Document("perfil",perfil.getName());
							modelo.append("zona", zona);
							modelo.append("altura_min", aMin);
							modelo.append("altura_max", aMax);
							modelo.append("distancia_min", dMin);
							modelo.append("distancia_max", dMax);
							manager.persist(modelo, COLECCION);

//							System.out.println(perfil.string()+" "+zona+" "+aMin+" "+aMax+" "+dMin+" "+dMax);
							alturas++;
						}
						double aMin = altura;

						PerfilAlerta perfil = perfiles.poll();

						Document modelo = new Document("perfil",perfil.getName());
						modelo.append("zona", zona);
						modelo.append("altura_min", aMin);
						modelo.append("altura_max", alturaMax);
						modelo.append("distancia_min", dMin);
						modelo.append("distancia_max", dMax);
						manager.persist(modelo, COLECCION);

//						System.out.println(perfil.string()+" "+zona+" "+aMin+" "+alturaMax+" "+dMin+" "+dMax);

						altura=0;
						distancias++;
					}
					
					ArrayBlockingQueue<PerfilAlerta> perfiles = new ArrayBlockingQueue<PerfilAlerta>(4);
					perfiles.offer(PerfilAlerta.precaucion);
					perfiles.offer(PerfilAlerta.informativo);
					perfiles.offer(PerfilAlerta.alerta);
					perfiles.offer(PerfilAlerta.alarma);

					double dMax = distancia;
					alturas = 0;
					altura = 0;
					
					while (altura<alturaMax && alturas<rangosAlturas-1){
						double aMin = altura;
						double deltaAltura = (Math.random()*4) + 1;
						double aMax = altura + deltaAltura;

						altura = aMax+0.00001;
						//Hasta acá tenemos el perfil que define el rango [dMin, dMax];[aMin, aMax];

						PerfilAlerta perfil = perfiles.poll();

						Document modelo = new Document("perfil",perfil.getName());
						modelo.append("zona", zona);
						modelo.append("altura_min", aMin);
						modelo.append("altura_max", aMax);
						modelo.append("distancia_min", distanciaMinima);
						modelo.append("distancia_max", dMax);
						manager.persist(modelo, COLECCION);
						
//						System.out.println(perfil.string()+" "+zona+" "+aMin+" "+aMax+" "+distanciaMinima+" "+dMax);


						alturas++;
					}
					double aMin = altura;

					PerfilAlerta perfil = perfiles.poll();

					Document modelo = new Document("perfil",perfil.getName());
					modelo.append("zona", zona);
					modelo.append("altura_min", aMin);
					modelo.append("altura_max", alturaMax);
					modelo.append("distancia_min", distanciaMinima);
					modelo.append("distancia_max", dMax);
					manager.persist(modelo, COLECCION);
					
//					System.out.println(perfil.string()+" "+zona+" "+aMin+" "+alturaMax+" "+distanciaMinima+" "+dMax);

				}
			});
		}catch (Exception e){
			
		}
	}

}
