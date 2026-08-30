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
    private final PropertySearchService propertySearchService;
    private final StatisticsService statisticsService;
    private final PredictionEngine predictionEngine;
    private final PersistenceManager persistenceManager;

public Shell(PropertyRepository propertyRepository) {
    this.propertyRepository = propertyRepository;
    this.csvImporter = new CsvImporter();
    this.propertySearchService = new PropertySearchService();
    this.statisticsService = new StatisticsService();
    this.predictionEngine = new PredictionEngine();
    this.persistenceManager = new PersistenceManager();
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

                // input will store the all commandsof shell to perform the particluar task
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
    case "search":
    handleSearch(commandParts);
    break;

    case "stats":
    handleStatistics(commandParts);
    break;

    case "import":
        if (commandParts.length < 2) {
            System.out.println("Usage: import <csv-file>");
        } else {
            importProperties(commandParts[1].trim());
        }
        break;

    case "predict":
    handlePrediction(commandParts);
    break;
    case "save":
    saveProperties();
    break;
    case "exit":
    case "quit":
    saveProperties();
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
        System.out.println("  search [filters]              Search properties");
        System.out.println("    --location <name> --bedrooms <count>");
        System.out.println("    --min-price <amount> --max-price <amount>");
        System.out.println("    --min-area <sqft> --max-area <sqft>");
        System.out.println("  stats                         Show overall property statistics");
        System.out.println("  stats --location <name>       Show statistics for one location");
        System.out.println("  predict [details]             Estimate a house price");
        System.out.println("    --location <name> --area <sqft> --bedrooms <count>");
        System.out.println("    --bathrooms <count> --age <years>");
        System.out.println("  save                          Save properties to local storage");
        System.out.println("  exit                          Close HomeVault");
        System.out.println();
    }

    private void handleSearch(String[] commandParts) {
    if (commandParts.length < 2) {
        System.out.println(
                "Usage: search [--location <name>] "
                        + "[--bedrooms <count>] "
                        + "[--min-price <amount>] "
                        + "[--max-price <amount>] "
                        + "[--min-area <sqft>] "
                        + "[--max-area <sqft>]"
        );
        return;
    }

    try {
        SearchOptions options = parseSearchOptions(commandParts[1]);

        List<Property> results = propertySearchService.search(
                propertyRepository.getAllProperties(),
                options
        );

        printSearchResults(results);

    } catch (IllegalArgumentException exception) {
        System.out.println("Search error: " + exception.getMessage());
    }
}

    private void handleStatistics(String[] commandParts) {
    try {
        List<Property> properties = propertyRepository.getAllProperties();
        String location = null;

        if (commandParts.length > 1) {
            String[] tokens = commandParts[1].trim().split("\\s+");

            if (tokens.length != 2 || !tokens[0].equals("--location")) {
                System.out.println(
                        "Usage: stats [--location <name>]"
                );
                return;
            }

            location = tokens[1];

            SearchOptions options = new SearchOptions();
            options.setLocation(location);

            properties = propertySearchService.search(
                    properties,
                    options
            );
        }

        if (properties.isEmpty()) {
            if (location == null) {
                System.out.println("No properties available.");
            } else {
                System.out.println(
                        "No properties found for location: " + location
                );
            }
            return;
        }

        PropertyStatistics statistics =
                statisticsService.calculate(properties);

        printStatistics(statistics, location);

    } catch (IllegalArgumentException exception) {
        System.out.println(
                "Statistics error: " + exception.getMessage()
        );
    }
}

    private void handlePrediction(String[] commandParts) {
    if (commandParts.length < 2) {
        printPredictionUsage();
        return;
    }

    try {
        PredictionRequest request =
                parsePredictionRequest(commandParts[1]);

        PredictionResult result = predictionEngine.predict(
                propertyRepository.getAllProperties(),
                request
        );

        printPredictionResult(result);

    } catch (IllegalArgumentException exception) {
        System.out.println(
                "Prediction error: " + exception.getMessage()
        );
    }
}

