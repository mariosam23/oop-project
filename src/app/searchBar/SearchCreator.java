package app.searchBar;

import app.searchBar.filters.Filters;
import app.user.ContentCreator;

import java.util.List;

/**
 * Abstract class for searching a content creator.
 */
public abstract class SearchCreator extends SearchBase<ContentCreator> {
    public SearchCreator(final String user) {
        super(user);
    }

    /**
     * Specific implementation of search for content creators.
     * @param type
     * @param filters
     * @return a list of content creators after applying the filters
     */
    @Override
    public List<ContentCreator> search(final String type, final Filters filters) {
        List<ContentCreator> entries = fetchEntries();

        if (filters.getName() != null) {
            entries.removeIf(contentCreator -> !contentCreator.getUsername().toLowerCase()
                    .startsWith(filters.getName().toLowerCase()));
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.lastSearchType = type;
        this.results = entries;
        return entries;
    }
}
