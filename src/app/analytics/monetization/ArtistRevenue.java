package app.analytics.monetization;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ArtistRevenue {
    private Double merchRevenue;
    private Double songRevenue;
    private Map<String, Double> songProfit;
    private String mostProfitableSong;

    public ArtistRevenue() {
        merchRevenue = 0.00;
        songRevenue = 0.00;
        songProfit = new HashMap<>();
        mostProfitableSong = "N/A";
    }

    /**
     * @return total revenue of an artist.
     */
    public Double getTotalRevenue() {
        return merchRevenue + songRevenue;
    }
}
