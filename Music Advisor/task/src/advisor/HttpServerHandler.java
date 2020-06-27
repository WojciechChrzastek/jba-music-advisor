package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpServerHandler {

  public static void serverHandler() {
    HttpServer server = null;
    try {
      server = HttpServer.create();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      if (server != null) {
        server.bind(new InetSocketAddress(8080), 0);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    if (server != null) {
      server.createContext("/",
              new HttpHandler() {
                public void handle(HttpExchange exchange) throws IOException {
                  String sampleMessage = "message";
                  exchange.sendResponseHeaders(200, sampleMessage.length());
                  exchange.getResponseBody().write(sampleMessage.getBytes());
                  exchange.getResponseBody().close();
                }
              }
      );
    }


    if (server != null) {
      server.start();
    }


    HttpClient client = HttpClient.newBuilder().build();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080"))
            .GET()
            .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    if (response != null) {
      System.out.println(response.body());
    }


    if (server != null) {
      server.stop(1);
    }
  }
}
