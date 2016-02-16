
package com.arquisoft.SATT;

import com.arquisoft.SATT.dao.ZoneFinderDAO;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		final String baseUri = "http://localhost:"+(System.getenv("PORT")!=null?System.getenv("PORT"):"9998")+"/";
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages","com.arquisoft.SATT.recursos");
        
        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        ZoneFinderDAO.loadPuntosCardinales();
        System.out.println(String.format("Jersey started with WADL available at %sapplication.wadl.",baseUri, baseUri));
	}
}
