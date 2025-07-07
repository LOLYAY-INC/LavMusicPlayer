# LavMusicPlayer API Documentation

## Architecture Overview

LavMusicPlayer is a Java-based music player application that provides a WebSocket API for controlling music playback. It uses a packet-based communication system to handle requests and responses between clients and the server.

The application consists of the following main components:

1. **WebSocket Server**: Handles real-time communication between clients and the server
2. **HTTP Server**: Serves static assets and handles HTTP requests
3. **Packet System**: Manages the communication protocol between clients and the server
4. **Music Manager**: Controls music playback functionality
5. **Search System**: Provides track search capabilities across various sources

## Communication Protocol

LavMusicPlayer uses a packet-based communication protocol over WebSockets. Each packet has a unique opcode that identifies its type and purpose.

### Packet Structure

All packets follow this basic structure:

```json
{
  "opcode": 123,  // Unique identifier for the packet type
  "field1": "value1",
  "field2": "value2"
  // Additional fields specific to the packet type
}
```

### Packet Types

There are two main types of packets:

1. **C2S (Client-to-Server)**: Packets sent from the client to the server
2. **S2C (Server-to-Client)**: Packets sent from the server to the client

## WebSocket API Reference

### Connection

Connect to the WebSocket server at: `ws://localhost:3272`

### Media Control Packets

#### C2S (Client-to-Server) Packets

##### Search Track (Opcode: 111)

