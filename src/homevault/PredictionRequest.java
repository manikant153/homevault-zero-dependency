package homevault;

public class PredictionRequest {

    private final String location;
    private final double areaSqFt;
    private final int bedrooms;
    private final int bathrooms;
    private final int ageYears;

    public PredictionRequest(
            String location,
            double areaSqFt,
            int bedrooms,
            int bathrooms,
            int ageYears
    ) {
        this.location = location;
        this.areaSqFt = areaSqFt;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.ageYears = ageYears;
    }

    public String getLocation() {
        return location;
    }

    public double getAreaSqFt() {
        return areaSqFt;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getAgeYears() {
        return ageYears;
    }
}