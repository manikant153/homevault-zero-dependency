package homevault;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {

    /* 
    List<Property> means a list that can hold objects of type Property. The List interface is part of the Java Collections Framework and provides a way to store an ordered collection of elements. In this case, the elements are instances of the Property class.
  ------------------------------------------------
    private means that the properties list can only be accessed within the PropertyRepository class. It cannot be accessed directly from outside the class. This encapsulation helps to protect the data and maintain control over how it is accessed and modified.
--------------------------------------------------
    final means that the properties variable can only be assigned once. Once it is initialized (in this case, in the constructor), it cannot be reassigned to point to a different List object. However, the contents of the list can still be modified (e.g., adding or removing Property objects).
---------------------------------------------
    Tell me one thing that is important to note is that the properties list is initialized in the constructor of the PropertyRepository class. This means that when an instance of PropertyRepository is created, the properties list is created and ready to be used. The addSampleProperties method is called in the constructor to populate the list with some sample Property objects for testing purposes.
    
    */

    //And how the Property class is called here is that the Property class is used as the type parameter for the List interface. This means that the properties list can only hold objects of type Property. The Property class is defined in the same package (homevault), so it can be accessed directly without needing to import it.Then what is use of the private, here private
    private final List<Property> properties;
    //just above  List<> properties is the instance of the PropertyRepository class.

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

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void addAllProperties(List<Property> importedProperties) {
        properties.addAll(importedProperties);
    }

    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }

    public int getPropertyCount() {
        return properties.size();
    }
}