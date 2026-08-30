package homevault;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PredictionEngine {

    private static final int MAX_COMPARABLES = 3;
    private static final double BEDROOM_ADJUSTMENT = 150000;
    private static final double BATHROOM_ADJUSTMENT = 100000;
    private static final double AGE_ADJUSTMENT_PER_YEAR = 25000;

    public PredictionResult predict(
            List<Property> properties,
            PredictionRequest request
    ) {
        List<ComparableProperty> comparableProperties =
                findComparableProperties(properties, request);

        if (comparableProperties.isEmpty()) {
            throw new IllegalArgumentException(
                    "No properties found for location: "
                            + request.getLocation()
            );
        }

        double averagePricePerSqFt =
                calculateAveragePricePerSqFt(comparableProperties);

        double baseEstimate =
                request.getAreaSqFt() * averagePricePerSqFt;

        double averageBedrooms =
                calculateAverageBedrooms(comparableProperties);

        double averageBathrooms =
                calculateAverageBathrooms(comparableProperties);

        double averageAge =
                calculateAverageAge(comparableProperties);

        double bedroomAdjustment =
                (request.getBedrooms() - averageBedrooms)
                        * BEDROOM_ADJUSTMENT;

        double bathroomAdjustment =
                (request.getBathrooms() - averageBathrooms)
                        * BATHROOM_ADJUSTMENT;

        double ageAdjustment =
                (averageAge - request.getAgeYears())
                        * AGE_ADJUSTMENT_PER_YEAR;

        double finalEstimate =
                baseEstimate
                        + bedroomAdjustment
                        + bathroomAdjustment
                        + ageAdjustment;

        String confidence = determineConfidence(comparableProperties);

        return new PredictionResult(
                request,
                comparableProperties,
                averagePricePerSqFt,
                baseEstimate,
                bedroomAdjustment,
                bathroomAdjustment,
                ageAdjustment,
                finalEstimate,
                confidence
        );
    }

    private List<ComparableProperty> findComparableProperties(
            List<Property> properties,
            PredictionRequest request
    ) {
        List<ComparableProperty> candidates = new ArrayList<>();

        for (Property property : properties) {
            if (property.getLocation().equalsIgnoreCase(
                    request.getLocation()
            )) {
                double score = calculateSimilarityScore(
                        property,
                        request
                );

                candidates.add(
                        new ComparableProperty(property, score)
                );
            }
        }

        candidates.sort(
                Comparator.comparingDouble(
                        ComparableProperty::getSimilarityScore
                )
        );

        if (candidates.size() > MAX_COMPARABLES) {
            return new ArrayList<>(
                    candidates.subList(0, MAX_COMPARABLES)
            );
        }

        return candidates;
    }

    private double calculateSimilarityScore(
            Property property,
            PredictionRequest request
    ) {
        double areaDifference =
                Math.abs(property.getAreaSqFt()
                        - request.getAreaSqFt());

        double bedroomDifference =
                Math.abs(property.getBedrooms()
                        - request.getBedrooms());

        double bathroomDifference =
                Math.abs(property.getBathrooms()
                        - request.getBathrooms());

        double ageDifference =
                Math.abs(property.getAgeYears()
                        - request.getAgeYears());

        return areaDifference
                + (bedroomDifference * 200)
                + (bathroomDifference * 100)
                + (ageDifference * 25);
    }

    private double calculateAveragePricePerSqFt(
            List<ComparableProperty> comparableProperties
    ) {
        double total = 0;

        for (ComparableProperty comparable : comparableProperties) {
            total += comparable.getProperty().getPricePerSqFt();
        }

        return total / comparableProperties.size();
    }

    private double calculateAverageBedrooms(
            List<ComparableProperty> comparableProperties
    ) {
        double total = 0;

        for (ComparableProperty comparable : comparableProperties) {
            total += comparable.getProperty().getBedrooms();
        }

        return total / comparableProperties.size();
    }

    private double calculateAverageBathrooms(
            List<ComparableProperty> comparableProperties
    ) {
        double total = 0;

        for (ComparableProperty comparable : comparableProperties) {
            total += comparable.getProperty().getBathrooms();
        }

        return total / comparableProperties.size();
    }

    private double calculateAverageAge(
            List<ComparableProperty> comparableProperties
    ) {
        double total = 0;

        for (ComparableProperty comparable : comparableProperties) {
            total += comparable.getProperty().getAgeYears();
        }

        return total / comparableProperties.size();
    }

    private String determineConfidence(
            List<ComparableProperty> comparableProperties
    ) {
        if (comparableProperties.size() >= 3) {
            return "High";
        }

        if (comparableProperties.size() == 2) {
            return "Medium";
        }

        return "Low";
    }
}