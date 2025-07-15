# LavMusicPlayer

A Java-based music player application that provides a WebSocket API for controlling music playback. LavMusicPlayer is a fork of [LavMusicBot](https://github.com/LOLYAY-INC/LavMusicBot) but **replacing discord with local playback**, functioning like Spotify desktop or any other local music player.

## 🌐 Web Interface

LavMusicPlayer comes with a modern, responsive web interface built with Svelte. The interface provides:

- Search functionality across multiple music sources
- (basic) Playlist management
- Media controls (play, pause, skip, volume, etc.)
- Track position control
- Lyrics display
- Headless support (keeps playing playlist when tab closed)

The default web interface is open source! The bundled files are Vite production distribution files. You can find the source code at [https://github.com/LOLYAY-INC/LavMusicPlayerWeb](https://github.com/LOLYAY-INC/LavMusicPlayerWeb).

### Customizing the Web Interface

When LavMusicPlayer runs for the first time, it downloads the bundled web assets into a local `assets` directory in the same folder as the JAR file. You can modify or replace these files to customize the interface:

1. Run LavMusicPlayer once to extract the assets
2. Navigate to the `assets` directory
3. Modify the files as needed
4. Restart LavMusicPlayer to see your changes

Or You can Change the Url LavMusicPlayer Downloads from in the Config under panel -> assets-url.
It can Either be:
- A direct Link to a zip with the files
- A Link to a Github Repo, With the latest Release having a "assets.zip" Asset Attached that gets downloaded.

You can also create your own site entirely, without depending on the built in web server (See API_DOCUMENTATION.md).

## ✨ Features

### Core Features
- **Local Music Playback**: Play music directly on your device with a demon.
- **WebSocket API**: Control playback programmatically through a comprehensive WebSocket API
- **HTTP Server**: Access the web interface and static assets through the built-in HTTP server
- **System Tray Integration**: Control the player from your system tray for easy access
- **Headless Mode**: Continue playback even when the web interface is closed

### Music Features
- **Multi-Source Search**: Search for tracks across various sources including YouTube
- **Playlist Support**: Create and manage playlists for organized listening
- **Volume Control**: Adjust playback volume to your preference
- **Track Position Control**: Seek to specific positions within tracks
- **Lyrics Support**: View lyrics for currently playing songs (using MusixMatch)
- **Live Lyrics**: Highlight the current line of the song being played

### Technical Features
- **Packet-Based Communication**: Efficient communication protocol between clients and server
- **Event-Driven Architecture**: Responsive design using an event bus system
- **Media Transport Controls Integration**: Control playback using system media keys

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Internet connection for streaming music

### Installation
1. Download the latest release from the [releases page](https://github.com/LOLYAY-INC/LavMusicPlayer/releases)
2. Run the JAR file using: `java -jar LavMusicPlayer.jar`
3. Access the web interface at `http://localhost:80`
4. Connect to the WebSocket API at `ws://localhost:3272`

### Command Line Arguments
- `-DEBUG`: Enable debug mode for detailed logging
- `-OVERWRITE_CONFIG`: Force creation of a new configuration file
- `-p=PORT` or `-port=PORT`: Specify the HTTP server port (default: 80)
- `-s` or `-silent`: Enable silent mode ( No popups when errors happen)
- `-noextract` or `-no-extract`: Disable resource extraction

## 📚 API Documentation

LavMusicPlayer provides a comprehensive WebSocket API that allows you to control playback programmatically. This makes it perfect for integration with other applications or creating custom interfaces.

For detailed information about the WebSocket API, packet system, and how to interact with LavMusicPlayer programmatically, please see the [API Documentation](API_DOCUMENTATION.md).

## ⚙️ Configuration

LavMusicPlayer uses a JSON configuration file (`config.json`) that is created in the same directory as the JAR file on first run. You can edit this file to configure various aspects of the application.

### Configuration Structure

The configuration file has the following main sections:

```json
{
  "music": {
    "volume": 0,
    "country-code": ""
  },
  "additional_sources": {
    "yt-oauth2-refresh-token": "",
    "spotify-client-id": "",
    "spotify-client-secret": "",
    "apple-music-token": "",
    "tidal-token": "",
    "deezer-decryption-key": "",
    "deezer-arl-cookie": ""
  },
  "lyrics": {
    "musicmatch-auth-token": ""
  }
}
```

### Setting Up Music Services

#### YouTube OAuth

To prevent issues with YouTube, the player can use an authenticated account:

1. Start LavMusicPlayer for the first time
2. You will be prompted to open a browser window and log into your YouTube account
3. The application will output a refresh token in the console
4. Copy this token to the `yt-oauth2-refresh-token` field in the config file

#### Spotify Integration

To enable Spotify search:

1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new application
3. Copy the Client ID and Client Secret
4. Add them to the config file:
   ```json
   "spotify-client-id": "YOUR_SPOTIFY_CLIENT_ID",
   "spotify-client-secret": "YOUR_SPOTIFY_CLIENT_SECRET"
   ```

#### Apple Music Integration

To enable Apple Music search without a Developer Account:

1. Go to https://music.apple.com
2. Open DevTools and go to the Sources tab
3. Search with this regex `(?<token>(ey[\w-]+)\.([w-]+)\.([w-]+))` in all index-*.js files
4. Copy the token from the source code
5. Add it to the config file:
   ```json
   "apple-music-token": "YOUR_APPLE_MUSIC_TOKEN"
   ```

#### Deezer Integration

To enable Deezer search:

1. To get a Deezer decryption key, follow the instructions at [this gist](https://gist.github.com/svbnet/b79b705a4c19d74896670c1ac7ad627e)
2. To obtain the Deezer ARL cookie:
   - Log in to your Deezer account through a web browser
   - Open the browser's developer tools (usually by pressing F12)
   - Navigate to the "Storage" or "Application" tab
   - Find the "arl" cookie within the list of cookies
   - Copy the long alphanumeric string
3. Add them to the config file:
   ```json
   "deezer-decryption-key": "YOUR_DEEZER_DECRYPTION_KEY",
   "deezer-arl-cookie": "YOUR_DEEZER_ARL_COOKIE"
   ```

#### Tidal Integration

To enable Tidal search:

1. Install Tidal Desktop
2. Install 'Fiddler Everywhere' and enable for HTTPS captures
3. Open tidal desktop
4. Login to the Tidal desktop app and start playing a track
5. Stop Fiddler capture
6. Look for host: desktop.tidal.com
7. Double click that request
8. In the Inspectors pane, scroll down to "Miscellaneous" and find "x-tidal-token"
9. Copy the token
10. Add it to the config file:
    ```json
    "tidal-token": "YOUR_TIDAL_TOKEN"
    ```
Done! Now restart the demon if it has started (check your tray for the LavMusicBot Icon) and you're set! Go to http://localhost:80 to open the UI.


## Roadmap
- Implement V2 Protocol including Moving playlists to server side to remove headless mode stuff
- Implement live lyrics
- Implement Playlist management and Import/Exporting
- Mobile Support


## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

*Note: I made this [Lavalink JSON to Java Converter](https://lolyay.dev/tools/lavalinkconverter/) to use with the JSON you get from: [The lavalink Server List](https://lavalink-list.appujet.site/non-ssl)*

## 📜 License

LavMusicPlayer is released under the [Apache License 2.0](LICENSE).


