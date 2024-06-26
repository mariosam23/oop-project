package app.user;

import app.pages.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Content creator.
 */
@Setter @Getter
public abstract class ContentCreator extends UserAbstract {
    private String description;
    private Page page;
    private List<String> subscribers = new ArrayList<>();

    /**
     * Instantiates a new Content creator.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public ContentCreator(final String username, final int age, final String city) {
        super(username, age, city);
    }
}
