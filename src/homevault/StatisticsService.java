package homevault;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public class StatisticsService {

    public PropertyStatistics calculate(List<Property> properties) {
        if (properties.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot calculate statistics for an empty property list."
            );
        }

        DoubleSummaryStatistics priceStatistics = properties.stream()
                .mapToDouble(Property::getPrice)
                .summaryStatistics();

        DoubleSummaryStatistics areaStatistics = properties.stream()
                .mapToDouble(Property::getAreaSqFt)
                .summaryStatistics();

        double totalPricePerSqFt = 0;

        for (Property property : properties) {
            totalPricePerSqFt += property.getPricePerSqFt();
        }

        double averagePricePerSqFt =
                totalPricePerSqFt / properties.size();

        return new PropertyStatistics(
                properties.size(),
                priceStatistics.getAverage(),
                priceStatistics.getMin(),
                priceStatistics.getMax(),
                areaStatistics.getAverage(),
                averagePricePerSqFt
        );
    }
}