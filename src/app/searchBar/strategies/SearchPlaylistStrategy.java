package app.searchBar.strategies;

import app.Admin;
import app.audio.LibraryEntry;
import app.searchBar.SearchAudio;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaylistStrategy extends SearchAudio {
    public SearchPlaylistStrategy(final String user) {
        super(user);
    }

    /**
     * @return List of playlists from library.
     */
    @Override
    protected List<LibraryEntry> fetchEntries() {
        return new ArrayList<>(Admin.getInstance().getPlaylists());
    }
}
