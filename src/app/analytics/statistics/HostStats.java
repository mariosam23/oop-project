package app.analytics.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter @Setter
public class HostStats {
    private Map<String, Integer> topEpisodes;
    private Set<String> listeners;

    public HostStats() {
        topEpisodes = new HashMap<>();
        listeners = new HashSet<>();
    }

    /**
     * Adds an episode to the top episodes map
     * @param episode
     */
    public void addTopEpisode(final String episode) {
        topEpisodes.putIfAbsent(episode, 0);
        topEpisodes.put(episode, topEpisodes.get(episode) + 1);
    }

    /**
     * Adds a listener to the listeners set
     * @param user
     */
    public void addListener(final String user) {
        listeners.add(user);
    }

    /**
     * Returns the number of listeners
     * @return
     */
    public Integer getListenersNumber() {
        return listeners.size();
    }

    /**
     * Resets the statistics
     */
    public void reset() {
        topEpisodes.clear();
        listeners.clear();
    }
}
