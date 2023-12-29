package app.commandHandle;

import app.Admin;
import app.analytics.Analytics;
import app.analytics.monetization.ArtistRevenue;
import app.audio.Collections.output.AlbumOutput;
import app.audio.Collections.output.PlaylistOutput;
import app.audio.Collections.output.PodcastOutput;
import app.player.PlayerStats;
import app.searchBar.filters.Filters;
import app.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.*;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    private static Admin admin = Admin.getInstance();

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        ArrayList<String> results = new ArrayList<>();
        String message = "%s is offline.".formatted(user.getUsername());

        if (user.isOnline()) {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        }

        return new OutputBuilder<>(commandInput).withMessage(message)
                .withResults(results).build();
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        String message = user.select(commandInput.getItemNumber());
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.load();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.repeat();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.forward();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.backward();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.like();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.next();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.prev();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        return new OutputBuilder<PlaylistOutput>(commandInput).withResult(playlists).build();
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.follow();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        return new OutputBuilder<PlayerStats>(commandInput).withStats(stats).build();
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        return new OutputBuilder<String>(commandInput).withResult(songs).build();
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        return new OutputBuilder<String>(commandInput).withMessage(preferredGenre).build();
    }

    /**
     * Switch connection status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        String message = admin.switchStatus(commandInput.getUsername());
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Add user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = admin.addNewUser(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Delete user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String message = admin.deleteUser(commandInput.getUsername());
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Add album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        String message = admin.addAlbum(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Remove album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        String message = admin.removeAlbum(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Show albums object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = artist.showAlbums();

        return new OutputBuilder<AlbumOutput>(commandInput).withResult(albums).build();
    }

    /**
     * Add event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        String message = admin.addEvent(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Remove event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        String message = admin.removeEvent(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        String message = admin.addPodcast(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Remove podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        String message = admin.removePodcast(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        Host host = admin.getHost(commandInput.getUsername());
        List<PodcastOutput> podcasts = host.getPodcasts().stream()
                .map(PodcastOutput::new).toList();

        return new OutputBuilder<PodcastOutput>(commandInput).withResult(podcasts).build();
    }

    /**
     * Add merch object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        String message = admin.addMerch(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    public static ObjectNode removeMerch(final CommandInput commandInput) {
        String message = admin.removeMerch(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Add announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        String message = admin.addAnnouncement(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Remove announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        String message = admin.removeAnnouncement(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();

    }

    /**
     * Gets online users.
     *
     * @param commandInput the command input
     * @return the online users
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = admin.getOnlineUsers();
        return new OutputBuilder<String>(commandInput).withResult(onlineUsers).build();
    }

    /**
     * Gets all users.
     *
     * @param commandInput the command input
     * @return the all users
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = admin.getAllUsers();
        return new OutputBuilder<String>(commandInput).withResult(users).build();
    }

    /**
     * Change page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        String message = admin.changePage(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Print current page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        String message = admin.printCurrentPage(commandInput);
        return new OutputBuilder<>(commandInput).withMessage(message).withSwap(Boolean.TRUE)
                .build();
    }

    /**
     * Gets top 5 album list.
     *
     * @param commandInput the command input
     * @return the top 5 album list
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Analytics.getTop5AlbumList(admin.getArtists());
        return new OutputBuilder<String>(commandInput).withResult(albums).build();
    }

    /**
     * Gets top 5 artist list.
     *
     * @param commandInput the command input
     * @return the top 5 artist list
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> artists = Analytics.getTop5ArtistList(admin.getArtists());
        return new OutputBuilder<String>(commandInput).withResult(artists).build();
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Analytics.getTop5Songs(admin.getSongs());
        return new OutputBuilder<String>(commandInput).withResult(songs).build();
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Analytics.getTop5Playlists(admin.getPlaylists());
        return new OutputBuilder<String>(commandInput).withResult(playlists).build();
    }

    /**
     * Changes page to the previous one.
     * @param commandInput
     * @return
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.previousPage();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Changes page to the next one.
     * @param commandInput
     * @return
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.nextPage();

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Subscribes to a host or an artist.
     * @param commandInput
     * @return
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        String message = admin.subscribe(commandInput);

        return new OutputBuilder<>(commandInput).withMessage(message).build();
    }

    /**
     * Gets the notifications of a user.
     * @param commandInput
     * @return
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        List<Notification> notifications = new ArrayList<>(user.getNotifications());
        user.getNotifications().clear();

        return new OutputBuilder<Notification>(commandInput)
                .withResultFieldName("notifications")
                .withResult(notifications).build();
    }

    /**
     * Wraps the data of a user, artist or host.
     * @param commandInput
     * @return
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        UserAbstract user = admin.getAbstractUser(commandInput.getUsername());

        if (user == null) {
            return new OutputBuilder<>(commandInput).withMessage("No data available!").build();
        }

        if (user.userType().equals("user")) {
            return Analytics.wrappedUser((User) user, commandInput);
        }
        else if (user.userType().equals("artist")) {
            return Analytics.wrappedArtist((Artist) user, admin.getUsers(), commandInput);
        } else if (user.userType().equals("host")) {
            return Analytics.wrappedHost((Host) user, admin.getUsers(), commandInput);
        }

        return null;
    }

    /**
     * Buys merch for user.
     * @param cmd
     * @return
     */
    public static ObjectNode buyMerch(final CommandInput cmd) {
        String message = admin.buyMerch(cmd);

        return new OutputBuilder<>(cmd).withMessage(message).build();
    }

    /**
     * See merch bought by user.
     * @param cmd
     * @return
     */
    public static ObjectNode seeMerch(final CommandInput cmd) {
        User user = admin.getUser(cmd.getUsername());
        if (user == null) {
            return new OutputBuilder<>(cmd)
                    .withMessage("The username " + cmd.getUsername() + " doesn't exist.").build();
        }

        return new OutputBuilder<String>(cmd).withResult(user.getMerchBought()).build();
    }

    public static ObjectNode buyPremium(final CommandInput cmd) {
        String message = admin.buyPremium(cmd);

        return new OutputBuilder<>(cmd).withMessage(message).build();
    }

    public static ObjectNode cancelPremium(final CommandInput cmd) {
        String message = admin.cancelPremium(cmd);

        return new OutputBuilder<>(cmd).withMessage(message).build();
    }

    public static ObjectNode updateRecommendations(final CommandInput cmd) {
        String message = admin.updateRecommendations(cmd);

        return new OutputBuilder<>(cmd).withMessage(message).build();
    }
}
