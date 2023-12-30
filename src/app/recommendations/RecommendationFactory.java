package app.recommendations;

import app.user.UserAbstract;

public class RecommendationFactory {
    private RecommendationFactory() { }
    private static RecommendationFactory instance = null;
    public static RecommendationFactory getInstance() {
        if (instance == null) {
            instance = new RecommendationFactory();
        }

        return instance;
    }

    public RecommendationStrategy getStrategy(final String strategyName, final UserAbstract user) {
        return switch (strategyName) {
            case "random_song" -> new RandomSongStrategy(user);
            case "random_playlist" -> new RandomPlaylistStrategy(user);
            case "fans_playlist" -> new FansPlaylistStrategy(user);
            default -> throw new IllegalArgumentException("Not Implemented recommendation name");
        };
    }
}
