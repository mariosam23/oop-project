package app.searchBar.strategies;

import app.Admin;
import app.audio.LibraryEntry;
import app.searchBar.SearchAudio;

import java.util.ArrayList;
import java.util.List;

public class SearchSongStrategy extends SearchAudio {
    public SearchSongStrategy(final String user) {
        super(user);
    }

    /**
     * @return List of songs from library.
     */
    @Override
    protected List<LibraryEntry> fetchEntries() {
        return new ArrayList<>(Admin.getInstance().getSongs());
    }
}
