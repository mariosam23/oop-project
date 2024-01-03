package app.notifications;

import lombok.Getter;
import lombok.Setter;

/**
 * Notifications data class
 */
@Getter @Setter
public class Notification {
    private String name;
    private String description;

    public Notification(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
