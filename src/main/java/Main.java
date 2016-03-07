import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class Main {
	private static URI getBaseURI(String hostname, int port) {
        return UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
    }

    protected static HttpServer startServer(URI uri) throws IOException {
        System.out.println("Starting grizzly...");
        ResourceConfig rc = new ResourceConfig().packages("recursos");
        return GrizzlyHttpServerFactory.createHttpServer(uri, rc);
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

        URI uri = getBaseURI(hostname, Integer.valueOf(port));

        final HttpServer httpServer = startServer(uri);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", uri, uri));
        if (isOnLocal) {
            System.in.read();
            httpServer.shutdown();
        } else {
            while (true) {
                System.in.read();
            }
        }
    }
}