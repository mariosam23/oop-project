package app.pages;

import app.audio.Collections.Podcast;
import app.pages.pageContent.Announcement;
import app.user.Host;

import java.util.List;

/**
 * The type Host page.
 */
public final class HostPage extends Page {
    private List<Podcast> podcasts;
    private List<Announcement> announcements;
    private String owner;

    /**
     * Instantiates a new Host page.
     *
     * @param host the host
     */
    public HostPage(final Host host) {
        podcasts = host.getPodcasts();
        announcements = host.getAnnouncements();
        owner = host.getUsername();
    }

    @Override
    public String printCurrentPage() {
        return "Podcasts:\n\t%s\n\nAnnouncements:\n\t%s"
               .formatted(podcasts.stream().map(podcast -> "%s:\n\t%s\n"
                          .formatted(podcast.getName(),
                                     podcast.getEpisodes().stream().map(episode -> "%s - %s"
                          .formatted(episode.getName(), episode.getDescription())).toList()))
                          .toList(),
                          announcements.stream().map(announcement -> "%s:\n\t%s\n"
                          .formatted(announcement.getName(), announcement.getDescription()))
                          .toList());
    }

    @Override
    public String pageType() {
        return "Host";
    }

    @Override
    public String pageOwner() {
        return owner;
    }
}
