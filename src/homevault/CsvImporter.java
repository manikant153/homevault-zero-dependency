package homevault;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter {

    public ImportResult importProperties(String filePath) {
        List<Property> importedProperties = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            errors.add("File not found: " + filePath);
            return new ImportResult(importedProperties, errors);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();

            if (header == null) {
                errors.add("The CSV file is empty.");
                return new ImportResult(importedProperties, errors);
            }

            int lineNumber = 1;
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Property property = parseProperty(line);
                    importedProperties.add(property);
                } catch (IllegalArgumentException exception) {
                    errors.add(
                            "Line " + lineNumber + ": "
                                    + exception.getMessage()
                    );
                }
            }

        } catch (IOException exception) {
            errors.add(
                    "Could not read file: " + exception.getMessage()
            );
        }

        return new ImportResult(importedProperties, errors);
    }

    private Property parseProperty(String line) {
        String[] values = line.split(",", -1);

        if (values.length != 7) {
            throw new IllegalArgumentException(
                    "Expected 7 columns but found " + values.length
            );
        }

        String id = values[0].trim();
        String location = values[1].trim();

        if (id.isEmpty()) {
            throw new IllegalArgumentException("Property ID is missing.");
        }

        if (location.isEmpty()) {
            throw new IllegalArgumentException("Location is missing.");
        }

        try {
            double areaSqFt = Double.parseDouble(values[2].trim());
            int bedrooms = Integer.parseInt(values[3].trim());
            int bathrooms = Integer.parseInt(values[4].trim());
            int ageYears = Integer.parseInt(values[5].trim());
            double price = Double.parseDouble(values[6].trim());

            if (areaSqFt <= 0 || bedrooms < 0 || bathrooms < 0
                    || ageYears < 0 || price <= 0) {
                throw new IllegalArgumentException(
                        "Numeric values must be valid and positive."
                );
            }

            return new Property(
                    id,
                    location,
                    areaSqFt,
                    bedrooms,
                    bathrooms,
                    ageYears,
                    price
            );

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Area, bedrooms, bathrooms, age and price "
                            + "must contain valid numbers."
            );
        }
    }
}