private PredictionRequest parsePredictionRequest(String arguments) {
    String location = null;
    Double areaSqFt = null;
    Integer bedrooms = null;
    Integer bathrooms = null;
    Integer ageYears = null;

    String[] tokens = arguments.trim().split("\\s+");

    for (int index = 0; index < tokens.length; index++) {
        String option = tokens[index];

        if (index + 1 >= tokens.length) {
            throw new IllegalArgumentException(
                    "Missing value for " + option
            );
        }

        String value = tokens[++index];

        switch (option) {
            case "--location":
                location = value;
                break;

            case "--area":
                areaSqFt = Double.parseDouble(value);
                break;

            case "--bedrooms":
                bedrooms = Integer.parseInt(value);
                break;

            case "--bathrooms":
                bathrooms = Integer.parseInt(value);
                break;

            case "--age":
                ageYears = Integer.parseInt(value);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown prediction option: " + option
                );
        }
    }

    if (location == null || location.isEmpty()) {
        throw new IllegalArgumentException(
                "Location is required."
        );
    }

    if (areaSqFt == null || areaSqFt <= 0) {
        throw new IllegalArgumentException(
                "Area must be a positive number."
        );
    }

    if (bedrooms == null || bedrooms < 0) {
        throw new IllegalArgumentException(
                "Bedrooms must be zero or greater."
        );
    }

    if (bathrooms == null || bathrooms < 0) {
        throw new IllegalArgumentException(
                "Bathrooms must be zero or greater."
        );
    }

    if (ageYears == null || ageYears < 0) {
        throw new IllegalArgumentException(
                "Age must be zero or greater."
        );
    }

    return new PredictionRequest(
            location,
            areaSqFt,
            bedrooms,
            bathrooms,
            ageYears
    );
}

private void printPredictionUsage() {
    System.out.println(
            "Usage: predict --location <name> --area <sqft> "
                    + "--bedrooms <count> --bathrooms <count> "
                    + "--age <years>"
    );
}

private void printPredictionResult(PredictionResult result) {
    System.out.println();
    System.out.println("HomeVault Price Estimate");
    System.out.println("----------------------------------------");
    System.out.println(
            "Location: " + result.getRequest().getLocation()
    );
    System.out.printf(
            "Requested area: %,.0f sq ft%n",
            result.getRequest().getAreaSqFt()
    );
    System.out.println(
            "Bedrooms: " + result.getRequest().getBedrooms()
    );
    System.out.println(
            "Bathrooms: " + result.getRequest().getBathrooms()
    );
    System.out.println(
            "Property age: "
                    + result.getRequest().getAgeYears()
                    + " years"
    );
    System.out.println();
    System.out.println("Comparable properties used:");

    for (ComparableProperty comparable
            : result.getComparableProperties()) {
        Property property = comparable.getProperty();

        System.out.printf(
                "  %s | %s | %,.0f sq ft | %d BHK | %d bath "
                        + "| %d years | INR %,.0f | score %.0f%n",
                property.getId(),
                property.getLocation(),
                property.getAreaSqFt(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getAgeYears(),
                property.getPrice(),
                comparable.getSimilarityScore()
        );
    }

    System.out.println();
    System.out.printf(
            "Average comparable price per sq ft: INR %,.0f%n",
            result.getAverageComparablePricePerSqFt()
    );
    System.out.printf(
            "Base estimate: INR %,.0f%n",
            result.getBaseEstimate()
    );
    System.out.printf(
            "Bedroom adjustment: INR %,.0f%n",
            result.getBedroomAdjustment()
    );
    System.out.printf(
            "Bathroom adjustment: INR %,.0f%n",
            result.getBathroomAdjustment()
    );
    System.out.printf(
            "Age adjustment: INR %,.0f%n",
            result.getAgeAdjustment()
    );
    System.out.println("----------------------------------------");
    System.out.printf(
            "Estimated price: INR %,.0f%n",
            result.getFinalEstimate()
    );
    System.out.println(
            "Prediction confidence: " + result.getConfidence()
    );
    System.out.println(
            "Note: This is an educational estimate based on "
                    + "local comparable property records."
    );
    System.out.println();
}

private void printStatistics(
        PropertyStatistics statistics,
        String location
) {
    System.out.println();

    if (location == null) {
        System.out.println("Overall property statistics");
    } else {
        System.out.println("Statistics for location: " + location);
    }

    System.out.println("----------------------------------------");
    System.out.println(
            "Properties analysed: "
                    + statistics.getPropertyCount()
    );
    System.out.printf(
            "Average price: INR %,.0f%n",
            statistics.getAveragePrice()
    );
    System.out.printf(
            "Minimum price: INR %,.0f%n",
            statistics.getMinimumPrice()
    );
    System.out.printf(
            "Maximum price: INR %,.0f%n",
            statistics.getMaximumPrice()
    );
    System.out.printf(
            "Average area: %,.0f sq ft%n",
            statistics.getAverageAreaSqFt()
    );
    System.out.printf(
            "Average price per sq ft: INR %,.0f%n",
            statistics.getAveragePricePerSqFt()
    );
    System.out.println("----------------------------------------");
    System.out.println();
}

private SearchOptions parseSearchOptions(String arguments) {
    SearchOptions options = new SearchOptions();
    String[] tokens = arguments.trim().split("\\s+");

    for (int index = 0; index < tokens.length; index++) {
        String option = tokens[index];

        if (index + 1 >= tokens.length) {
            throw new IllegalArgumentException(
                    "Missing value for " + option
            );
        }

        String value = tokens[++index];

        switch (option) {
            case "--location":
                options.setLocation(value);
                break;

            case "--bedrooms":
                options.setBedrooms(Integer.parseInt(value));
                break;

            case "--min-price":
                options.setMinPrice(Double.parseDouble(value));
                break;

            case "--max-price":
                options.setMaxPrice(Double.parseDouble(value));
                break;

            case "--min-area":
                options.setMinArea(Double.parseDouble(value));
                break;

            case "--max-area":
                options.setMaxArea(Double.parseDouble(value));
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown filter: " + option
                );
        }
    }

    validateSearchOptions(options);
    return options;
}

