<p align="center">
  <img src="https://cdn.lolyay.dev/img/lavmusicbot.png" alt="LavMusicBot Logo" width="150">
</p>

# LavMusicBot

A simple yet powerful music bot for Discord, built with JDA and powered by Lavalink. It's designed to be lightweight, easy to use, and simple to self-host, providing high-quality audio playback for your server.

<p align="center">
  <img src="https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white" alt="Discord">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
</p>

---

## 📋 Table of Contents

- [Use The Public Instance](#-use-the-public-instance)
- [✨ Features](#-features)
- [🎵 Commands](#-commands)
- [🚀 Self-Hosting Guide](#-self-hosting-guide)
    - [Prerequisites](#prerequisites)
    - [Setup Instructions](#setup-instructions)
    - [Configuration](#-configuration-settingsyml)
- [🗺️ Roadmap](#️-roadmap)
- [🤝 Contributing](#-contributing)
- [📜 License](#-license)

---

## 🤖 Use The Public Instance

Don't want to bother with hosting it yourself? You can add the official instance of the bot to your server with just one click.

*Note: The public bot is a shared resource. For the best performance and reliability, self-hosting is always recommended.*

<a href="https://discord.com/oauth2/authorize?client_id=1381983517617033306">
  <img src="https://img.shields.io/badge/Add%20to%20Your%20Server-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Add to Discord">
</a>

## ✨ Features

- **High-Quality Audio:** Delivers smooth, high-quality music streaming by offloading audio processing to a Lavalink server.
- **Wide Source Support:** Play music from YouTube (search or URL), SoundCloud, direct HTTP MP3 links, and even Twitch streams.
- **Modern Slash Commands:** All commands are integrated as easy-to-use slash commands (`/`).
- **Queue Control:** Full queue system with options to repeat a single track or the entire queue.
- **Easy to Configure:** A single `settings.yml` file makes configuration a breeze.
- **Multi-Guild Support:** Works out of the box in multiple servers simultaneously.
- **Auto-Leave:** The bot automatically leaves the voice channel when it's left alone.

## 🎵 Commands

Here are all the available music commands:

| Command               | Description                                                                                                             |
|:----------------------|:------------------------------------------------------------------------------------------------------------------------|
| **/play `<song>`**    | Plays a song or adds it to the queue. Can be a song name, YouTube URL, SoundCloud URL, etc.                             |
| **/pause**            | Pauses the currently playing track.                                                                                     |
| **/resume**           | Resumes playback if paused.                                                                                             |
| **/stop**             | Stops playback and clears the queue.                                                                                    |
| **/skip**             | Skips the current track and plays the next one in the queue.                                                            |
| **/repeat `<mode>`**  | Sets the repeat mode. Options: `false/off` (no repeat), `true/all` (repeat queue), `one/single` (repeat current track). |
| **/volume `<1-150>`** | Sets the volume (1-150).                                                                                                |
| **/status**           | Shows the current playback status and queue information.                                                                |
| **/version**          | Displays the current version of the bot.                                                                                |
| **/changenode**       | Changes to a different Lavalink node if using multiple nodes.                                                           |
| **/lyrics**           | Shows you the lyrics of the currently playing song (DEPRECATED) Doesn't work                                                                      |

***Note**: If you receive the error **"Unknown Interaction"**, try restarting your Discord client with Ctrl+R*

***

### 🚀 Self-Hosting Guide

This guide is for running the bot using the pre-compiled releases. No development tools are required.

#### Prerequisites

- **Java 23 or higher:** You need Java installed to run the `.jar` file.
- **A running Lavalink Server:** This bot requires a connection to a Lavalink server. You can find public ones [here](https://lavalink-list.appujet.site/) or host your own.
- **A Discord Bot Token:** Create a bot application in the [Discord Developer Portal](https://discord.com/developers/applications).

### Setup Instructions

1.  **Download the Bot**
    Go to the [**Latest Release Page**](https://github.com/LOLYAY-INC/LavMusicBot/releases/latest) on GitHub. Download the `.jar` file (e.g., `LavMusicBot-1.0.0.jar`) and save it to a new, empty folder where you want the bot to live.

2.  **Generate the Configuration File**
    Open your terminal or command prompt, navigate to the folder where you saved the bot, and run it for the first time with the following command:
    ```sh
    java -jar LavMusicBot-1.0.0.jar
    ```
    *(Replace `LavMusicBot-1.0.0.jar` with the actual name of the file you downloaded).*

    The bot will state that a new configuration file was created and then shut down. This is expected. You will now see a `settings.yml` file in the folder.

3.  **Edit the Configuration**
    Open the newly created `settings.yml` file with any text editor. Fill in your `discord-bot-token` and your `lavalink-*` details. **The bot will not start without these values.**

4.  **Run the Bot**
    Once you have saved your changes to `settings.yml`, run the exact same command again:
    ```sh
    java -jar LavMusicBot-1.0.0.jar
    ```
    This time, the bot will read your configuration and connect to Discord. Congratulations, your bot is now online!
    *Note: If you get Bad Audio on Mobile, That's an Issue with your Lavalink Server, as in my Testing turning the `resamplingQuality` of your Lavalink Server to `HIGH` fixed it.* 

5. **Command Line Arguments**
   The bot supports several command line arguments:
   ```
   -DEBUG                    # Enables debug logging
   -OVERWRITE_CONFIG         # Forces overwriting of the config file
   -NO_REGISTER_COMMANDS     # Skips registering slash commands
   ```
---

### ⚙️ Configuration (settings.yml)

This `settings.yml` file will be automatically generated in the same folder as your `.jar` file after you run it for the first time. Open it and edit the values as needed.

### Guild Configuration Storage

The bot automatically creates a `guildconfigs` folder in the same directory as the jar file to store per-guild settings.
Each server gets its own JSON file (using the guild ID as the filename) that preserves settings like volume level,
repeat mode, and play count between bot restarts. These files are managed automatically and don't need manual editing.

```yaml
# ---------------------------------- #
#      LavMusicBot Config            #
# ---------------------------------- #

# Your discord bot token, get it from https://discord.com/developers/applications
discord-bot-token: "YOUR_TOKEN_HERE"

# The default volume of the bot when it starts playing in a new server.
# Value must be between 1 and 100.
default-volume: 10

# Set this to true if you want to clear the queue when the voice-channel is empty ( aka everyone left and the bot is alone in the channel ).
clear-on-empty-channel: true



using-nodes-json-file: false # Set this to true if you are using a nodes.json file, if you want to use the bottom config, set this to false
nodes-json-file: "C:\\path\\to\\nodes.json" # The path to your nodes.json file

# =======================
# Lavalink Server Details (Only if you're not using a nodes.json file)
# =======================
# Get these from your Lavalink provider or your own server.
# A list of public servers can be found at https://lavalink-list.appujet.site/

# The hostname or IP address of the Lavalink server.
lavalink-host: "lavalink.example.com"

# The port the Lavalink server is running on.
lavalink-port: 2000

# Set to 'true' if your Lavalink server uses a secure (SSL/TLS) connection.
lavalink-secure: false

# The password for your Lavalink server.
lavalink-password: "pleaseletmein"

  # ============
                     =============
# Permissions Configuration
# =========================

# Set this to true if you want to enable permissions
permissions-enabled: false

# Set this to true if you want to use a blacklist instead of a whitelist
whitelist-acts-as-blacklist: false

# The role IDs of the roles you want to allow to use the bot
role-id-whitelist:
  - "123123123"
  - "456456456"


# ====================
# Lyrics Configuration
# ====================
# Enable or disable lyrics functionality
# Set to 'true' to enable the /lyrics command
lyrics-enabled: false

# MusixMatch Authentication (Required for lyrics)
# ---------------------------------------------
# To use the lyrics feature, you need to provide a valid MusixMatch user token.
# Here's how to get it:
# 1. Sign up for a free account at https://www.musixmatch.com/
# 2. Log in to your MusixMatch account in your web browser
# 3. Open Developer Tools (F12 or right-click -> Inspect)
# 4. Go to the 'Application' tab (or 'Storage' in Firefox)
# 5. In the left sidebar, expand 'Cookies' and select 'https://www.musixmatch.com'
# 6. Find the cookie named 'musixmatchUserToken'
# 7. Copy the token value (a long string of characters) and paste it below
musixmatch-user-cookie: "YOUR_MUSIXMATCH_USER_TOKEN"


# Enable of disable the live lyrics feature, it highlights the current line playing, only works with musixmatch
# I think this is the best feature
live-lyrics-enabled: true

# How many milliseconds to move the line earlier than the song, higher = line gets highlighted earlier
live-lyrics-ping-compensation: 10


# --- Do Not Edit Below This Line ---
version: "${project.version}"
```
## Configuration Options

### Basic Settings
- `discord-bot-token`: Your Discord bot token from the [Discord Developer Portal](https://discord.com/developers/applications)
- `default-volume`: Default volume level (1-100) for the bot when joining a new server

### Lavalink Configuration
- `using-nodes-json-file`: Set to `true` to use a nodes.json file for Lavalink configuration.
- `nodes-json-file`: Path to your nodes.json file (if using nodes.json)
- **OR**
- `lavalink-host`: Hostname or IP of your Lavalink server
- `lavalink-port`: Port of your Lavalink server
- `lavalink-secure`: Set to `true` for SSL/TLS (`wss://`) connections
- `lavalink-password`: Authentication password for your Lavalink server

### Permission Settings
- `permissions-enabled`: Enable/disable role-based permissions
- `whitelist-acts-as-blacklist`: When enabled, the whitelist becomes a blacklist
- `role-id-whitelist`: List of role IDs that are allowed (or denied) to use the bot

### Lyrics Configuration

- `lyrics-enabled`: Enable/disable lyrics functionality
- `musixmatch-user-cookie`: MusixMatch user token for lyrics
- `live-lyrics-enabled`: Check [live lyrics](#live-lyrics)
- `live-lyrics-ping-compensation`: Check [live lyrics](#live-lyrics)

## Live Lyrics

This feature highlights the current line playing, currently only works with MusixMatch.
Doesn't work if you are repeating a song, only the first playback works for now.
It's a great feature, check it out!

### Ping Compensation

The live lyrics feature highlights the current line playing, so it's important to make sure the line is highlighted at
the right time.
The `live-lyrics-ping-compensation` option allows you to adjust how many milliseconds to move the line earlier than the
song, higher = line gets highlighted earlier.
The default value is 10 milliseconds, which is a good balance between highlighting the line and not highlighting it too
early.

## Nodes.json File
Nodes.json is a file that contains a list of Lavalink servers if you want to use more than one, if not you can use the standard config options.
In the config file, you can set the `using-nodes-json-file` to `true` and provide the path to your `nodes.json` file.
If you do that then you can omit the `lavalink-host`, `lavalink-port`, `lavalink-secure`, and `lavalink-password` fields.
You can get single json config from https://lavalink-list.appujet.site/non-ssl
```yaml
using-nodes-json-file: true
nodes-json-file: "path/to/nodes.json"
```
### Example nodes.json
```json
[
  {
    "identifier": "Lavalink Server 1",
    "password": "secret",
    "host": "lava.example.com",
    "port": 1234,
    "secure": false
  },
  {
    "identifier": "Lavalink Server 2",
    "password": "pleaseletmein",
    "host": "lava.somthig.else",
    "port": 1234,
    "secure": true
  }
]
```

## 🗺️ Roadmap

Here are some of the features planned for future updates:

- [x] **Auto-Leave:** The bot will automatically leave the voice channel after a configurable amount of time when it's left alone.
- [x] **/lyrics Command:** A new command to fetch and display the lyrics for the currently playing song. (Using
  MusixMatch)
- [x] **Live Lyrics:** Highlight the current line of the song being played
- [ ] **Single-Guild Mode:** An option in the config to restrict the bot to operate in only one server, simplifying permissions and setup for private use.
- [ ] **DJ Role:** A configurable "DJ role" that can exclusively use music commands.

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

*Note: I made this [Lavalink JSON to Java Converter](https://lolyay.dev/tools/lavalinkconverter/) to use with the JSON
you get from: [The lavalink Server List](https://lavalink-list.appujet.site/non-ssl)

## 📜 License

LavMusicBot is released under the [Apache License 2.0](https://github.com/LOLYAY-INC/LavMusicBot/blob/main/LICENSE).


*Yes I'm sorry, a bit of this Readme is AI generated, But its correct and accurate*
