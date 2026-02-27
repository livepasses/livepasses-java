package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessMetrics {

    private final Double totalRevenue;
    private final Double averageTicketPrice;
    private final Map<String, Integer> seatDistribution;
    private final Map<String, Integer> tierDistribution;
    private final Map<String, Double> revenueByCategory;

    @JsonCreator
    public BusinessMetrics(
            @JsonProperty("totalRevenue") Double totalRevenue,
            @JsonProperty("averageTicketPrice") Double averageTicketPrice,
            @JsonProperty("seatDistribution") Map<String, Integer> seatDistribution,
            @JsonProperty("tierDistribution") Map<String, Integer> tierDistribution,
            @JsonProperty("revenueByCategory") Map<String, Double> revenueByCategory) {
        this.totalRevenue = totalRevenue;
        this.averageTicketPrice = averageTicketPrice;
        this.seatDistribution = seatDistribution;
        this.tierDistribution = tierDistribution;
        this.revenueByCategory = revenueByCategory;
    }

    public Double getTotalRevenue() { return totalRevenue; }
    public Double getAverageTicketPrice() { return averageTicketPrice; }
    public Map<String, Integer> getSeatDistribution() { return seatDistribution; }
    public Map<String, Integer> getTierDistribution() { return tierDistribution; }
    public Map<String, Double> getRevenueByCategory() { return revenueByCategory; }
}
