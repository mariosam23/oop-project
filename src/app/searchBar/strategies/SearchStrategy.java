package app.searchBar.strategies;

import app.searchBar.filters.Filters;

import java.util.List;

/**
 * Interface for search strategies
 */
public interface SearchStrategy<T> {
    /**
     * Searches in the current library based on a specific type and filters
     * @param type The type of search
     * @param filters The filters to apply in the search
     * @return A list of search results
     */
    List<T> search(String type, Filters filters);

    /**
     * Selects an element from the list of results
     * @param index The index of the element to select
     * @return The selected element
     */
    T select(Integer index);

    /**
     * Clears the current selection
     */
    void clearSelection();

    /**
     * Returns the last selection
     * @return The last selected element
     */
    T getLastSelected();

    /**
     * Returns the last search type
     * @return The last type of search performed
     */
    String getLastSearchType();
}
