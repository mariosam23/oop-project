package app.searchBar;

import app.searchBar.filters.Filters;
import app.searchBar.strategies.SearchStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for search strategies. It contains the common methods and attributes
 * @param <T> LibraryEntry or ContentCreator.
 */
public abstract class SearchBase<T> implements SearchStrategy<T> {
    protected List<T> results;
    protected final String user;
    protected static final Integer MAX_RESULTS = 5;
    protected String lastSearchType;
    protected T lastSelected;

    public SearchBase(final String user) {
        this.user = user;
        this.results = new ArrayList<>();
    }

    /**
     * Fetches all the entries from the library
     * @return a list of entries
     */
    protected abstract List<T> fetchEntries();

    /**
     * Searches for the entries that match the filters
     * @param type the type of the entry
     * @param filters the filters to be applied
     * @return a list of entries
     */
    public abstract List<T> search(String type, Filters filters);

    /**
     * Selects an element from the results list
     * @param index The index of the element to select
     * @return
     */
    @Override
    public T select(final Integer index) {
        if (index < 0 || index > this.results.size()) {
            results.clear();
            return null;
        }

        this.lastSelected = results.get(index - 1);
        results.clear();
        return this.lastSelected;
    }

    /**
     * Clears the results list
     */
    @Override
    public void clearSelection() {
        this.lastSelected = null;
        this.lastSearchType = null;
    }

    /**
     * @return the last selected element
     */
    @Override
    public T getLastSelected() {
        return this.lastSelected;
    }

    /**
     * @return the last search type
     */
    @Override
    public String getLastSearchType() {
        return this.lastSearchType;
    }
}
