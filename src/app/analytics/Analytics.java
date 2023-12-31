package app.analytics;

import app.Admin;
import app.analytics.monetization.ArtistRevenue;
import app.analytics.statistics.UserStats;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.commandHandle.OutputBuilder;
import app.player.Player;
import app.user.Artist;
import app.user.Host;
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
        for (User user : Admin.getInstance().getUsers()) {
            user.getPlayer().updateHistory();

            if (user.isPremium()) {
                Player player = user.getPlayer();
                int startIdx = player.getPremiumStarts().get(player.getPremiumStarts().size() - 1);
                int endIdx = player.getHistory().size();
                Admin.getInstance().distributePremiumRevenue(user, startIdx, endIdx);
            }
        }

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
        final double oneHundred = 100.00;

        for (Map.Entry<String, ArtistRevenue> entry : sortedEntries) {
            ArtistRevenue revenue = entry.getValue();
            Map<String, Object> details = new LinkedHashMap<>();

            details.put("merchRevenue", revenue.getMerchRevenue());
            details.put("songRevenue", Math.round(revenue.getSongRevenue()
                    * oneHundred) / oneHundred);
            details.put("ranking", rank++);
            revenue.findMostProfitableSong();
            details.put("mostProfitableSong", revenue.getMostProfitableSong());

            results.put(entry.getKey(), details);
        }

        return results;
    }

    private static Map<String, Integer> getTopFiveSortedByCount(final Map<String, Integer> map) {
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

    /**
     * Gets the statistics for an user for wrapped. It firsts updates the user's history
     * and then it gets the top 5 artists, genres, songs, albums and episodes.
     * @param user the user
     * @param cmd the command
     * @return the output in JSON format
     */
    public static ObjectNode wrappedUser(final User user, final CommandInput cmd) {
        user.getPlayer().updateHistory();
        List<AudioFile> history = user.getPlayer().getHistory();
        UserStats stats = user.getUserStats();
        stats.reset();

        if (history.isEmpty()) {
            return new OutputBuilder<>(cmd).withMessage("No data to show for user "
                    + user.getUsername() + ".").build();
        }

        for (AudioFile audioFile : history) {
            if (audioFile.getType().equals("song")) {
                Song song = (Song) audioFile;

                stats.addTopArtist(song.getArtist());
                stats.addTopAlbum(song.getAlbum());
                stats.addTopGenre(song.getGenre());
                stats.addTopSong(song.getName());
            } else {
                Episode episode = (Episode) audioFile;
                stats.addTopEpisode(episode.getName());
            }
        }

        LinkedHashMap<String, Map<String, Integer>> statsMap = new LinkedHashMap<>();

        statsMap.put("topArtists", getTopFiveSortedByCount(stats.getTopArtists()));
        statsMap.put("topGenres", getTopFiveSortedByCount(stats.getTopGenres()));
        statsMap.put("topSongs", getTopFiveSortedByCount(stats.getTopSongs()));
        statsMap.put("topAlbums", getTopFiveSortedByCount(stats.getTopAlbums()));
        statsMap.put("topEpisodes", getTopFiveSortedByCount(stats.getTopEpisodes()));

        return new OutputBuilder<>(cmd).withMapResult(statsMap).build();
    }

    /**
     * Gets the stats for wrapped for an artist. It first updates every user's history.
     * Then filter all songs that are not from the artist for every user. Then it gets
     * the top 5 albums, songs, fans and the number of listeners.
     * @param artist the artist
     * @param users the list of users from the app
     * @param cmd the command
     * @return the output in JSON format
     */
    public static ObjectNode wrappedArtist(final Artist artist, final List<User> users,
                                           final CommandInput cmd) {
        artist.getStats().reset();
        for (User user : users) {
            user.getPlayer().updateHistory();
            List<AudioFile> history = user.getPlayer().getHistory();

            List<AudioFile> historyCopy = new ArrayList<>(history);

            historyCopy.removeIf(audioFile -> !audioFile.getType().equals("song"));
            historyCopy.removeIf(audioFile -> !((Song) audioFile).getArtist()
                                          .equals(artist.getUsername()));

            if (historyCopy.isEmpty()) {
                continue;
            }

            artist.getStats().addListener(user.getUsername());

            for (AudioFile audioFile : historyCopy) {
                Song song = (Song) audioFile;
                artist.getStats().addTopAlbum(song.getAlbum());
                artist.getStats().addTopSong(song.getName());
                artist.getStats().addTopFan(user.getUsername());
            }
        }

        if (artist.getStats().getListenersNumber() == 0) {
            return new OutputBuilder<>(cmd).withMessage("No data to show for artist "
                    + artist.getUsername() + ".").build();
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("topAlbums", getTopFiveSortedByCount(artist.getStats().getTopAlbums()));
        result.put("topSongs", getTopFiveSortedByCount(artist.getStats().getTopSongs()));

        artist.getStats().setListTopFans(new ArrayList<>(getTopFiveSortedByCount(artist
                .getStats().getTopFans()).keySet()));

        result.put("topFans", artist.getStats().getListTopFans());

        result.put("listeners", artist.getStats().getListenersNumber());

        return new OutputBuilder<>(cmd)
                .withResultFieldName("result")
                .withMapResult(result)
                .build();
    }

    /**
     * Updates the fans for an artist. It first updates every user's history.
     * Then filter all songs that are not from the artist for every user.
     * @param artist
     * @param users
     */
    public static void updateFans(final Artist artist, final List<User> users) {
        artist.getStats().reset();
        for (User user : users) {
            user.getPlayer().updateHistory();
            List<AudioFile> history = user.getPlayer().getHistory();

            for (AudioFile audioFile : history) {
                if (audioFile.getType().equals("song") && ((Song) audioFile).getArtist()
                        .equals(artist.getUsername())) {
                    artist.getStats().addTopFan(user.getUsername());
                }
            }
        }

        artist.getStats().setListTopFans(new ArrayList<>(getTopFiveSortedByCount(artist.getStats()
                .getTopFans()).keySet()));
    }


    /**
     * Gets the stats for wrapped for a host. It first updates every user's history.
     * Then filter all episodes that are not from the host for every user. Then
     * it gets the top 5 episodes and the number of listeners.
     * @param host the host
     * @param users the list of users from the app
     * @param cmd the command
     * @return the output in JSON format
     */
    public static ObjectNode wrappedHost(final Host host, final List<User> users,
                                         final CommandInput cmd) {
        host.getStats().reset();
        for (User user : users) {
            user.getPlayer().updateHistory();
            List<AudioFile> history = user.getPlayer().getHistory();

            List<AudioFile> copyHistory = new ArrayList<>(history);
            copyHistory.removeIf(audioFile -> !audioFile.getType().equals("episode"));
            copyHistory.removeIf(audioFile -> !(audioFile.getOwner()
                                          .equals(host.getUsername())));

            if (copyHistory.isEmpty()) {
                continue;
            }

            host.getStats().addListener(user.getUsername());

            for (AudioFile audioFile : copyHistory) {
                Episode episode = (Episode) audioFile;
                host.getStats().addTopEpisode(episode.getName());
            }
        }

        if (host.getStats().getListenersNumber() == 0) {
            return new OutputBuilder<>(new CommandInput())
                    .withMessage("No data to show for host " + host.getUsername() + ".")
                    .build();
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("topEpisodes", getTopFiveSortedByCount(host.getStats().getTopEpisodes()));
        result.put("listeners", host.getStats().getListenersNumber());

        return new OutputBuilder<>(cmd)
                .withResultFieldName("result")
                .withMapResult(result)
                .build();
    }
}
