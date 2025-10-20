package com.yarmook.realstate.service.dto;

import java.util.ArrayList;
import java.util.List;

public class PropertyContextDTO {

    private Long propertyId;
    private Long extId;
    private String slug;
    private String name;
    private String developer;
    private String area;
    private String city;
    private String country;
    private String status;
    private String priceRange;
    private Double minPriceAed;
    private String unitsRange;
    private Double score;
    private List<String> keyPoints = new ArrayList<>();

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public Double getMinPriceAed() {
        return minPriceAed;
    }

    public void setMinPriceAed(Double minPriceAed) {
        this.minPriceAed = minPriceAed;
    }

    public String getUnitsRange() {
        return unitsRange;
    }

    public void setUnitsRange(String unitsRange) {
        this.unitsRange = unitsRange;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public List<String> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(List<String> keyPoints) {
        this.keyPoints = keyPoints;
    }
}
