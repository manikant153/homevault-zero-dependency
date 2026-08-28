package homevault;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Shell {
    /*
    here PropertyRepository is injected into the Shell class through its constructor. This allows the Shell class to access the property data and perform operations like listing properties, searching, and displaying statistics. The Shell class handles user input and provides a command-line interface for interacting with the HomeVault application.
    and this.propertyRepository = propertyRepository; assigns the injected PropertyRepository instance to the Shell class's propertyRepository field, allowing it to be used throughout the class for various operations related to property data.
    */

    private final PropertyRepository propertyRepository;
    private final CsvImporter csvImporter;

    public Shell(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
        this.csvImporter = new CsvImporter();
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

                String[] commandParts = input.split("\\s+", 2);
String command = commandParts[0].toLowerCase();

switch (command) {
    case "help":
        showHelp();
        break;

    case "list":
        listProperties();
        break;

    case "import":
        if (commandParts.length < 2) {
            System.out.println("Usage: import <csv-file>");
        } else {
            importProperties(commandParts[1].trim());
        }
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

    private void importProperties(String filePath) {
    ImportResult result = csvImporter.importProperties(filePath);

    propertyRepository.addAllProperties(
            result.getImportedProperties()
    );

    System.out.println();
    System.out.println(
            "Imported properties: " + result.getImportedCount()
    );
    System.out.println(
            "Rejected rows: " + result.getErrorCount()
    );

    if (!result.getErrors().isEmpty()) {
        System.out.println("Import issues:");

        for (String error : result.getErrors()) {
            System.out.println("  - " + error);
        }
    }

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