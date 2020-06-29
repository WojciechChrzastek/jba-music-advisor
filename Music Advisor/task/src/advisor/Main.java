package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static final Scanner scanner = new Scanner(System.in);
  private static final List<String> validActions = Arrays.asList(
          "auth", "new", "featured", "categories", "exit");
  private static boolean isAuthorized;
  private static final String clientId = "98138c41bf754e06a99bba3195392adb";
  private static String redirectUri = "http://localhost:8080";

  public static void main(String[] args) {
    redirectUri = determineRedirectUri(args);
    executeAction();
  }

  private static String determineRedirectUri(String[] args) {
    if (args.length == 2 && args[0].equals("-access")) {
      return args[1];
    } else {
      return redirectUri;
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

  private static void executeAction() {
    String action = takeActionInput();

    switch (action) {
      case "auth": {
        authUser(redirectUri);
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

  private static void authUser(String redirectUri) {
    //server response
    HttpServerHandler.serverHandler(redirectUri, clientId);

    printAuth(redirectUri);
    executeAction();
  }

  private static void printAuth(String redirectUri) {
    String authLink = "https://accounts.spotify.com/authorize?client_id=" +
            clientId +
            "&redirect_uri=" +
            redirectUri +
            "&response_type=code";
    System.out.println(authLink
            + "\n\n---SUCCESS---");
    isAuthorized = true;
  }

  private static boolean verifyAuth() {
    if (isAuthorized) {
      return true;
    } else {
      System.out.println("Please, provide access for application.");
      return false;
    }
  }

  private static void printNewReleases() {
    if (verifyAuth()) {
      System.out.println("---NEW RELEASES---\n" +
              "Mountains [Sia, Diplo, Labrinth]\n" +
              "Runaway [Lil Peep]\n" +
              "The Greatest Show [Panic! At The Disco]\n" +
              "All Out Life [Slipknot]");
    }
    executeAction();
  }

  private static void printFeaturedPlaylists() {
    if (verifyAuth()) {
      System.out.println("---FEATURED---\n" +
              "Mellow Morning\n" +
              "Wake Up and Smell the Coffee\n" +
              "Monday Motivation\n" +
              "Songs to Sing in the Shower");
    }
    executeAction();
  }

  private static void printAvailableCategories() {
    if (verifyAuth()) {
      System.out.println("---CATEGORIES---\n" +
              "Top Lists\n" +
              "Pop\n" +
              "Mood\n" +
              "Latin");
    }
    executeAction();
  }

  private static void printCategoryPlaylists(String action) {
    if (verifyAuth()) {
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
