package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static final Scanner scanner = new Scanner(System.in);
  private static final List<String> validActions = Arrays.asList(
          "auth", "new", "featured", "categories", "exit");
  private static final String DEFAULT_SPOTIFY_ACCESS_SERVER_POINT = "https://accounts.spotify.com";
  private static String spotifyAccessServerPoint;
  private static boolean isAuthorized;

  public static void main(String[] args) {
    spotifyAccessServerPoint = determineSpotifyServerAccessPoint(args);
    try {
      executeAction();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String determineSpotifyServerAccessPoint(String[] args) {
    if (args.length == 2 && args[0].equals("-access")) {
      return args[1];
    } else {
      return DEFAULT_SPOTIFY_ACCESS_SERVER_POINT;
    }
  }

  private static String takeActionInput() {
    boolean isValidAction;
    String action;
    do {
      action = scanner.nextLine();
      isValidAction = validActions.contains(action) || action.matches("playlists [^\\s].+");
      if (!isValidAction) {
        System.out.println("Please input a valid action (auth, new, featured, categories, playlists [category], exit");
      }
    }
    while (!isValidAction);
    return action;
  }

  private static void executeAction() throws Exception {
    String action = takeActionInput();

    switch (action) {
      case "auth": {
        authUser(spotifyAccessServerPoint);
        break;
      }
      case "new": {
        printNewReleases();
        break;
      }
      case "featured": {
        printFeaturedPlaylists();
        break;
      }
      case "categories": {
        printAvailableCategories();
        break;
      }
      default: {
        printCategoryPlaylists(action);
        break;
      }
      case "exit": {
        exitApplication();
      }
    }
  }

  private static void authUser(String spotifyAccessServerPoint) throws Exception {
    String authCode = HttpServerHandler.handleServer(spotifyAccessServerPoint);
    String accessToken = HttpServerHandler.getAccessToken(authCode, spotifyAccessServerPoint);
    System.out.println("response: ");
    System.out.println(accessToken);
    confirmAuth(accessToken);
    executeAction();
  }

  private static void confirmAuth(String accessToken) {
    if (!accessToken.contains("error")) {
      System.out.println("\n---SUCCESS---");
      isAuthorized = true;
    } else {
      System.out.println("\n---ERROR---");
    }
  }

  private static boolean checkAuth() {
    if (isAuthorized) {
      return true;
    } else {
      System.out.println("Please, provide access for application.");
      return false;
    }
  }

  private static void printNewReleases() throws Exception {
    if (checkAuth()) {
      System.out.println("---NEW RELEASES---\n" +
              "Mountains [Sia, Diplo, Labrinth]\n" +
              "Runaway [Lil Peep]\n" +
              "The Greatest Show [Panic! At The Disco]\n" +
              "All Out Life [Slipknot]");
    }
    executeAction();
  }

  private static void printFeaturedPlaylists() throws Exception {
    if (checkAuth()) {
      System.out.println("---FEATURED---\n" +
              "Mellow Morning\n" +
              "Wake Up and Smell the Coffee\n" +
              "Monday Motivation\n" +
              "Songs to Sing in the Shower");
    }
    executeAction();
  }

  private static void printAvailableCategories() throws Exception {
    if (checkAuth()) {
      System.out.println("---CATEGORIES---\n" +
              "Top Lists\n" +
              "Pop\n" +
              "Mood\n" +
              "Latin");
    }
    executeAction();
  }

  private static void printCategoryPlaylists(String action) throws Exception {
    if (checkAuth()) {
      String playlistCategory = action.substring(action.indexOf(" ") + 1);

      System.out.println("---" + playlistCategory.toUpperCase() + " PLAYLISTS---\n" +
              "Walk Like A Badass\n" +
              "Rage Beats\n" +
              "Arab Mood Booster\n" +
              "Sunday Stroll");
    }
    executeAction();
  }

  private static void exitApplication() {
    System.out.println("---GOODBYE!---");
    scanner.close();
    System.exit(0);
  }

}
