package homevault;

/*This class implements a simple command-line shell for the HomeVault application. It provides an interactive interface for users to execute commands related to property data management and price estimation. The shell reads user input, processes commands, and displays appropriate responses or help information.*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shell {

    public void start() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in)
        );

        System.out.println();
        System.out.println("Type 'help' to see available commands.");
        System.out.println();

        boolean running = true;

        while (running) {
            try {
                System.out.print("homevault> ");
                String input = reader.readLine();

                if (input == null) {
                    break;
                }

                input = input.trim();

                if (input.isEmpty()) {
                    continue;
                }

                switch (input.toLowerCase()) {
                    case "help":
                        showHelp();
                        break;

                    case "exit":
                    case "quit":
                        System.out.println("Saving data...");
                        System.out.println("Thank you for using HomeVault.");
                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Unknown command: '" + input + "'."
                        );
                        System.out.println(
                                "Type 'help' to see available commands."
                        );
                }

            } catch (IOException exception) {
                System.out.println(
                        "Error while reading command: "
                                + exception.getMessage()
                );
                running = false;
            }
        }
    }

    private void showHelp() {
        System.out.println();
        System.out.println("HomeVault commands:");
        System.out.println("  help                          Show all commands");
        System.out.println("  import <csv-file>             Import property data");
        System.out.println("  list                          List stored properties");
        System.out.println("  search --location <name>      Search properties");
        System.out.println("  stats --location <name>       Show price statistics");
        System.out.println("  predict                       Estimate a house price");
        System.out.println("  save                          Save local data");
        System.out.println("  exit                          Close HomeVault");
        System.out.println();
    }
}