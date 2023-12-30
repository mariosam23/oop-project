package app.recommendations;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;
import app.user.UserAbstract;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

public class RandomSongStrategy extends RecommendationStrategy {
    private final Integer minElapsedTime = 30;

    public RandomSongStrategy(final UserAbstract currentUser) {
        super(currentUser);
    }

    @Override
    public LibraryEntry getRecommendation() {
        UserAbstract user = super.getUserAbstract();
        if (((User) user).getPlayer().getSource() == null) {
            return null;
        }

        AudioFile currentAudio = ((User) user).getPlayer().getSource().getAudioFile();
        if (!currentAudio.getType().equals("song")) {
            throw new IllegalArgumentException("Current audio is not a song");
        }

        int elapsedDuration = currentAudio.getDuration() - ((User) user).getPlayer()
                .getSource().getRemainedDuration();
        if (elapsedDuration < minElapsedTime) {
            return null;
        }

        String currentGenre = ((Song) currentAudio).getGenre();

        List<Song> allSongs = new ArrayList<>(Admin.getInstance().getSongs());
        List<Song> sameGenreSongs = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.getGenre().equals(currentGenre)) {
                sameGenreSongs.add(song);
            }
        }

        if (sameGenreSongs.isEmpty()) {
            return null;
        }

        Random random = new Random(elapsedDuration);
        int randomIndex = random.nextInt(sameGenreSongs.size());
        Song recommendedSong = sameGenreSongs.get(randomIndex);

        super.setLastRecommendation(recommendedSong);
        super.setLastRecommendationType("song");

        return recommendedSong;
    }
}
