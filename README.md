# Proiect GlobalWaves

---

### Application Overview

&emsp; This is the last stage of the `GlobalWaves` project , which emulates Spotify's
functionalities. A user can create playlists, like favorite songs, and view the 
application's recommendations for them. Additionally, the application offers features 
for content creators, who can easily add songs, albums, podcasts to the database, and 
personalize their page to their liking. This app is known for well paying its artists,
and for very good recommendations.

---

### Inheritance Hierarchy

- LibraryEntry
  - AudioFile
    -  Song
    - Episode
  - AudioCollection
    - Album
    - Podcast
    - Playlist


- SearchBase
  - SearchCreator
    - SearchArtistStrategy
    - SearchHostStrategy
  - SearchAudio
    - SearchAlbumStrategy
    - SearchPlaylistStrategy
    - SearchPodcastStrategy
    - SearchSongStrategy


- UserAbstract
  - ContentCreator
    - Artist
    - Host
  - User


- Page
  - ArtistPage
  - HostPage
  - UserHomePage
  - UserContentPage


- RecommendationStrategy
  - FansPlaylistStrategy
  - RandomPlaylistStrategy
  - RandomSongStrategy
---

### Package Structure

- audio/ &rarr; Contains all the audio files and collection of audio files.
  All audio files inherit the `LibraryEntry` class.
- commandHandle/ &rarr; Manages the commands of users, artists, hosts.
  - The OutputBuilder class uses the Builder design pattern.
    This pattern facilitates the construction of an output message, tailored to
    the specifics of each command. Generics were used for quality coding.
  - The CommandType class is an enum, incorporating anonymous classes
    to implement the abstract execute method within it.
    This approach is a variant of the Command design pattern.
  - The CommandExecution class interacts with CommandType, adding to the dictionary
    all methods defined in the enum. When a command is invoked, it is searched
    in the dictionary and the execute method is called.


- pages/ &rarr; Contains pages of each user along with their content.


- player/ &rarr; Presents classes that offer basic functionalities of a player.
  Each regular user has their own player, from where they can listen to music, albums,
  playlists, podcasts.


- recommendations/ &rarr; Contains classes that implement recommendation strategies.
  - The Strategy design pattern was used to implement a specific recommendation algorithm
  for each type of audio file or content creator.


- searchBar/ &rarr; Contains search strategies and applicable filters.
  - The Strategy design pattern was used to implement a specific search algorithm for
  each type of audio file or content creator. _Factory_ in combination with _Singleton_ was implemented
  to generate instances of strategies in this package.


- users/ &rarr; Presents classes that define the application's users: regular user,
  artist, and host.

---

### Design Patterns
- **Builder**: This design pattern was used to construct output messages, tailored to
  each command. Thus, code repetition was avoided and a more qualitative code was offered.
  The `OutputBuilder` class implements this design pattern.


- **Command**: This design pattern was used to implement the commands of users,
  artists, and hosts. The CommandType class is an enum, incorporating anonymous classes
  to implement the abstract execute method within it.


- **Factory**: Used to generate instances of related classes in the `searchBar` package.


- **Strategy**: This design pattern was used to implement a specific search algorithm for
  each type of audio file or content creator. Was also used to implement a specific
  recommendation algorithm.


- **Singleton**: Used for the Admin class and together with factory type classes, for
  memory efficiency as there is a need for only one instance of these.

---

#### &copy; Sampetru Mario 321Ca
