package homevault;

public class ComparableProperty {

    private final Property property;
    private final double similarityScore;

    public ComparableProperty(
            Property property,
            double similarityScore
    ) {
        this.property = property;
        this.similarityScore = similarityScore;
    }

    public Property getProperty() {
        return property;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }
}