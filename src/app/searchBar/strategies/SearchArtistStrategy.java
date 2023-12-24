package app.searchBar.strategies;

import app.Admin;
import app.searchBar.SearchCreator;
import app.user.ContentCreator;

import java.util.ArrayList;
import java.util.List;

public class SearchArtistStrategy extends SearchCreator {
    public SearchArtistStrategy(final String user) {
        super(user);
    }

    /**
     * @return List of artists from library.
     */
    @Override
    protected List<ContentCreator> fetchEntries() {
        return new ArrayList<>(Admin.getInstance().getArtists());
    }
}