Search for tracks using a query string. This packet responds with [Search Track Results (Opcode: 111)](#search-track-results-opcode-111).

```json
{
  "opcode": 111,
  "query": "search term"
}
```

##### Play Track (Opcode: 112)

Play a specific track. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Update Player (Opcode: 200)](#update-player-opcode-200).

```json
{
  "opcode": 112,
  "track": {
    // MusicAudioTrack object
  }
}
```

##### Pause Track (Opcode: 1131)

Pause the currently playing track. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Update Player (Opcode: 200)](#update-player-opcode-200).

```json
{
  "opcode": 1131
}
```

##### Unpause Track (Opcode: 114)

Resume playback of a paused track. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Update Player (Opcode: 200)](#update-player-opcode-200).

```json
{
  "opcode": 114
}
```

##### Stop Track (Opcode: 115)

Stop the currently playing track. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Track Stopped (Opcode: 202)](#track-stopped-opcode-202).

```json
{
  "opcode": 115
}
```

##### Set Volume (Opcode: 116)

Set the playback volume. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Update Player (Opcode: 200)](#update-player-opcode-200).

```json
{
  "opcode": 116,
  "volume": 50  // Volume level (0-100)
}
```

##### Seek (Opcode: 118)

Seek to a specific position in the current track. This packet responds with [Success Packet (Opcode: -2)](#success-packet-opcode--2) and [Update Player Position (Opcode: 201)](#update-player-position-opcode-201).

```json
{
  "opcode": 118,
  "seek": 30000  // Position in milliseconds
}
```

##### Request Player Update (Opcode: 119)

Request the current state of the player. This packet responds with [Update Player (Opcode: 200)](#update-player-opcode-200).

```json
{
  "opcode": 119
}
```

##### Request Track Length (Opcode: 120)

Request the length of the current track. This packet responds with [Track Length (Opcode: 203)](#track-length-opcode-203).

```json
{
  "opcode": 120
}
```

#### S2C (Server-to-Client) Packets

##### Search Track Results (Opcode: 111)

Response to a search request.

```json
{
  "opcode": 111,
  "query": "search term",
  "results": [
    // Array of Search objects
  ]
}
```

##### Update Player (Opcode: 200)

Updates the client with the current player state.

```json
{
  "opcode": 200,
  "current": {
    // MusicAudioTrack object of the current track
  },
  "volume": 50,
  "paused": false,
  "playing": true,
  "headless": false
}
```

##### Update Player Position (Opcode: 201)

Updates the client with the current playback position.

```json
{
  "opcode": 201,
  "position": 15000  // Current position in milliseconds
}
```

##### Track Stopped (Opcode: 202)

Notifies the client that the track has stopped.

```json
{
  "opcode": 202
}
```

##### Track Length (Opcode: 203)

Response to a track length request.

```json
{
  "opcode": 203,
  "length": 180000  // Track length in milliseconds
}
```

### Utility Packets

#### Success Packet (Opcode: -2)

Indicates that a request was successful.

```json
{
  "opcode": -2,
  "packetOpcode": 112,  // The opcode of the original request
  "message": "Track title"  // Success message
}
```

#### Error Packet (Opcode: -1)

Indicates that an error occurred while processing a request.

```json
{
  "opcode": -1,
  "packetOpcode": 112,  // The opcode of the original request
  "message": "Error message"  // Error details
}
```

## Object Structures

### MusicAudioTrack

Represents a playable audio track. This object encapsulates all the information needed to play, display, and manage an audio track.

```json
{
  "trackInfo": {
    "title": "Track Title",
    "author": "Artist Name",
    "artWorkUrl": "https://example.com/artwork.jpg",
    "duration": 180000  // Track length in milliseconds
  },
  "encoded": "base64EncodedTrackData",  // Base64-encoded audio track data
  "startTime": 0  // Timestamp when track started playing (milliseconds)
}
```

### TrackInfo

Contains metadata about a track.

```json
{
  "title": "Track Title",
  "author": "Artist Name",
  "artWorkUrl": "https://example.com/artwork.jpg",
  "duration": 180000  // Track length in milliseconds
}
```

### Search

Represents a search result. This object contains information about the result of a search operation, including the found track (if any), the original query, the source of the search, the result status, and playlist data if applicable.

```json
{
  "track": {
    // MusicAudioTrack object (may be null if no track was found)
  },
  "query": "search term",
  "source": "youtube",  // Source of the search result (e.g., "youtube", "spotify")
  "result": {
    "status": "SUCCESS",  // Status can be: "SUCCESS", "ERROR", "NOT_FOUND", "PLAYLIST"
    "message": "Track found successfully."  // Human-readable result message
  },
  "playlistData": {  // Present only if result status is "PLAYLIST", null otherwise
    // PlaylistData object
  }
}
```

### PlaylistData

Represents a playlist. This object contains information about a collection of tracks, including the playlist name, the list of tracks, and the currently selected track.

```json
{
  "playlistName": "Playlist Name",
  "selectedTrackId": 0,  // Index of the selected track in the tracks array
  "tracks": [
    // Array of MusicAudioTrack objects
  ]
}
```

## HTTP API

The HTTP server runs on port 80 by default and serves the following endpoints:

- `/`: Serves the web interface
- `/assets/`: Serves static assets
- `/start-headless-on-close`: Special endpoint for headless mode operation that receives BeaconablePackets when the browser is closing. This endpoint allows the music player to continue playing in headless mode even after the web interface is closed. It processes packets like C2SStartHeadlessPacket and C2SStopHeadlessPacket to enable or disable headless mode.

## Usage Examples

### Searching for a Track

1. Send a C2S Search Track packet:
   ```json
   {
     "opcode": 111,
     "query": "never gonna give you up"
   }
   ```

2. Receive an S2C Search Track Results packet:
   ```json
   {
     "opcode": 111,
     "query": "never gonna give you up",
     "results": [
       // Array of search results
     ]
   }
   ```

### Playing a Track

1. Send a C2S Play Track packet with the selected track:
   ```json
   {
     "opcode": 112,
     "track": {
       // MusicAudioTrack object from search results
     }
   }
   ```

2. Receive an S2C Success packet:
   ```json
   {
     "opcode": -2,
     "packetOpcode": 112,
     "message": "Track Title"
   }
   ```

3. Receive an S2C Update Player packet:
   ```json
   {
     "opcode": 200,
     "current": {
       // MusicAudioTrack object
     },
     "volume": 50,
     "paused": false,
     "playing": true,
     "headless": false
   }
   ```

## Error Handling

When an error occurs, the server will respond with an Error Packet (opcode: -1) containing details about the error.

```json
{
  "opcode": -1,
  "packetOpcode": 112,  // The opcode of the original request
  "message": "Track not found"  // Error details
}
```

## Implementation Notes

- The WebSocket server runs on port 3272 by default
- The HTTP server runs on port 80 by default
- All communication is done through JSON-encoded packets
- The server broadcasts player state updates to all connected clients