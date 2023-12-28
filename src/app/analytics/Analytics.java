package app.analytics;

import app.analytics.monetization.ArtistRevenue;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.commandHandle.OutputBuilder;
import app.user.Artist;
import app.utils.Constants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that contains the methods for the analytics commands.
 */
public final class Analytics {

    /**
     * Gets top 5 album list.
     *
     * @return the top 5 album list
     */
    public static List<String> getTop5AlbumList(final List<Artist> artists) {
        List<Album> albums = artists.stream().map(Artist::getAlbums)
                .flatMap(List::stream).toList();

        final Map<Album, Integer> albumLikes = new HashMap<>();
        albums.forEach(album -> albumLikes.put(album, album.getSongs().stream()
                .map(Song::getLikes).reduce(0, Integer::sum)));

        return albums.stream().sorted((o1, o2) -> {
            if ((int) albumLikes.get(o1) == albumLikes.get(o2)) {
                return o1.getName().compareTo(o2.getName());
            }
            return albumLikes.get(o2) - albumLikes.get(o1);
        }).limit(Constants.LIMIT).map(Album::getName).toList();
    }

    /**
     * Gets top 5 artist list.
     *
     * @return the top 5 artist list
     */
    public static List<String> getTop5ArtistList(final List<Artist> artists) {
        final Map<Artist, Integer> artistLikes = new HashMap<>();
        artists.forEach(artist -> artistLikes.put(artist, artist.getAllSongs().stream()
                .map(Song::getLikes).reduce(0, Integer::sum)));

        return artists.stream().sorted(Comparator.comparingInt(artistLikes::get).reversed())
                .limit(Constants.LIMIT).map(Artist::getUsername).toList();
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs(final List<Song> songs) {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= Constants.LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }


    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists(final List<Playlist> playlists) {
        List<Playlist> sortedPlaylists = new ArrayList<>(playlists);
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= Constants.LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Ends the program by showing all the artist revenues and their rankings.
     * @param artistRevenues the map of artist revenues
     * @return the output
     */
    public static ObjectNode endProgram(Map<String, ArtistRevenue> artistRevenues) {
        List<Map.Entry<String, ArtistRevenue>> sortedEntries = artistRevenues.entrySet().stream()
                .sorted(Comparator.comparing(
                                (Map.Entry<String, ArtistRevenue> entry) -> entry
                                        .getValue().getTotalRevenue(),
                                Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toList());

        LinkedHashMap<String, Map<String, Object>> sortedResults = createSortedResults(
                sortedEntries);

        CommandInput commandInput = new CommandInput();
        commandInput.setCommand("endProgram");

        return new OutputBuilder<>(commandInput)
                .withMapResult(sortedResults)
                .build();
    }

    private static LinkedHashMap<String, Map<String, Object>> createSortedResults(
            List<Map.Entry<String, ArtistRevenue>> sortedEntries) {
        LinkedHashMap<String, Map<String, Object>> results = new LinkedHashMap<>();
        int rank = 1;

        for (Map.Entry<String, ArtistRevenue> entry : sortedEntries) {
            ArtistRevenue revenue = entry.getValue();
            Map<String, Object> details = new LinkedHashMap<>();

            details.put("merchRevenue", revenue.getMerchRevenue());
            details.put("songRevenue", revenue.getSongRevenue());
            details.put("ranking", rank++);
            details.put("mostProfitableSong", revenue.getMostProfitableSong());

            results.put(entry.getKey(), details);
        }

        return results;
    }
}
