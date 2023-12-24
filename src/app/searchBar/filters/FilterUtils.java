package app.searchBar.filters;

import app.audio.LibraryEntry;
import java.util.ArrayList;
import java.util.List;

public final class FilterUtils {

    private FilterUtils() { }

    /**
     * Method that filters a list of audio files by name.
     * @param entries list of audio files
     * @param name
     * @return list of audio files that match the name
     */
    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries,
                                                  final String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * Method that filters a list of audio files by album.
     * @param entries
     * @param album
     * @return
     */
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries,
                                                   final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }

    /**
     * Method that filters a list of audio files by tags.
     * @param entries
     * @param tags
     * @return
     */
    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    /**
     * Method that filters a list of audio files by lyrics.
     * @param entries
     * @param lyrics
     * @return
     */
    public static List<LibraryEntry> filterByLyrics(final List<LibraryEntry> entries,
                                                    final String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }

    /**
     * Method that filters a list of audio files by genre.
     * @param entries
     * @param genre
     * @return
     */
    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries,
                                                   final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    /**
     * Method that filters a list of audio files by artist.
     * @param entries
     * @param artist
     * @return
     */
    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries,
                                                    final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }

    /**
     * Method that filters a list of audio files by release year.
     * @param entries
     * @param releaseYear
     * @return
     */
    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                         final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }

    /**
     * Method that filters a list of audio files by owner.
     * @param entries
     * @param user
     * @return
     */
    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries,
                                                   final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }

    /**
     * Method that filters a list of audio files by playlist visibility.
     * @param entries
     * @param user
     * @return
     */
    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                                final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }

    /**
     * Method that filters a list of audio files by followers.
     * @param entries
     * @param followers
     * @return
     */
    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    /**
     * Method that filters a list of audio files by description.
     * @param entries
     * @param description
     * @return
     */
    public static List<LibraryEntry> filterByDescription(final List<LibraryEntry> entries,
                                                         final String description) {
        return filter(entries, entry -> entry.matchesDescription(description));
    }

    /**
     * Method that filters a list of audio files by a criteria.
     * @param entries
     * @param criteria
     * @return
     */
    private static List<LibraryEntry> filter(final List<LibraryEntry> entries,
                                             final FilterCriteria criteria) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (criteria.matches(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * Method that applies filters to a list of audio files.
     * @param entries
     * @param filters
     * @return
     */
    public static List<LibraryEntry> applyFilters(List<LibraryEntry> entries,
                                                  final Filters filters) {
        if (filters.getName() != null) {
            entries = filterByName(entries, filters.getName());
        }

        if (filters.getAlbum() != null) {
            entries = filterByAlbum(entries, filters.getAlbum());
        }

        if (filters.getTags() != null) {
            entries = filterByTags(entries, filters.getTags());
        }

        if (filters.getLyrics() != null) {
            entries = filterByLyrics(entries, filters.getLyrics());
        }

        if (filters.getGenre() != null) {
            entries = filterByGenre(entries, filters.getGenre());
        }

        if (filters.getReleaseYear() != null) {
            entries = filterByReleaseYear(entries, filters.getReleaseYear());
        }

        if (filters.getArtist() != null) {
            entries = filterByArtist(entries, filters.getArtist());
        }

        if (filters.getOwner() != null) {
            entries = filterByOwner(entries, filters.getOwner());
        }

        if (filters.getFollowers() != null) {
            entries = filterByFollowers(entries, filters.getFollowers());
        }

        if (filters.getDescription() != null) {
            entries = filterByDescription(entries, filters.getDescription());
        }

        return entries;
    }

    @FunctionalInterface
    private interface FilterCriteria {
        boolean matches(LibraryEntry entry);
    }
}
