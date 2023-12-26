package app.pages.pageContent;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Announcement.
 */
@Getter @Setter
public class Announcement {
    private String name;
    private String description;

    /**
     * Instantiates a new Announcement.
     *
     * @param name        the name
     * @param description the description
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
