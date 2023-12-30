package app.recommendations;

import app.audio.LibraryEntry;
import app.user.UserAbstract;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class RecommendationStrategy {
    protected LibraryEntry lastRecommendation = null;
    protected String lastRecommendationType = null;
    private UserAbstract userAbstract;

    public RecommendationStrategy(final UserAbstract currentUser) {
        this.userAbstract = currentUser;
    }

    /**
     * Calculates the recommendation asked by the user.
     * @return
     */
    public abstract LibraryEntry getRecommendation();
}
