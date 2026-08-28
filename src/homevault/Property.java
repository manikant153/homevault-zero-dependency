package homevault;

public class Property {

    private final String id;
    private final String location;
    private final double areaSqFt;
    private final int bedrooms;
    private final int bathrooms;
    private final int ageYears;
    private final double price;

    public Property(
            String id,
            String location,
            double areaSqFt,
            int bedrooms,
            int bathrooms,
            int ageYears,
            double price
    ) {
        this.id = id;
        this.location = location;
        this.areaSqFt = areaSqFt;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.ageYears = ageYears;
        this.price = price;
    }

    public String getId() {
        return id;
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

    public double getPrice() {
        return price;
    }

    public double getPricePerSqFt() {
        return price / areaSqFt;
    }

    public String toDisplayRow() {
        return String.format(
                "%-6s %-16s %8.0f %5d %5d %5d INR %,12.0f",
                id,
                location,
                areaSqFt,
                bedrooms,
                bathrooms,
                ageYears,
                price
        );
    }
}