package homevault;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {

    private final List<Property> importedProperties;
    private final List<String> errors;

    public ImportResult(
            List<Property> importedProperties,
            List<String> errors
    ) {
        this.importedProperties = new ArrayList<>(importedProperties);
        this.errors = new ArrayList<>(errors);
    }

    public List<Property> getImportedProperties() {
        return new ArrayList<>(importedProperties);
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public int getImportedCount() {
        return importedProperties.size();
    }

    public int getErrorCount() {
        return errors.size();
    }
}