package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import lombok.Getter;

import java.util.List;

public final class Podcast extends AudioCollection {
    @Getter
    private final List<Episode> episodes;
    private String owner;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
        this.owner = owner;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    @Override
    public boolean containsTrack(final AudioFile track) {
        return episodes.contains(track);
    }

    @Override
    public String getOwner() {
        return owner;
    }
}
