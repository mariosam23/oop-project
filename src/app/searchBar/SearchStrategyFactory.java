package app.searchBar;

import app.searchBar.strategies.SearchAlbumStrategy;
import app.searchBar.strategies.SearchArtistStrategy;
import app.searchBar.strategies.SearchHostStrategy;
import app.searchBar.strategies.SearchPlaylistStrategy;
import app.searchBar.strategies.SearchPodcastStrategy;
import app.searchBar.strategies.SearchSongStrategy;
import app.searchBar.strategies.SearchStrategy;
import app.utils.Enums;
import lombok.Getter;

/**
 * Factory class for search strategies. As there is no need for multiple instances,
 * this is designed as a Singleton to save on memory usage.
 */
public final class SearchStrategyFactory {
    private String type;
    @Getter
    private static SearchStrategyFactory instance = new SearchStrategyFactory();

    private SearchStrategyFactory() { }

    /**
     * Returns a search strategy based on the type of search and user
     * @param typeAudio The type of audio content to search for
     * @param user The user performing the search
     * @return A specific SearchStrategy instance based on the search type
     */
    public SearchStrategy<?> getSearchStrategy(final String typeAudio, final String user) {
        this.type = typeAudio;
        return switch (Enums.SearchType.valueOf(type.toUpperCase())) {
            case PODCAST -> new SearchPodcastStrategy(user);
            case SONG -> new SearchSongStrategy(user);
            case PLAYLIST -> new SearchPlaylistStrategy(user);
            case ALBUM -> new SearchAlbumStrategy(user);
            case ARTIST -> new SearchArtistStrategy(user);
            case HOST -> new SearchHostStrategy(user);
            default -> null;
        };
    }
}
