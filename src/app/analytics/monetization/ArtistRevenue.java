package app.analytics.monetization;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArtistRevenue {
    private Double merchRevenue;
    private Double songRevenue;
    private String mostProfitableSong;
    private Double mostProfitableSongRevenue;

    public ArtistRevenue() {
        merchRevenue = 0.0;
        songRevenue = 0.0;
        mostProfitableSong = "N/A";
    }

    /**
     * @return total revenue of an artist.
     */
    public Double getTotalRevenue() {
        return merchRevenue + songRevenue;
    }
}
