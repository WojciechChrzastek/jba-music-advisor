package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> validActions = Arrays.asList(
            "new", "featured", "categories", "exit");

    public static void main(String[] args) {
        executeAction(takeActionInput());
    }

    private static String takeActionInput() {
        boolean isValidAction;
        String action;
        do {
            action = scanner.nextLine();
            isValidAction = validActions.contains(action) || action.matches("playlists \\w+|playlists [^\\s]");
            if (!isValidAction) {
                System.out.println("Please input a valid action (new, featured, categories, playlists b, exit");
            }
        }
        while (!isValidAction);
        return action;
    }

    private static void executeAction(String action) {

        switch (action) {
            case "new" : {
                printNewReleases();
                break;
            }
            case "featured" : {
                printFeaturedPlaylists();
                break;
            }
            case "categories" : {
                printAvailableCategories();
                break;
            }
            case "playlists" : {
                printCategoryPlaylists();
                break;
            }
            case "exit" : {
                exitApplication();
            }
        }
    }

    private static void printNewReleases() {
    }

    private static void printFeaturedPlaylists() {
    }

    private static void printAvailableCategories() {
    }

    private static void printCategoryPlaylists() {
    }

    private static void exitApplication() {
    }

}
