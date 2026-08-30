package homevault;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" HomeVault");
        System.out.println(" Offline Property Data and Price Estimator");
        System.out.println(" Zero external dependencies");
        System.out.println("========================================");

        PropertyRepository propertyRepository =
                new PropertyRepository();

        PersistenceManager persistenceManager =
                new PersistenceManager();

        List<Property> savedProperties =
                persistenceManager.loadProperties();

        if (!savedProperties.isEmpty()) {
            propertyRepository.replaceAllProperties(savedProperties);

            System.out.println(
                    "Loaded " + propertyRepository.getPropertyCount()
                            + " saved properties."
            );
        } else {
            System.out.println(
                    "No saved properties found. "
                            + "Use import <csv-file> to begin."
            );
        }

        Shell shell = new Shell(propertyRepository);
        shell.start();
    }
}



  /*
        When propertyRepository is created, it will automataically  add some sample proerpties to the list you can use for testing. You can also ad your own properties using the addProperty method of the PropertyRespository class. You can also modify the sample peroperties in the addSampleProperties method of the Propertyrespositry and soon.
        When Create this Object it automatically call it's default constructor and which has the addSampleProperties method which will add some sample properties to the list. In the list is private and final ,eg:-
        private final List<Property> properties
        */