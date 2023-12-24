package app.searchBar;

import app.audio.LibraryEntry;
import app.searchBar.filters.FilterUtils;
import app.searchBar.filters.Filters;

import java.util.List;

import static app.searchBar.filters.FilterUtils.filterByPlaylistVisibility;

/**
 * Abstract class for searching an audio file.
 */
public abstract class SearchAudio extends SearchBase<LibraryEntry> {
    public SearchAudio(final String user) {
        super(user);
    }

    /**
     * Specific implementation of search for audio files.
     * @param type
     * @param filters
     * @return
     */
    @Override
    public List<LibraryEntry> search(final String type, final Filters filters) {
        List<LibraryEntry> entries = fetchEntries();

        if ("playlist".equals(type)) {
            entries = filterByPlaylistVisibility(entries, user);
        }

        entries = FilterUtils.applyFilters(entries, filters);

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }
}
