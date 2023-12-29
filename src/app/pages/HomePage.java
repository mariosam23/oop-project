package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Home page.
 */
@Getter @Setter
public final class HomePage extends Page {
    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;
    private List<Song> songRecommendations;
    private List<Playlist> playlistRecommendations;
    private String owner;
    private final int limit = 5;

    /**
     * Instantiates a new Home page.
     *
     * @param user the user
     */
    public HomePage(final User user) {
        likedSongs = user.getLikedSongs();
        followedPlaylists = user.getFollowedPlaylists();
        songRecommendations = user.getSongRecommendations();
        playlistRecommendations = user.getPlaylistRecommendations();
        owner = user.getUsername();
    }

    @Override
    public String printCurrentPage() {
        return ("Liked songs:\n\t%s\n\nFollowed playlists:\n\t%s\n\nSong recommendations:"
                + "\n\t%s\n\nPlaylists recommendations:\n\t%s")
                .formatted(likedSongs.stream()
                                .sorted(Comparator.comparing(Song::getLikes)
                                        .reversed()).limit(limit).map(Song::getName)
                                .toList(),
                        followedPlaylists.stream().sorted((o1, o2) ->
                                        o2.getSongs().stream().map(Song::getLikes)
                                                .reduce(Integer::sum).orElse(0)
                                                - o1.getSongs().stream().map(Song::getLikes).reduce(Integer::sum)
                                                .orElse(0)).limit(limit).map(Playlist::getName)
                                .toList(),
                        songRecommendations.stream()
                                .limit(limit).map(Song::getName).toList(),
                        playlistRecommendations.stream().limit(limit).map(Playlist::getName)
                                .toList());
    }

    @Override
    public String pageType() {
        return "Home";
    }

    @Override
    public String pageOwner() {
        return owner;
    }
}
