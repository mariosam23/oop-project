package app.recommendations;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import app.user.UserAbstract;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that implements the strategy for the random playlist recommendation.
 */
public class RandomPlaylistStrategy extends RecommendationStrategy {
    public RandomPlaylistStrategy(final UserAbstract currentUser) {
        super(currentUser);
    }

    private void updateGenreCount(final Map<String, Integer> genreCount, final List<Song> songs) {
        for (Song song : songs) {
            String genre = song.getGenre();
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }
    }

    /**
     * Calculates the random playlist recommendation for the current user.
     * @return
     */
    @Override
    public Playlist getRecommendation() {
        UserAbstract user = super.getUserAbstract();
        List<Song> likedSongs = ((User) user).getLikedSongs();
        List<Playlist> playlists = ((User) user).getPlaylists();
        List<Playlist> followedPlaylists = ((User) user).getFollowedPlaylists();

        HashMap<String, Integer> genreCount = new HashMap<>();

        updateGenreCount(genreCount, likedSongs);
        playlists.forEach(playlist -> updateGenreCount(genreCount, playlist.getSongs()));
        followedPlaylists.forEach(playlist -> updateGenreCount(genreCount, playlist.getSongs()));

        if (genreCount.isEmpty()) {
            return null;
        }

        List<String> topGenres = genreCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());

        Map<String, List<Song>> songsByGenre = new HashMap<>();
        for (String genre : topGenres) {
            List<Song> songs = likedSongs.stream()
                    .filter(song -> song.getGenre().equals(genre))
                    .distinct()
                    .sorted(Comparator.comparing(Song::getLikes).reversed())
                    .collect(Collectors.toList());
            songsByGenre.put(genre, songs);
        }

        String username = user.getUsername();
        Playlist recommendedPlaylist = new Playlist(username + "'s recommendations", "None");

        addTopSongs(recommendedPlaylist, songsByGenre, topGenres, Arrays.asList(5, 3, 2));

        super.setLastRecommendation(recommendedPlaylist);
        super.setLastRecommendationType("playlist");
        return recommendedPlaylist;
    }

    private void addTopSongs(final Playlist playlist, Map<String, List<Song>> songsByGenre,
                             final List<String> topGenres, final List<Integer> limits) {
        for (int i = 0; i < topGenres.size(); i++) {
            String genre = topGenres.get(i);
            int limit = limits.get(i);
            songsByGenre.getOrDefault(genre, Collections.emptyList())
                    .stream()
                    .limit(limit)
                    .forEach(playlist.getSongs()::add);
        }
    }
}
