package app.searchBar.strategies;

import app.Admin;
import app.audio.LibraryEntry;
import app.searchBar.SearchAudio;

import java.util.ArrayList;
import java.util.List;

public class SearchPodcastStrategy extends SearchAudio {
    public SearchPodcastStrategy(final String user) {
        super(user);
    }

    /**
     * @return List of podcasts from library.
     */
    @Override
    protected List<LibraryEntry> fetchEntries() {
        return new ArrayList<>(Admin.getInstance().getPodcasts());
    }
}
