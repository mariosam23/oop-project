package app.audio.Files;

import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;
    private final String owner;

    public Episode(final String name, final Integer duration, final String description,
                   final String owner) {
        super(name, duration);
        this.description = description;
        this.owner = owner;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getType() {
        return "episode";
    }
}
