package app.recommendations;

import app.Admin;
import app.analytics.Analytics;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.User;
import app.user.UserAbstract;
import java.util.*;

/**
 * Class that implements the strategy for the fans playlist recommendation.
 */
public class FansPlaylistStrategy extends RecommendationStrategy {
    public FansPlaylistStrategy(final UserAbstract currentArtist) {
        super(currentArtist);
    }

    /**
     * Calculates the fans playlist recommendation for the current artist.
     * @return
     */
    @Override
    public LibraryEntry getRecommendation() {
        Artist artist = (Artist) super.getUserAbstract();
        Analytics.updateFans(artist, Admin.getInstance().getUsers());
        List<String> topFans = artist.getStats().getListTopFans();

        if (topFans.isEmpty()) {
            return null;
        }

        Set<Song> uniqueSongs = new HashSet<>();
        Admin admin = Admin.getInstance();

        topFans.stream().limit(5).forEach(username -> {
            User user = admin.getUser(username);
            List<Song> likedSongs = user.getLikedSongs().stream()
                    .sorted(Comparator.comparing(Song::getLikes).reversed())
                    .limit(5)
                    .toList();
            uniqueSongs.addAll(likedSongs);
        });

        Playlist fansPlaylist = new Playlist(artist.getUsername() + " Fan Club recommendations",
                "None");
        fansPlaylist.getSongs().addAll(uniqueSongs);

        super.setLastRecommendation(fansPlaylist);
        super.setLastRecommendationType("playlist");
        return fansPlaylist;
    }
}

