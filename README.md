Of course! I have reformatted and enhanced your README file to be clean, professional, and easy to navigate, using standard Markdown practices for a polished look on platforms like GitHub.

Here is the revised version:

***

# Lava JDA Music Bot

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

## 🎵 Commands

Here are all the available commands:

| Command | Description |
| :--- | :--- |
| **/play `<query>`** | Plays a song or adds it to the queue. The query can be a song name, YouTube URL, SoundCloud URL, etc. |
| **/repeat `<mode>`** | Sets the repeat mode. Options: `SINGLE` (repeats the current song), `QUEUE` (repeats the queue), `OFF`. |
| **/skip** | Skips the currently playing song and plays the next one in the queue. |
| **/volume `<level>`** | Adjusts the player volume from 1 to 100. |

## 🚀 Self-Hosting Guide

Hosting the bot yourself gives you full control and the best performance.

### Prerequisites

- **Java 17 or higher:** You need a recent Java Development Kit (JDK) installed.
- **A running Lavalink Server:** This bot requires a connection to a Lavalink server to function. You can host one yourself or use a public service.
    - Find a list of public Lavalink servers [here](https://lavalink-list.appujet.site/).
    - To host your own, see the [official Lavalink repository](https://github.com/lavalink-devs/Lavalink).
- **A Discord Bot Token:** You need to create a bot application in the [Discord Developer Portal](https://discord.com/developers/applications).

### Setup Instructions

1.  **Clone the Repository**
    ```sh
    git clone https://github.com/your-username/your-repo-name.git
    cd your-repo-name
    ```

2.  **Configure `settings.yml`**
    Create a file named `settings.yml` in the root directory of the project and fill it with your details. A template is provided in the Configuration section below.

3.  **Build the Project**
    This project uses Maven to build. Run the following command in the root directory:
    ```sh
    mvn clean package
    ```
    This will create a `.jar` file in the `target/` directory.

4.  **Run the Bot**
    Execute the compiled JAR file to start your bot (replace the filename with your actual JAR file name).
    ```sh
    java -jar target/your-bot-name-1.0.0.jar
    ```

### ⚙️ Configuration (settings.yml)

Create this file and place it in the same directory where you run the bot's JAR file.

```yaml
# ---------------------------------- #
#      Lava JDA Music Bot Config     #
# ---------------------------------- #

# Your discord bot token, get it from https://discord.com/developers/applications
discord-bot-token: "YOUR_TOKEN_HERE"

# The default volume of the bot when it starts playing in a new server.
# Value must be between 1 and 100.
default-volume: 10

# --- Lavalink Server Details ---
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


# --- Do Not Edit Below This Line ---
version: "1.0.0"
```

## 🗺️ Roadmap

Here are some of the features planned for future updates:

- **Auto-Leave:** The bot will automatically leave the voice channel after a configurable amount of time when it's left alone.
- **/lyrics Command:** A new command to fetch and display the lyrics for the currently playing song.
- **Single-Guild Mode:** An option in the config to restrict the bot to operate in only one server, simplifying permissions and setup for private use.
- **DJ Role:** A configurable "DJ role" that can exclusively use music commands.

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📜 License

This project is licensed under the MIT License. See the `LICENSE` file for more information.