import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
	public static final String BASE_URI = "http://localhost:8080/myapp/";

    protected static HttpServer startServer() throws IOException {
        System.out.println("Starting grizzly...");
<<<<<<< HEAD
        ResourceConfig rc = new ResourceConfig().packages("recursos");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
=======
        ResourceConfig rc = new PackagesResourceConfig("recursos");
        return GrizzlyServerFactory.createHttpServer(uri, rc);
>>>>>>> parent of 3b024a5... Updated to jersey 2
    }

    public static void main(String[] args) throws IOException {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null) {
            hostname = "localhost";
        }

        boolean isOnLocal = false;
        String port = System.getenv("PORT");
        if (port == null) {
            isOnLocal = true;
            port = "9998";
        }

<<<<<<< HEAD
        final HttpServer httpServer = startServer();
=======
        URI uri = getBaseURI(hostname, Integer.valueOf(port));

        HttpServer httpServer = startServer(uri);
>>>>>>> parent of 3b024a5... Updated to jersey 2
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        if (isOnLocal) {
            System.in.read();
            httpServer.stop();
        } else {
            while (true) {
                System.in.read();
            }
        }
    }
}