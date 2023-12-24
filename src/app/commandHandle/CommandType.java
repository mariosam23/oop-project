package app.commandHandle;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * Enum that contains all the commands that can be executed.
 * Each command has a method that will be called when the command is executed.
 */
public enum CommandType {
    SEARCH {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.search(command);
        }
    },
    SELECT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.select(command);
        }
    },
    LOAD {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.load(command);
        }
    },
    PLAY_PAUSE {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.playPause(command);
        }
    },
    REPEAT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.repeat(command);
        }
    },
    SHUFFLE {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.shuffle(command);
        }
    },
    FORWARD {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.forward(command);
        }
    },
    BACKWARD {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.backward(command);
        }
    },
    LIKE {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.like(command);
        }
    },
    NEXT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.next(command);
        }
    },
    PREV {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.prev(command);
        }
    },
    CREATE_PLAYLIST {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.createPlaylist(command);
        }
    },
    ADD_REMOVE_IN_PLAYLIST {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addRemoveInPlaylist(command);
        }
    },
    SWITCH_VISIBILITY {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.switchVisibility(command);
        }
    },
    FOLLOW {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.follow(command);
        }
    },
    SHOW_PLAYLISTS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.showPlaylists(command);
        }
    },
    STATUS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.status(command);
        }
    },
    SHOW_PREFERRED_SONGS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.showLikedSongs(command);
        }
    },
    GET_TOP5_SONGS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getTop5Songs(command);
        }
    },
    GET_TOP5_PLAYLISTS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getTop5Playlists(command);
        }
    },
    CHANGE_PAGE {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.changePage(command);
        }
    },
    PRINT_CURRENT_PAGE {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.printCurrentPage(command);
        }
    },
    ADD_USER {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addUser(command);
        }
    },
    DELETE_USER {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.deleteUser(command);
        }
    },
    SHOW_ALBUMS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.showAlbums(command);
        }
    },
    SHOW_PODCASTS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.showPodcasts(command);
        }
    },
    ADD_ALBUM {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addAlbum(command);
        }
    },
    REMOVE_ALBUM {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.removeAlbum(command);
        }
    },
    ADD_EVENT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addEvent(command);
        }
    },
    REMOVE_EVENT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.removeEvent(command);
        }
    },
    ADD_MERCH {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addMerch(command);
        }
    },
    ADD_PODCAST {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addPodcast(command);
        }
    },
    REMOVE_PODCAST {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.removePodcast(command);
        }
    },
    ADD_ANNOUNCEMENT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.addAnnouncement(command);
        }
    },
    REMOVE_ANNOUNCEMENT {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.removeAnnouncement(command);
        }
    },
    SWITCH_CONNECTION_STATUS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.switchConnectionStatus(command);
        }
    },
    GET_TOP5_ARTISTS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getTop5Artists(command);
        }
    },
    GET_TOP5_ALBUMS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getTop5Albums(command);
        }
    },
    GET_ALL_USERS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getAllUsers(command);
        }
    },
    GET_ONLINE_USERS {
        @Override
        public ObjectNode execute(final CommandInput command) {
            return CommandRunner.getOnlineUsers(command);
        }
    };

    /**
     * Abstract method that will be implemented by each command.
     * @return the result of the command
     */
    public abstract ObjectNode execute(CommandInput command);
}
