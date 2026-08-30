package homevault;

public class PropertyStatistics {

    private final int propertyCount;
    private final double averagePrice;
    private final double minimumPrice;
    private final double maximumPrice;
    private final double averageAreaSqFt;
    private final double averagePricePerSqFt;

    public PropertyStatistics(
            int propertyCount,
            double averagePrice,
            double minimumPrice,
            double maximumPrice,
            double averageAreaSqFt,
            double averagePricePerSqFt
    ) {
        this.propertyCount = propertyCount;
        this.averagePrice = averagePrice;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.averageAreaSqFt = averageAreaSqFt;
        this.averagePricePerSqFt = averagePricePerSqFt;
    }

    public int getPropertyCount() {
        return propertyCount;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public double getMaximumPrice() {
        return maximumPrice;
    }

    public double getAverageAreaSqFt() {
        return averageAreaSqFt;
    }

    public double getAveragePricePerSqFt() {
        return averagePricePerSqFt;
    }
}