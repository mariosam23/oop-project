package app.recommendations;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

public class RandomSongStrategy extends RecommendationStrategy<Song> {
    private final Integer minElapsedTime = 30;

    public RandomSongStrategy(final User currentUser) {
        super(currentUser);
        lastRecommendation = null;
    }

    @Override
    public Song getRecommendation() {
        User currentUser = super.getUser();
        if (currentUser.getPlayer().getSource() == null) {
            return null;
        }

        AudioFile currentAudio = currentUser.getPlayer().getSource().getAudioFile();
        if (!currentAudio.getType().equals("song")) {
            throw new IllegalArgumentException("Current audio is not a song");
        }

        int elapsedDuration = currentAudio.getDuration() - currentUser.getPlayer()
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
        return recommendedSong;
    }
}
