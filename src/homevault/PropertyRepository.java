package homevault;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {

    private final List<Property> properties;

    public PropertyRepository() {
        properties = new ArrayList<>();
        addSampleProperties();
    }

    private void addSampleProperties() {
        properties.add(new Property(
                "H001",
                "Noida",
                950,
                2,
                2,
                5,
                5200000
        ));

        properties.add(new Property(
                "H002",
                "Noida",
                1200,
                3,
                2,
                3,
                7100000
        ));

        properties.add(new Property(
                "H003",
                "Greater Noida",
                1400,
                3,
                3,
                2,
                7600000
        ));

        properties.add(new Property(
                "H004",
                "Noida",
                800,
                1,
                1,
                7,
                4200000
        ));
    }

    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }

    public int getPropertyCount() {
        return properties.size();
    }
}