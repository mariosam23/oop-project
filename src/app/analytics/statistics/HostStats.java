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
}
