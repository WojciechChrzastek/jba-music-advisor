package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpServerHandler {
  private static final String SPOTIFY_API_TOKEN_ENDPOINT_END = "/api/token";
  private static final String REDIRECT_URI = "http://localhost:8080";
  private static final String CLIENT_ID = "98138c41bf754e06a99bba3195392adb";
  private static final String CLIENT_SECRET = "3c843f1d5d5c4c49948064192cba9b3a";
  private static final String GRANT_TYPE = "authorization_code";
  private static String authCode = "";
  private static boolean hasCode;
  private static HttpServer server;

  private static void showAuthLink(String spotifyAccessServerPoint) {
    System.out.println("use this link to request the access code:");
    System.out.println(
            spotifyAccessServerPoint +
                    "/authorize?"
                    + "client_id=" + CLIENT_ID
                    + "&redirect_uri=" + REDIRECT_URI
                    + "&response_type=code");

    System.out.println("\nwaiting for code...");
  }

  static String handleServer(String spotifyServerAccessPoint) {
    try {
      if (server == null) {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.start();
        server.createContext("/",
                exchange -> {
                  String query = exchange.getRequestURI().getQuery();
                  String result;
                  if (query != null && query.contains("code")) {
                    authCode = query.substring(5);
                    result = "Got the code. Return back to your program.";
                    System.out.println("code received");
                    hasCode = true;
                  } else {
                    result = "Not found authorization code. Try again.";
                    System.out.println("code not received");
                  }
                  exchange.sendResponseHeaders(200, result.length());
                  exchange.getResponseBody().write(result.getBytes());
                  exchange.getResponseBody().close();
                }
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (server != null) {
      showAuthLink(spotifyServerAccessPoint);
      hasCode = false;

      while (!hasCode) {
        try {
          Thread.sleep(10);
          if (hasCode) {
            server.stop(1);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    return authCode;
  }

  static String getAccessToken(String authCode, String spotifyAccessServerPoint) {

    System.out.println("making http request for access_token...");

    String requestBody = "client_id=" + CLIENT_ID
            + "&client_secret=" + CLIENT_SECRET
            + "&grant_type=" + GRANT_TYPE
            + "&code=" + authCode
            + "&redirect_uri=" + REDIRECT_URI;

    HttpRequest requestForAccessToken = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .uri(URI.create(spotifyAccessServerPoint + SPOTIFY_API_TOKEN_ENDPOINT_END))
            .build();

    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse<String> responseWithAccessToken = null;
    try {
      responseWithAccessToken = client
              .send(requestForAccessToken,
                      HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    String fullToken = null;
    if (responseWithAccessToken != null) {
      fullToken = responseWithAccessToken.body();
    }

//    return parseAccessToken(fullToken);
    return fullToken;
  }

//  private static String parseAccessToken(final String bearerToken) {
//    JsonObject jo = JsonParser.parseString(bearerToken).getAsJsonObject();
//    return jo.get("access_token").getAsString();
//  }
}
