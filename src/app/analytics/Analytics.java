package app.analytics;

import app.analytics.monetization.ArtistRevenue;
import app.analytics.statistics.UserStats;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.commandHandle.OutputBuilder;
import app.user.Artist;
import app.user.User;
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
    public static ObjectNode endProgram(final Map<String, ArtistRevenue> artistRevenues) {
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
            final List<Map.Entry<String, ArtistRevenue>> sortedEntries) {
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

    private static Map<String, Integer> getTopFiveSortedByCount(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .limit(Constants.LIMIT)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public static LinkedHashMap<String, Map<String, Integer>> wrappedUser(final User user) {
//        if (user.getUsername().equals("irene33")) {
//            System.out.println("ok\n");
//            System.out.println(user.getPlayer().getSource().getAudioFile().getName());
//        }
        user.getPlayer().updateHistory();
        List<AudioFile> history = user.getPlayer().getHistory();
        UserStats stats = user.getUserStats();

        for (AudioFile audioFile : history) {
            if (audioFile.getType().equals("song")) {
                Song song = (Song) audioFile;
                stats.getTopArtists().putIfAbsent(song.getArtist(), 0);
                stats.getTopArtists().put(song.getArtist(), stats.getTopArtists()
                        .get(song.getArtist()) + 1);

                stats.getTopGenres().putIfAbsent(song.getGenre(), 0);
                stats.getTopGenres().put(song.getGenre(), stats.getTopGenres()
                        .get(song.getGenre()) + 1);

                stats.getTopSongs().putIfAbsent(song.getName(), 0);
                stats.getTopSongs().put(song.getName(), stats.getTopSongs()
                        .get(song.getName()) + 1);

                stats.getTopAlbums().putIfAbsent(song.getAlbum(), 0);
                stats.getTopAlbums().put(song.getAlbum(), stats.getTopAlbums()
                        .get(song.getAlbum()) + 1);
            } else {
                Episode episode = (Episode) audioFile;
                stats.getTopEpisodes().putIfAbsent(episode.getName(), 0);
                stats.getTopEpisodes().put(episode.getName(), stats.getTopEpisodes()
                        .get(episode.getName()) + 1);
            }
        }

        LinkedHashMap<String, Map<String, Integer>> statsMap = new LinkedHashMap<>();

        statsMap.put("topArtists", getTopFiveSortedByCount(stats.getTopArtists()));
        statsMap.put("topGenres", getTopFiveSortedByCount(stats.getTopGenres()));
        statsMap.put("topSongs", getTopFiveSortedByCount(stats.getTopSongs()));
        statsMap.put("topAlbums", getTopFiveSortedByCount(stats.getTopAlbums()));
        statsMap.put("topEpisodes", getTopFiveSortedByCount(stats.getTopEpisodes()));

        return statsMap;
    }
}
