package app.analytics.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing user statistics
 */
@Getter @Setter
public class UserStats {
    private Map<String, Integer> topArtists;
    private Map<String, Integer> topGenres;
    private Map<String, Integer> topSongs;
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topEpisodes;

    public UserStats() {
        topArtists = new HashMap<>();
        topGenres = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topEpisodes = new HashMap<>();
    }

    /**
     * Adds an artist to the top artists map
     * @param artist
     */
    public void addTopArtist(final String artist) {
        topArtists.putIfAbsent(artist, 0);
        topArtists.put(artist, topArtists.get(artist) + 1);
    }

    /**
     * Adds a genre to the top genres map
     * @param genre
     */
    public void addTopGenre(final String genre) {
        topGenres.putIfAbsent(genre, 0);
        topGenres.put(genre, topGenres.get(genre) + 1);
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
     * Adds an album to the top albums map
     * @param album
     */
    public void addTopAlbum(final String album) {
        topAlbums.putIfAbsent(album, 0);
        topAlbums.put(album, topAlbums.get(album) + 1);
    }

    /**
     * Adds an episode to the top episodes map
     * @param episode
     */
    public void addTopEpisode(final String episode) {
        topEpisodes.putIfAbsent(episode, 0);
        topEpisodes.put(episode, topEpisodes.get(episode) + 1);
    }

    /**
     * Resets all the maps
     */
    public void reset() {
        topArtists.clear();
        topGenres.clear();
        topSongs.clear();
        topAlbums.clear();
        topEpisodes.clear();
    }
}
