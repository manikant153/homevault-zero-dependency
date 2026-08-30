package homevault;

import java.util.ArrayList;
import java.util.List;

public class PredictionResult {

    private final PredictionRequest request;
    private final List<ComparableProperty> comparableProperties;
    private final double averageComparablePricePerSqFt;
    private final double baseEstimate;
    private final double bedroomAdjustment;
    private final double bathroomAdjustment;
    private final double ageAdjustment;
    private final double finalEstimate;
    private final String confidence;

    public PredictionResult(
            PredictionRequest request,
            List<ComparableProperty> comparableProperties,
            double averageComparablePricePerSqFt,
            double baseEstimate,
            double bedroomAdjustment,
            double bathroomAdjustment,
            double ageAdjustment,
            double finalEstimate,
            String confidence
    ) {
        this.request = request;
        this.comparableProperties = new ArrayList<>(comparableProperties);
        this.averageComparablePricePerSqFt =
                averageComparablePricePerSqFt;
        this.baseEstimate = baseEstimate;
        this.bedroomAdjustment = bedroomAdjustment;
        this.bathroomAdjustment = bathroomAdjustment;
        this.ageAdjustment = ageAdjustment;
        this.finalEstimate = finalEstimate;
        this.confidence = confidence;
    }

    public PredictionRequest getRequest() {
        return request;
    }

    public List<ComparableProperty> getComparableProperties() {
        return new ArrayList<>(comparableProperties);
    }

    public double getAverageComparablePricePerSqFt() {
        return averageComparablePricePerSqFt;
    }

    public double getBaseEstimate() {
        return baseEstimate;
    }

    public double getBedroomAdjustment() {
        return bedroomAdjustment;
    }

    public double getBathroomAdjustment() {
        return bathroomAdjustment;
    }

    public double getAgeAdjustment() {
        return ageAdjustment;
    }

    public double getFinalEstimate() {
        return finalEstimate;
    }

    public String getConfidence() {
        return confidence;
    }
}