private void validateSearchOptions(SearchOptions options) {
    if (options.getBedrooms() != null
            && options.getBedrooms() < 0) {
        throw new IllegalArgumentException(
                "Bedrooms cannot be negative."
        );
    }

    if (options.getMinPrice() != null
            && options.getMinPrice() < 0) {
        throw new IllegalArgumentException(
                "Minimum price cannot be negative."
        );
    }

    if (options.getMaxPrice() != null
            && options.getMaxPrice() < 0) {
        throw new IllegalArgumentException(
                "Maximum price cannot be negative."
        );
    }

    if (options.getMinArea() != null
            && options.getMinArea() < 0) {
        throw new IllegalArgumentException(
                "Minimum area cannot be negative."
        );
    }

    if (options.getMaxArea() != null
            && options.getMaxArea() < 0) {
        throw new IllegalArgumentException(
                "Maximum area cannot be negative."
        );
    }

    if (options.getMinPrice() != null
            && options.getMaxPrice() != null
            && options.getMinPrice() > options.getMaxPrice()) {
        throw new IllegalArgumentException(
                "Minimum price cannot be greater than maximum price."
        );
    }

    if (options.getMinArea() != null
            && options.getMaxArea() != null
            && options.getMinArea() > options.getMaxArea()) {
        throw new IllegalArgumentException(
                "Minimum area cannot be greater than maximum area."
        );
    }
}

private void printSearchResults(List<Property> properties) {
    if (properties.isEmpty()) {
        System.out.println("No properties matched your search.");
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
    System.out.println("Matching properties: " + properties.size());
    System.out.println();
}

private void importProperties(String filePath) {
    ImportResult result = csvImporter.importProperties(filePath);

    int addedCount = propertyRepository.addAllProperties(
            result.getImportedProperties()
    );

    int duplicateCount =
            result.getImportedCount() - addedCount;

    System.out.println();
    System.out.println("Imported new properties: " + addedCount);
    System.out.println("Duplicate IDs skipped: " + duplicateCount);
    System.out.println("Rejected rows: " + result.getErrorCount());

    if (!result.getErrors().isEmpty()) {
        System.out.println("Import issues:");

        for (String error : result.getErrors()) {
            System.out.println("  - " + error);
        }
    }

    saveProperties();
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


    // =========The below saveProperties is used to save the data======
private void saveProperties() {
    try {
        persistenceManager.saveProperties(
                propertyRepository.getAllProperties()
        );

        System.out.println(
                "Saved " + propertyRepository.getPropertyCount()
                        + " properties to data/properties.csv"
        );

    } catch (IOException exception) {
        System.out.println(
                "Save error: " + exception.getMessage()
        );
    }
}


}

