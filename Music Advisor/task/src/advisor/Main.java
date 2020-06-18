package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> validActions = Arrays.asList(
            "auth", "new", "featured", "categories", "exit");
    private static boolean isAuthorized;
    private static String clientId = "98138c41bf754e06a99bba3195392adb";
    private static String redirectUri = "http://localhost:8080";

    public static void main(String[] args) {
        executeAction();
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
                printAuth(clientId, redirectUri);
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

    private static boolean verifyAuth() {
        if (isAuthorized) {
            return true;
        } else {
            System.out.println("Please, provide access for application.");
            return false;
        }
    }

    private static void printAuth(String clientId, String redirectUri) {
        String authLink = "https://accounts.spotify.com/authorize?client_id=" +
                clientId +
                "&redirect_uri=" +
                redirectUri +
                "&response_type=code";
        System.out.println(authLink
                + "\n\n---SUCCESS---");
        isAuthorized = true;
        executeAction();
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
