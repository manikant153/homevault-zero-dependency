package homevault;

public class SearchOptions {

    private String location;
    private Integer bedrooms;
    private Double minPrice;
    private Double maxPrice;
    private Double minArea;
    private Double maxArea;

    public String getLocation() {
        return location;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinArea() {
        return minArea;
    }

    public Double getMaxArea() {
        return maxArea;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinArea(Double minArea) {
        this.minArea = minArea;
    }

    public void setMaxArea(Double maxArea) {
        this.maxArea = maxArea;
    }
}