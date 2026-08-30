package homevault;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {

    private static final String HEADER =
            "id,location,areaSqFt,bedrooms,bathrooms,ageYears,price";

    private final Path dataFile;

    public PersistenceManager() {
        dataFile = Path.of("data", "properties.csv");
    }

    public List<Property> loadProperties() {
        List<Property> properties = new ArrayList<>();

        if (!Files.exists(dataFile)) {
            return properties;
        }

        try (BufferedReader reader = Files.newBufferedReader(dataFile)) {
            String header = reader.readLine();

            if (header == null) {
                return properties;
            }

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                Property property = parseProperty(line);
                properties.add(property);
            }

        } catch (IOException | IllegalArgumentException exception) {
            System.out.println(
                    "Warning: Could not load saved data: "
                            + exception.getMessage()
            );
        }

        return properties;
    }

    public void saveProperties(List<Property> properties)
            throws IOException {
        Files.createDirectories(dataFile.getParent());

        Path temporaryFile = Path.of(
                "data",
                "properties-temp.csv"
        );

        try (BufferedWriter writer =
                Files.newBufferedWriter(temporaryFile)) {

            writer.write(HEADER);
            writer.newLine();

            for (Property property : properties) {
                writer.write(toCsvRow(property));
                writer.newLine();
            }
        }

        try {
            Files.move(
                    temporaryFile,
                    dataFile,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (IOException exception) {
            Files.move(
                    temporaryFile,
                    dataFile,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    public boolean savedDataExists() {
        return Files.exists(dataFile);
    }

    private Property parseProperty(String line) {
        String[] values = line.split(",", -1);

        if (values.length != 7) {
            throw new IllegalArgumentException(
                    "Saved data contains an invalid row."
            );
        }

        return new Property(
                values[0].trim(),
                values[1].trim(),
                Double.parseDouble(values[2].trim()),
                Integer.parseInt(values[3].trim()),
                Integer.parseInt(values[4].trim()),
                Integer.parseInt(values[5].trim()),
                Double.parseDouble(values[6].trim())
        );
    }

    private String toCsvRow(Property property) {
        return property.getId() + ","
                + property.getLocation() + ","
                + property.getAreaSqFt() + ","
                + property.getBedrooms() + ","
                + property.getBathrooms() + ","
                + property.getAgeYears() + ","
                + property.getPrice();
    }
}