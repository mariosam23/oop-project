package app.analytics.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for storing artist statistics
 */
@Getter @Setter
public class ArtistStats {
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topSongs;
    private Map<String, Integer> topFans;
    private Set<String> listeners;

    public ArtistStats() {
        topAlbums = new HashMap<>();
        topSongs = new HashMap<>();
        topFans = new HashMap<>();
        listeners = new HashSet<>();
    }
}
