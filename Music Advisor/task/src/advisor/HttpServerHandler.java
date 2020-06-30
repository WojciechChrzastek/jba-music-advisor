package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpServerHandler {

  public static void showAuthLink(String clientId, String redirectUri) {
    System.out.println("use this link to request the access code:");
    System.out.println("https://accounts.spotify.com/authorize?"
            + "client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=" + "code");
    System.out.println("waiting for code...");
  }

  public static String code = "";

  public static void serverHandler(String redirectUri, String clientId) throws InterruptedException {
    HttpServer server = null;
    try {
      server = HttpServer.create();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (server != null) {

      try {
        server.bind(new InetSocketAddress(8080), 0);
      } catch (IOException e) {
        e.printStackTrace();
      }
      server.start();

      showAuthLink(clientId, redirectUri);
      server.createContext("/",
              exchange -> {
                String query = exchange.getRequestURI().getQuery();
                String result;

                if (query != null && query.contains("code")) {
                  code = query.substring(5);
                  result = "Got the code. Return back to your program.";
                  System.out.println("code received");
                } else {
                  result = "Not found authorization code. Try again.";
                }
                exchange.sendResponseHeaders(200, result.length());
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();

                System.out.println(result);
              }
      );

      while (code.equals("")) {
          Thread.sleep(10);
      }
      server.stop(1);
    }
  }

  public static String getAccessToken(String redirectUri, String clientId) {

    System.out.println("making http request for access_token...");

//    String authUri = "https://accounts.spotify.com/authorize?" +
//            "client_id=" + "98138c41bf754e06a99bba3195392adb"
//            + "&client_secret=" + "3c843f1d5d5c4c49948064192cba9b3a"
//            + "&grant_type=" + "authorization_code"
//            + "&code=" + code
//            + "&redirect_uri=" + redirectUri;

    HttpRequest requestForAccessToken = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(
                    "client_id=" + "98138c41bf754e06a99bba3195392adb"
                            + "&client_secret=" + "3c843f1d5d5c4c49948064192cba9b3a"
                            + "&grant_type=" + "authorization_code"
                            + "&code=" + code
                            + "&redirect_uri=" + redirectUri))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .uri(URI.create("https://accounts.spotify.com/api/token"))
            .build();

    HttpResponse<String> responseWithAccessToken = null;
    try {
      responseWithAccessToken = HttpClient
              .newBuilder()
              .build()
              .send(requestForAccessToken,
                      HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    String fullToken = null;
    if (responseWithAccessToken != null) {
      fullToken = responseWithAccessToken.body();
    }

    return parseAccessToken(fullToken);
  }

  private static String parseAccessToken(final String bearerToken) {

    JsonObject jo = JsonParser.parseString(bearerToken).getAsJsonObject();

    return jo.get("access_token").getAsString();
  }
}
