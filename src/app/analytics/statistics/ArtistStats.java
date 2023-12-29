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

    /**
     * Adds an album to the top albums map
     * @param album
     */
    public void addTopAlbum(final String album) {
        topAlbums.putIfAbsent(album, 0);
        topAlbums.put(album, topAlbums.get(album) + 1);
    }

    /**
     * Adds a song to the top songs map
     * @param song
     */
    public void addTopSong(final String song) {
        topSongs.putIfAbsent(song, 0);
        topSongs.put(song, topSongs.get(song) + 1);
    }

    /**
     * Adds a fan to the top fans map
     * @param user
     */
    public void addTopFan(final String user) {
        topFans.putIfAbsent(user, 0);
        topFans.put(user, topFans.get(user) + 1);
    }

    /**
     * Adds a listener to the listeners set
     * @param user
     */
    public void addListener(final String user) {
        listeners.add(user);
    }

    /**
     * Returns the number of listeners
     * @return
     */
    public Integer getListenersNumber() {
        return listeners.size();
    }

    /**
     * Resets the statistics
     */
    public void reset() {
        topAlbums.clear();
        topSongs.clear();
        topFans.clear();
        listeners.clear();
    }
}
