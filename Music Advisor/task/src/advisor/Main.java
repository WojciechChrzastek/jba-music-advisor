package advisor;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        determineAction();
    }

    private static void determineAction() {
        String action;
        action = scanner.next();

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
