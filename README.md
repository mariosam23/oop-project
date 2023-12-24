# Proiect GlobalWaves - Etapa 2 - Pagination

---

### Prezentarea aplicatiei

&emsp; Aceasta este etapa a doua a proiectului `GlobalWaves`, care imita functionalitatile Spotify. Un utilizator poate sa isi creeze playlist-uri, aprecieze melodiile favorite, precum si sa vada recomandarile facute de aplicatie pentru acesta. De asemenea, aplicatie ofera functionalitati pentru creatorii de continut, care pot adauga cu usurinta in baza de date melodii, albume, podcast-uri si isi pot personaliza dupa plac pagina.

---

### Mostenirile folosite

- LibraryEntry
  - Artist
  - Host
  - AudioFile
    -  Song
    - Episode
  - AudioCollection
    - Album
    - Podcast
    - Playlist


- Page
  - ArtistPage
  - HostPage
  - UserHomePage
  - UserContentPage


- SearchStrategyBase, care implementeaza interfata SearchStrategy
  - SearchAlbumStrategy
  - SearchArtistStrategy
  - SearchHostStrategy
  - SearchPlaylistStrategy
  - SearchPodcastStrategy
  - SearchSongStrategy

---

### Descrierea pachetelor

- audio/ &rarr; Pachetul ce contine toate entitatile audio din cadrul aplicatiei.
Toate fisierele audio mostenesc clasa LibraryEntry.


- commandHandle/ &rarr; Se ocupa de gestionarea comenzilor utilizatorilor, artistilor, host-urilor.
  - Clasa `OutputBuilder` utilizeaza modelul de design **Builder**. 
  Acest model faciliteaza construirea unui mesaj de iesire, adaptat
  particularitatilor fiecarei comenzi. Am folosit genericitatea pentru un cod mai calitativ.
  - Clasa `CommandType` este un enum, care incorporeaza clase anonime
  pentru a implementa metoda abstracta _execute_ din interiorul acesteia.
  Aceasta abordare este o varianta a modelului de design **Command**.
  - Clasa `CommandExecution`, interactioneaza cu `CommandType`, adaugand in dictionar
  toate metodele definite in enum. Cand o comanda este invocata, se cauta 
  in dictionar si se apeleaza metoda _execute_.



- pages/ &rarr; Contine paginile fiecarui utilizator impreuna cu continutul accestora.
  - Am folosit clasa `PageFactory` pentru a genera instante ale claselor inrudite din acest
pachet. Aceasta este de tip **Singleton** pentru eficienta din punct de vedere al memoriei.


- player/ &rarr; Prezinta clase care ofera functionalitatile de baza ale unui player.
Fiecare utilizator normal are propriul player, de unde poate asculta muzica, albume,
playlist-uri, podcast-uri.


- searchBar/ &rarr; Contine strategiile de cautare si filtrele care pot fi aplicate.
  - Am folosit modelul de design **Strategy** pentru a implementa un algoritm de
  cautare specific fiecarui tip de fisier audio sau creator de continut. Am implementat
  **Factory** pentru a genera instante ale strategiilor din acest pachet.


- users/ &rarr; Prezinta clasele care definesc utilizatorii aplicatiei: utilizator normal
, artist si host.

---
### Descrierea modelelor de design folosite
- **Builder**: Am folosit acest model de design pentru a construi mesaje de iesire, adaptate
  fiecarei comenzi in parte. Astfel, am putut sa evit repetarea codului si sa
  ofer un cod mai calitativ. Clasa `OutputBuilder` implementeaza acest model de design.


- **Command**: Am folosit acest model de design pentru a implementa comenzile utilizatorilor,
  artistilor si host-urilor. Clasa `CommandType` este un enum, care incorporeaza clase anonime
  pentru a implementa metoda abstracta _execute_ din interiorul acesteia.


- **Factory**: Folosit pentru a genera instante ale claselor inrudite (vezi pachetele `pages`
  si `searchBar`).


- **Strategy**: Am folosit acest model de design pentru a implementa un algoritm de cautare
  specific fiecarui tip de fisier audio sau creator de continut.


- **Singleton**: Utilizat pentru clasa `Admin` si impreuna cu clasele de tip _factory_, pentru
  a fi eficienti din punct de vedere al memoriei utilizate, intrucat este nevoie de o singura
  instanta a acestora.

---

#### Mentiuni
  - Am folosit scheletul de cod de la rezolvarea oficiala.
  - Am folosit Chat-GPT pentru a inlocui for-uri/while-uri imbricate cu stream-uri.

---


#### &copy; Sampetru Mario 321Ca
