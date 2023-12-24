package app.searchBar.strategies;

import app.Admin;
import app.searchBar.SearchCreator;
import app.user.ContentCreator;

import java.util.ArrayList;
import java.util.List;

public class SearchHostStrategy extends SearchCreator {
    public SearchHostStrategy(final String user) {
        super(user);
    }

    /**
     * @return List of hosts from library.
     */
    @Override
    protected List<ContentCreator> fetchEntries() {
        return new ArrayList<>(Admin.getInstance().getHosts());
    }
}
