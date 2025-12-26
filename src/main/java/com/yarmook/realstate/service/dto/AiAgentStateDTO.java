package com.yarmook.realstate.service.dto;

import java.io.Serializable;

/**
 * Conversation state exchanged between the AI agent frontend and backend.
 */
public class AiAgentStateDTO implements Serializable {

    private Double budgetMaxAed;
    private String purpose;
    private String plan;
    private Boolean interestConfirmed;
    private String leadName;
    private String leadPhone;
    private boolean leadCaptured;
    private String stage;
    private Integer minBedrooms;
    private Integer maxBedrooms;
    private Double minArea;
    private Double maxArea;
    private Integer completionYearFrom;
    private Integer completionYearTo;
    private String city;
    private String area;

    public Double getBudgetMaxAed() {
        return budgetMaxAed;
    }

    public void setBudgetMaxAed(Double budgetMaxAed) {
        this.budgetMaxAed = budgetMaxAed;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Boolean getInterestConfirmed() {
        return interestConfirmed;
    }

    public void setInterestConfirmed(Boolean interestConfirmed) {
        this.interestConfirmed = interestConfirmed;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getLeadPhone() {
        return leadPhone;
    }

    public void setLeadPhone(String leadPhone) {
        this.leadPhone = leadPhone;
    }

    public boolean isLeadCaptured() {
        return leadCaptured;
    }

    public void setLeadCaptured(boolean leadCaptured) {
        this.leadCaptured = leadCaptured;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getMinBedrooms() {
        return minBedrooms;
    }

    public void setMinBedrooms(Integer minBedrooms) {
        this.minBedrooms = minBedrooms;
    }

    public Integer getMaxBedrooms() {
        return maxBedrooms;
    }

    public void setMaxBedrooms(Integer maxBedrooms) {
        this.maxBedrooms = maxBedrooms;
    }

    public Double getMinArea() {
        return minArea;
    }

    public void setMinArea(Double minArea) {
        this.minArea = minArea;
    }

    public Double getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(Double maxArea) {
        this.maxArea = maxArea;
    }

    public Integer getCompletionYearFrom() {
        return completionYearFrom;
    }

    public void setCompletionYearFrom(Integer completionYearFrom) {
        this.completionYearFrom = completionYearFrom;
    }

    public Integer getCompletionYearTo() {
        return completionYearTo;
    }

    public void setCompletionYearTo(Integer completionYearTo) {
        this.completionYearTo = completionYearTo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
