package homevault;

import java.util.ArrayList;
import java.util.List;

public class PropertySearchService {

    public List<Property> search(
            List<Property> properties,
            SearchOptions options
    ) {
        List<Property> matches = new ArrayList<>();

        for (Property property : properties) {
            if (matchesFilters(property, options)) {
                matches.add(property);
            }
        }

        return matches;
    }

    private boolean matchesFilters(
            Property property,
            SearchOptions options
    ) {
        if (options.getLocation() != null
                && !property.getLocation().equalsIgnoreCase(
                        options.getLocation()
                )) {
            return false;
        }

        if (options.getBedrooms() != null
                && property.getBedrooms() != options.getBedrooms()) {
            return false;
        }

        if (options.getMinPrice() != null
                && property.getPrice() < options.getMinPrice()) {
            return false;
        }

        if (options.getMaxPrice() != null
                && property.getPrice() > options.getMaxPrice()) {
            return false;
        }

        if (options.getMinArea() != null
                && property.getAreaSqFt() < options.getMinArea()) {
            return false;
        }

        if (options.getMaxArea() != null
                && property.getAreaSqFt() > options.getMaxArea()) {
            return false;
        }

        return true;
    }
}