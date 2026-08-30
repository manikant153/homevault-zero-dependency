package homevault;

import java.util.List;

public class TestRunner {

    private int testsPassed;
    private int testsFailed;

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.runAllTests();
    }

    private void runAllTests() {
        System.out.println("Running HomeVault tests...");
        System.out.println("----------------------------------------");

        testPropertyPricePerSqFt();
        testSearchByLocation();
        testSearchByBedrooms();
        testPredictionForKnownLocation();
        testPredictionForUnknownLocation();
        testDuplicatePropertyPrevention();

        System.out.println("----------------------------------------");
        System.out.println("Tests passed: " + testsPassed);
        System.out.println("Tests failed: " + testsFailed);

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private void testPropertyPricePerSqFt() {
        Property property = new Property(
                "T001",
                "Noida",
                1000,
                2,
                2,
                4,
                6000000
        );

        assertEquals(
                "Price per sq ft calculation",
                6000.0,
                property.getPricePerSqFt()
        );
    }

    private void testSearchByLocation() {
        PropertyRepository repository = createTestRepository();

        SearchOptions options = new SearchOptions();
        options.setLocation("Noida");

        PropertySearchService searchService =
                new PropertySearchService();

        List<Property> results = searchService.search(
                repository.getAllProperties(),
                options
        );

        assertEquals(
                "Search by location",
                2,
                results.size()
        );
    }

    private void testSearchByBedrooms() {
        PropertyRepository repository = createTestRepository();

        SearchOptions options = new SearchOptions();
        options.setBedrooms(3);

        PropertySearchService searchService =
                new PropertySearchService();

        List<Property> results = searchService.search(
                repository.getAllProperties(),
                options
        );

        assertEquals(
                "Search by bedrooms",
                2,
                results.size()
        );
    }

    private void testPredictionForKnownLocation() {
        PropertyRepository repository = createTestRepository();

        PredictionRequest request = new PredictionRequest(
                "Noida",
                1200,
                3,
                2,
                4
        );

        PredictionEngine predictionEngine =
                new PredictionEngine();

        PredictionResult result = predictionEngine.predict(
                repository.getAllProperties(),
                request
        );

        assertTrue(
                "Prediction returns a positive estimate",
                result.getFinalEstimate() > 0
        );

        assertTrue(
                "Prediction includes comparable properties",
                !result.getComparableProperties().isEmpty()
        );
    }

    private void testPredictionForUnknownLocation() {
        PropertyRepository repository = createTestRepository();

        PredictionRequest request = new PredictionRequest(
                "Mumbai",
                1200,
                3,
                2,
                4
        );

        PredictionEngine predictionEngine =
                new PredictionEngine();

        try {
            predictionEngine.predict(
                    repository.getAllProperties(),
                    request
            );

            fail(
                    "Prediction rejects unknown location"
            );

        } catch (IllegalArgumentException exception) {
            pass(
                    "Prediction rejects unknown location"
            );
        }
    }

    private void testDuplicatePropertyPrevention() {
        PropertyRepository repository = new PropertyRepository();

        Property property = new Property(
                "T100",
                "Noida",
                1000,
                2,
                2,
                4,
                6000000
        );

        boolean firstAdd = repository.addProperty(property);
        boolean duplicateAdd = repository.addProperty(property);

        assertTrue(
                "Repository accepts a new property",
                firstAdd
        );

        assertTrue(
                "Repository rejects a duplicate property ID",
                !duplicateAdd
        );

        assertEquals(
                "Repository stores one property after duplicate attempt",
                1,
                repository.getPropertyCount()
        );
    }

    private PropertyRepository createTestRepository() {
        PropertyRepository repository = new PropertyRepository();

        repository.addProperty(new Property(
                "T001",
                "Noida",
                1000,
                2,
                2,
                4,
                6000000
        ));

        repository.addProperty(new Property(
                "T002",
                "Noida",
                1300,
                3,
                2,
                3,
                7800000
        ));

        repository.addProperty(new Property(
                "T003",
                "Greater Noida",
                1200,
                3,
                3,
                5,
                6900000
        ));

        return repository;
    }

    private void assertEquals(
            String testName,
            double expected,
            double actual
    ) {
        double allowedDifference = 0.001;

        if (Math.abs(expected - actual) <= allowedDifference) {
            pass(testName);
        } else {
            fail(
                    testName
                            + " | Expected: " + expected
                            + " | Actual: " + actual
            );
        }
    }

    private void assertEquals(
            String testName,
            int expected,
            int actual
    ) {
        if (expected == actual) {
            pass(testName);
        } else {
            fail(
                    testName
                            + " | Expected: " + expected
                            + " | Actual: " + actual
            );
        }
    }

    private void assertTrue(
            String testName,
            boolean condition
    ) {
        if (condition) {
            pass(testName);
        } else {
            fail(testName);
        }
    }

    private void pass(String testName) {
        testsPassed++;
        System.out.println("[PASS] " + testName);
    }

    private void fail(String testName) {
        testsFailed++;
        System.out.println("[FAIL] " + testName);
    }
}