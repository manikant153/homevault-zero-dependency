package homevault;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Shell {

    private final PropertyRepository propertyRepository;

    public Shell(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

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

                    case "list":
                        listProperties();
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

    private void listProperties() {
        List<Property> properties = propertyRepository.getAllProperties();

        if (properties.isEmpty()) {
            System.out.println("No properties available.");
            return;
        }

        System.out.println();
        System.out.println(
                "ID     LOCATION             AREA  BED   BATH  AGE          PRICE"
        );
        System.out.println(
                "------------------------------------------------------------------"
        );

        for (Property property : properties) {
            System.out.println(property.toDisplayRow());
        }

        System.out.println(
                "------------------------------------------------------------------"
        );
        System.out.println("Total properties: " + properties.size());
        System.out.println();
    }
}