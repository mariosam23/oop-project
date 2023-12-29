package app;

import app.analytics.monetization.ArtistRevenue;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.player.Player;
import app.pages.pageContent.Announcement;
import app.recommendations.RandomSongStrategy;
import app.recommendations.RecommendationStrategy;
import app.user.*;
import app.pages.pageContent.Event;
import app.pages.pageContent.Merchandise;
import app.utils.Constants;
import fileio.input.LibraryInput;
import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import fileio.input.EpisodeInput;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private List<User> users = new ArrayList<>();
    @Getter
    private List<Artist> artists = new ArrayList<>();
    @Getter
    private List<Host> hosts = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private RecommendationStrategy<?> recommendationStrategy;
    private int timestamp = 0;
    private static Admin instance;
    @Getter @Setter
    private Map<String, ArtistRevenue> userInteractions = new HashMap<>();

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Sets entities.
     *
     * @param library the database
     */
    public void setEntities(final LibraryInput library) {
        setUsers(library.getUsers());
        setSongs(library.getSongs());
        setPodcasts(library.getPodcasts());
    }

    private void setUsers(final List<UserInput> userInputList) {
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    private void setSongs(final List<SongInput> songInputList) {
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    private void setPodcasts(final List<PodcastInput> podcastInputList) {
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription(),
                        podcastInput.getOwner()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        return users.stream()
                .flatMap(user -> user.getPlaylists().stream())
                .collect(Collectors.toList());
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public List<Album> getAlbums() {
        return artists.stream()
                .flatMap(artist -> artist.getAlbums().stream())
                .collect(Collectors.toList());
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();

        allUsers.addAll(users.stream().map(UserAbstract::getUsername).toList());
        allUsers.addAll(artists.stream().map(UserAbstract::getUsername).toList());
        allUsers.addAll(hosts.stream().map(UserAbstract::getUsername).toList());

        return allUsers;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(final String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets artist.
     *
     * @param username the username
     * @return the artist
     */
    public Artist getArtist(final String username) {
        return artists.stream()
                .filter(artist -> artist.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets host.
     *
     * @param username the username
     * @return the host
     */
    public Host getHost(final String username) {
        return hosts.stream()
                .filter(host -> host.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;

        if (elapsed == 0) {
            return;
        } else if (elapsed < 0) {
            throw new IllegalArgumentException("Invalid timestamp" + newTimestamp);
        }

        users.forEach(user -> user.simulateTime(elapsed));
    }

    /**
     * Gets abstract user.
     * @param username
     * @return
     */
    public UserAbstract getAbstractUser(final String username) {
        ArrayList<UserAbstract> allUsers = new ArrayList<>();

        allUsers.addAll(users);
        allUsers.addAll(artists);
        allUsers.addAll(hosts);

        return allUsers.stream()
                .filter(userPlatform -> userPlatform.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private String validateArtistUser(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        return null;
    }

    private String validateHostUser(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("host")) {
            return "%s is not a host.".formatted(username);
        }

        return null;
    }

    private String validateNormalUser(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("user")) {
            return "%s is not a normal user.".formatted(username);
        }

        return null;
    }

    /**
     * Add new user string.
     *
     * @param command the command input
     * @return the string
     */
    public String addNewUser(final CommandInput command) {
        String username = command.getUsername();
        String type = command.getType();
        int age = command.getAge();
        String city = command.getCity();

        UserAbstract currentUser = getAbstractUser(username);
        if (currentUser != null) {
            return "The username %s is already taken.".formatted(username);
        }

        if (type.equals("user")) {
            users.add(new User(username, age, city));
        } else if (type.equals("artist")) {
            artists.add(new Artist(username, age, city));
        } else {
            hosts.add(new Host(username, age, city));
        }

        return "The username %s has been added successfully.".formatted(username);
    }

    /**
     * Delete user string.
     *
     * @param username the username
     * @return the string
     */
    public String deleteUser(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        }

        if (currentUser.userType().equals("user")) {
            return deleteNormalUser((User) currentUser);
        }

        if (currentUser.userType().equals("host")) {
            return deleteHost((Host) currentUser);
        }

        return deleteArtist((Artist) currentUser);
    }

    private String deleteNormalUser(final User user) {
        if (user.getPlaylists().stream().anyMatch(playlist -> users.stream().map(User::getPlayer)
                .filter(player -> player != user.getPlayer())
                .map(Player::getCurrentAudioCollection)
                .filter(Objects::nonNull)
                .anyMatch(collection -> collection == playlist))) {
            return "%s can't be deleted.".formatted(user.getUsername());
        }

        user.getLikedSongs().forEach(Song::dislike);
        user.getFollowedPlaylists().forEach(Playlist::decreaseFollowers);

        users.stream().filter(otherUser -> otherUser != user)
                .forEach(otherUser -> otherUser.getFollowedPlaylists()
                        .removeAll(user.getPlaylists()));

        users.remove(user);
        return "%s was successfully deleted.".formatted(user.getUsername());
    }

    private String deleteHost(final Host host) {
        if (host.getPodcasts().stream().anyMatch(podcast -> getAudioCollectionsStream()
                .anyMatch(collection -> collection == podcast))
                || users.stream().anyMatch(user -> user.getCurrentPage() == host.getPage())) {
            return "%s can't be deleted.".formatted(host.getUsername());
        }

        host.getPodcasts().forEach(podcast -> podcasts.remove(podcast));
        hosts.remove(host);

        return "%s was successfully deleted.".formatted(host.getUsername());
    }

    private String deleteArtist(final Artist artist) {
        if (artist.getAlbums().stream().anyMatch(album -> album.getSongs().stream()
                .anyMatch(song -> getAudioFilesStream().anyMatch(audioFile -> audioFile == song))
                || getAudioCollectionsStream().anyMatch(collection -> collection == album))
                || users.stream().anyMatch(user -> user.getCurrentPage() == artist.getPage())) {
            return "%s can't be deleted.".formatted(artist.getUsername());
        }

        users.forEach(user -> artist.getAlbums().forEach(album -> album.getSongs().forEach(song -> {
            user.getLikedSongs().remove(song);
            user.getPlaylists().forEach(playlist -> playlist.removeSong(song));
        })));

        songs.removeAll(artist.getAllSongs());
        artists.remove(artist);
        return "%s was successfully deleted.".formatted(artist.getUsername());
    }

    /**
     * Add album string.
     *
     * @param command the command input
     * @return the string
     */
    public String addAlbum(final CommandInput command) {
        String username = command.getUsername();
        String albumName = command.getName();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        if (currentArtist.getAlbums().stream()
                .anyMatch(album -> album.getName().equals(albumName))) {
            return "%s has another album with the same name.".formatted(username);
        }

        List<Song> newSongs = command.getSongs().stream()
                .map(songInput -> new Song(songInput.getName(),
                        songInput.getDuration(),
                        albumName,
                        songInput.getTags(),
                        songInput.getLyrics(),
                        songInput.getGenre(),
                        songInput.getReleaseYear(),
                        currentArtist.getUsername()))
                .toList();

        Set<String> songNames = new HashSet<>();
        if (!newSongs.stream().filter(song -> !songNames.add(song.getName()))
                .collect(Collectors.toSet()).isEmpty()) {
            return "%s has the same song at least twice in this album.".formatted(username);
        }

        songs.addAll(newSongs);
        currentArtist.getAlbums().add(new Album(albumName,
                command.getDescription(),
                username,
                newSongs,
                command.getReleaseYear()));

        notify(currentArtist, "New Album", "New Album from " + username + ".");
        return "%s has added new album successfully.".formatted(username);
    }

    /**
     * Remove album string.
     *
     * @param command the command input
     * @return the string
     */
    public String removeAlbum(final CommandInput command) {
        String username = command.getUsername();
        String albumName = command.getName();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        Album searchedAlbum = currentArtist.getAlbum(albumName);
        if (searchedAlbum == null) {
            return "%s doesn't have an album with the given name.".formatted(username);
        }

        if (getAudioCollectionsStream().anyMatch(collection -> collection == searchedAlbum)) {
            return "%s can't delete this album.".formatted(username);
        }

        for (Song song : searchedAlbum.getSongs()) {
            if (getAudioCollectionsStream().anyMatch(collection -> collection.containsTrack(song))
                    || getAudioFilesStream().anyMatch(audioFile -> audioFile == song)) {
                return "%s can't delete this album.".formatted(username);
            }
        }

        for (Song song: searchedAlbum.getSongs()) {
            users.forEach(user -> {
                user.getLikedSongs().remove(song);
                user.getPlaylists().forEach(playlist -> playlist.removeSong(song));
            });
            songs.remove(song);
        }

        currentArtist.getAlbums().remove(searchedAlbum);
        return "%s deleted the album successfully.".formatted(username);
    }

    /**
     * Add podcast string.
     *
     * @param command the command input
     * @return the string
     */
    public String addPodcast(final CommandInput command) {
        String username = command.getUsername();
        String podcastName = command.getName();
        if (validateHostUser(username) != null) {
            return validateHostUser(username);
        }

        Host currentHost = getHost(username);
        if (currentHost.getPodcasts().stream()
                .anyMatch(podcast -> podcast.getName().equals(podcastName))) {
            return "%s has another podcast with the same name.".formatted(username);
        }

        List<Episode> episodes = command.getEpisodes().stream()
                .map(episodeInput ->
                        new Episode(episodeInput.getName(),
                                episodeInput.getDuration(),
                                episodeInput.getDescription(),
                                username))
                .collect(Collectors.toList());

        Set<String> episodeNames = new HashSet<>();
        if (!episodes.stream().filter(episode -> !episodeNames.add(episode.getName()))
                .collect(Collectors.toSet()).isEmpty()) {
            return "%s has the same episode in this podcast.".formatted(username);
        }

        Podcast newPodcast = new Podcast(podcastName, username, episodes);
        currentHost.getPodcasts().add(newPodcast);
        podcasts.add(newPodcast);

        notify(currentHost, "New Podcast", "New Podcast from " + username + ".");
        return "%s has added new podcast successfully.".formatted(username);
    }


    /**
     * Remove podcast string.
     *
     * @param command the command input
     * @return the string
     */
    public String removePodcast(final CommandInput command) {
        String username = command.getUsername();
        String podcastName = command.getName();

        if (validateHostUser(username) != null) {
            return validateHostUser(username);
        }

        Host currentHost = getHost(username);
        Podcast searchedPodcast = currentHost.getPodcast(podcastName);

        if (searchedPodcast == null) {
            return "%s doesn't have a podcast with the given name.".formatted(username);
        }

        if (getAudioCollectionsStream().anyMatch(collection -> collection == searchedPodcast)) {
            return "%s can't delete this podcast.".formatted(username);
        }

        currentHost.getPodcasts().remove(searchedPodcast);
        podcasts.remove(searchedPodcast);
        return "%s deleted the podcast successfully.".formatted(username);
    }

    /**
     * Add event string.
     *
     * @param command the command input
     * @return the string
     */
    public String addEvent(final CommandInput command) {
        String username = command.getUsername();
        String eventName = command.getName();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        if (currentArtist.getEvent(eventName) != null) {
            return "%s has another event with the same name.".formatted(username);
        }

        String date = command.getDate();

        if (!checkDate(date)) {
            return "Event for %s does not have a valid date.".formatted(username);
        }

        currentArtist.getEvents().add(new Event(eventName,
                command.getDescription(),
                command.getDate()));

        notify(currentArtist, "New Event", "New Event from " + username + ".");
        return "%s has added new event successfully.".formatted(username);
    }

    /**
     * Remove event string.
     *
     * @param command the command input
     * @return the string
     */
    public String removeEvent(final CommandInput command) {
        String username = command.getUsername();
        String eventName = command.getName();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        Event searchedEvent = currentArtist.getEvent(eventName);
        if (searchedEvent == null) {
            return "%s doesn't have an event with the given name.".formatted(username);
        }

        currentArtist.getEvents().remove(searchedEvent);
        return "%s deleted the event successfully.".formatted(username);
    }

    private boolean checkDate(final String date) {
        if (date.length() != Constants.DATE_STRING_LENGTH) {
            return false;
        }

        List<String> dateElements = Arrays.stream(date.split("-", Constants
                .DATE_FORMAT_SIZE)).toList();

        if (dateElements.size() != Constants.DATE_FORMAT_SIZE) {
            return false;
        }

        int day = Integer.parseInt(dateElements.get(0));
        int month = Integer.parseInt(dateElements.get(1));
        int year = Integer.parseInt(dateElements.get(2));

        if (day < Constants.DATE_DAY_LOW_LIMIT
                || (month == 2 && day > Constants.FEB_MAX_DAY)
                || day > Constants.DATE_DAY_HIGH_LIMIT
                || month < Constants.DATE_MOTH_LOW_LIMIT || month > Constants.DATE_MOTH_HIGH_LIMIT
                || year < Constants.DATE_YEAR_LOW_LIMIT || year > Constants.DATE_YEAR_HIGH_LIMIT) {
            return false;
        }

        return true;
    }

    /**
     * Add merch string.
     *
     * @param command the command input
     * @return the string
     */
    public String addMerch(final CommandInput command) {
        String username = command.getUsername();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        if (currentArtist.getMerch().stream()
                .anyMatch(merch -> merch.getName().equals(command.getName()))) {
            return "%s has merchandise with the same name.".formatted(currentArtist.getUsername());
        } else if (command.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        currentArtist.getMerch().add(new Merchandise(command.getName(),
                command.getDescription(),
                command.getPrice()));

        notify(currentArtist, "New Merchandise", "New Merchandise from " + username + ".");
        return "%s has added new merchandise successfully.".formatted(username);
    }

    public String removeMerch(final CommandInput command) {
        String username = command.getUsername();
        String merchName = command.getName();

        if (validateArtistUser(username) != null) {
            return validateArtistUser(username);
        }

        Artist currentArtist = getArtist(username);
        Merchandise searchedMerch = currentArtist.getMerch().stream()
                .filter(merch -> merch.getName().equals(merchName))
                .findFirst()
                .orElse(null);

        if (searchedMerch == null) {
            return "%s doesn't have merchandise with the given name.".formatted(username);
        }

        currentArtist.getMerch().remove(searchedMerch);
        return "%s deleted the merchandise successfully.".formatted(username);
    }

    /**
     * Add announcement string.
     *
     * @param command the command input
     * @return the string
     */
    public String addAnnouncement(final CommandInput command) {
        String username = command.getUsername();
        String announcementName = command.getName();
        String announcementDescription = command.getDescription();

        if (validateHostUser(username) != null) {
            return validateHostUser(username);
        }

        Host currentHost = getHost(username);
        Announcement searchedAnnouncement = currentHost.getAnnouncement(announcementName);
        if (searchedAnnouncement != null) {
            return "%s has already added an announcement with this name.";
        }

        currentHost.getAnnouncements().add(new Announcement(announcementName,
                announcementDescription));

        notify(currentHost, "New Announcement", "New Announcement from " + username + ".");
        return "%s has successfully added new announcement.".formatted(username);
    }

    /**
     * Remove announcement string.
     *
     * @param command the command input
     * @return the string
     */
    public String removeAnnouncement(final CommandInput command) {
        String username = command.getUsername();
        String announcementName = command.getName();

        if (validateHostUser(username) != null) {
            return validateHostUser(username);
        }

        Host currentHost = getHost(username);
        Announcement searchAnnouncement = currentHost.getAnnouncement(announcementName);
        if (searchAnnouncement == null) {
            return "%s has no announcement with the given name.".formatted(username);
        }

        currentHost.getAnnouncements().remove(searchAnnouncement);
        return "%s has successfully deleted the announcement.".formatted(username);
    }

    /**
     * Change page string.
     *
     * @param command the command input
     * @return the string
     */
    public String changePage(final CommandInput command) {
        String username = command.getUsername();
        String nextPage = command.getNextPage();

        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (!user.isOnline()) {
            return "%s is offline.".formatted(user.getUsername());
        }

        switch (nextPage) {
            case "Home" -> user.setCurrentPage(new HomePage(user));
            case "LikedContent" -> user.setCurrentPage(new LikedContentPage(user));
            case "Artist", "Host" -> {
                String owner = getFileOwner(username);
                UserAbstract ownerUser = getAbstractUser(owner);

                if (ownerUser.userType().equals("artist")) {
                    user.setCurrentPage(((Artist) ownerUser).getPage());
                } else {
                    user.setCurrentPage(((Host) ownerUser).getPage());
                }
            }
            default -> {
                return "%s is trying to access a non-existent page.".formatted(username);
            }
        }

        user.getForwardHistory().clear();
        return "%s accessed %s successfully.".formatted(username, nextPage);
    }

    /**
     * Print current page string.c
     *
     * @param command the command input
     * @return the string
     */
    public String printCurrentPage(final CommandInput command) {
        String username = command.getUsername();

        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (!user.isOnline()) {
            return "%s is offline.".formatted(user.getUsername());
        }

        return user.getCurrentPage().printCurrentPage();
    }

    /**
     * Switch status string.
     *
     * @param username the username
     * @return the string
     */
    public String switchStatus(final String username) {
        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        getUser(username).switchStatus();
        return username + " has changed status successfully.";
    }

    /**
     * Gets online users.
     *
     * @return the online users
     */
    public List<String> getOnlineUsers() {
        return users.stream().filter(User::isOnline).map(User::getUsername).toList();
    }

    private String getFileOwner(final String normalUserName) {
        User user = getUser(normalUserName);
        AudioCollection currentCollection = user.getPlayer().getCurrentAudioCollection();
        AudioFile currentFile = user.getPlayer().getCurrentAudioFile();

        if (currentCollection != null) {
            return currentCollection.getOwner();
        } else if (currentFile != null) {
            return currentFile.getOwner();
        }

        return null;
    }

    private Stream<AudioCollection> getAudioCollectionsStream() {
        return users.stream().map(User::getPlayer)
                .map(Player::getCurrentAudioCollection).filter(Objects::nonNull);
    }

    private Stream<AudioFile> getAudioFilesStream() {
        return users.stream().map(User::getPlayer)
                .map(Player::getCurrentAudioFile).filter(Objects::nonNull);
    }

    public String subscribe(final CommandInput command) {
        UserAbstract currentUser = getAbstractUser(command.getUsername());
        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(command.getUsername());
        }

        String pageType = ((User) currentUser).getCurrentPage().pageType();
        String pageOwnerName = ((User)currentUser).getCurrentPage().pageOwner();

        if (!pageType.equals("Artist") && !pageType.equals("Host")) {
            return "To subscribe you need to be on the page of an artist or host.";
        }

        ContentCreator pageOwner = (ContentCreator) getAbstractUser(pageOwnerName);

        if (pageOwner.getSubscribers().contains(command.getUsername())) {
            pageOwner.getSubscribers().remove(command.getUsername());
            return command.getUsername() + " unsubscribed from "
                    + pageOwner.getUsername() + " successfully.";
        } else {
            pageOwner.getSubscribers().add(command.getUsername());
            return command.getUsername() + " subscribed to "
                    + pageOwner.getUsername() + " successfully.";
        }
    }

    private void notify(final ContentCreator creator, final String name, final String descript) {
        for (User user : users) {
            if (creator.getSubscribers().contains(user.getUsername())) {
                user.getNotifications().add(new Notification(name, descript));
            }
        }
    }

    public String buyMerch(final CommandInput cmd) {
        String username = cmd.getUsername();
        String merchName = cmd.getName();

        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (user.getCurrentPage() == null || !user.getCurrentPage().pageType().equals("Artist")) {
            return "Cannot buy merch from this page.";
        }

        Merchandise searchedMerch = ((ArtistPage) user.getCurrentPage())
                .getMerch().stream()
                .filter(merch -> merch.getName().equals(merchName))
                .findFirst()
                .orElse(null);

        if (searchedMerch == null) {
            return "The merch %s doesn't exist.".formatted(merchName);
        }

        Artist artist = getArtist(user.getCurrentPage().pageOwner());
        artist.getRevenue().setMerchRevenue(artist.getRevenue().getMerchRevenue()
                + searchedMerch.getPrice());

        user.getMerchBought().add(merchName);
        userInteractions.putIfAbsent(artist.getUsername(), artist.getRevenue());
        return "%s has added new merch successfully.".formatted(username);
    }

    public String buyPremium(final CommandInput cmd) {
        String username = cmd.getUsername();

        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (user.isPremium()) {
            return "%s is already a premium user.".formatted(username);
        }

        user.setPremium(true);
        return "%s bought the subscription successfully.".formatted(username);
    }

    public String cancelPremium(final CommandInput cmd) {
        String username = cmd.getUsername();

        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (!user.isPremium()) {
            return "%s is not a premium user.".formatted(username);
        }

        user.setPremium(false);
        return "%s cancelled the subscription successfully.".formatted(username);
    }

    public String updateRecommendations(final CommandInput cmd) {
        String type = cmd.getRecommendationType();

        if (validateNormalUser(cmd.getUsername()) != null) {
            return validateNormalUser(cmd.getUsername());
        }

        User user = getUser(cmd.getUsername());
        Song recommendation;

        if (type.equals("random_song")) {
            recommendationStrategy = new RandomSongStrategy(user);
            recommendation = (Song) recommendationStrategy.getRecommendation();
            if (recommendation == null) {
                return "No new recommendations were found.";
            }

            user.getSongRecommendations().add(recommendation);
            return "The recommendations for user %s have been updated successfully."
                    .formatted(cmd.getUsername());
        }

        return null;
    }

    public String loadRecommendations(final CommandInput cmd) {
        String username = cmd.getUsername();
        if (validateNormalUser(username) != null) {
            return validateNormalUser(username);
        }

        User user = getUser(username);
        if (!user.isOnline()) {
            return "%s is offline.".formatted(user.getUsername());
        }

        Player userPlayer = user.getPlayer();
        if (recommendationStrategy.getLastRecommendation() != null) {
            userPlayer.setSource((LibraryEntry) recommendationStrategy.getLastRecommendation(), "song");
            userPlayer.pause();
            return "Playback loaded successfully.";
        }

        return "No recommendations were found.";
    }

    /**
     * Resets all database.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        getAlbums().clear();
        userInteractions.clear();
        timestamp = 0;
    }
}
