package homevault;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PropertyRepository {

    private final Map<String, Property> properties;

    public PropertyRepository() {
        properties = new LinkedHashMap<>();
    }

    public boolean addProperty(Property property) {
        if (properties.containsKey(property.getId())) {
            return false;
        }

        properties.put(property.getId(), property);
        return true;
    }

    public int addAllProperties(List<Property> importedProperties) {
        int addedCount = 0;

        for (Property property : importedProperties) {
            if (addProperty(property)) {
                addedCount++;
            }
        }

        return addedCount;
    }

    public List<Property> getAllProperties() {
        return new ArrayList<>(properties.values());
    }

    public int getPropertyCount() {
        return properties.size();
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public void replaceAllProperties(List<Property> loadedProperties) {
        properties.clear();

        for (Property property : loadedProperties) {
            addProperty(property);
        }
    }
}