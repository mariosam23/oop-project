package app.recommendations;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class RecommendationStrategy<T> {
    private User user;
    protected T lastRecommendation;

    public RecommendationStrategy(final User currentUser) {
        user = currentUser;
        lastRecommendation = null;
    }

    public abstract T getRecommendation();
}
