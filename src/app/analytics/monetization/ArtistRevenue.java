package app.analytics.monetization;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains the details of revenue of an artist.
 */
@Setter @Getter
public class ArtistRevenue {
    private Double merchRevenue;
    private Double songRevenue;
    private Map<String, Double> profitSongs;
    private String mostProfitableSong;

    public ArtistRevenue() {
        merchRevenue = 0.00;
        songRevenue = 0.00;
        profitSongs = new HashMap<>();
        mostProfitableSong = "N/A";
    }

    /**
     * @return total revenue of an artist.
     */
    public Double getTotalRevenue() {
        return merchRevenue + songRevenue;
    }

    /**
     * Finds the most profitable song of an artist.
     */
    public void findMostProfitableSong() {
        double maxProfit = 0.00;
        String maxProfitSong = "N/A";

        for (Map.Entry<String, Double> entry : profitSongs.entrySet()) {
            if (entry.getValue() > maxProfit) {
                maxProfit = entry.getValue();
                maxProfitSong = entry.getKey();
            }
        }

        mostProfitableSong = maxProfitSong;
    }
}
