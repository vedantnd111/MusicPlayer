# MusicPlayer
MusicPlayer using glide, exo-player and dagger-hilt

# App:
![Screenshot_1693050757](https://github.com/vedantnd111/MusicPlayer/assets/48761376/4f4aef29-5030-428f-b9f0-278efeece1c3)
![Screenshot_1693050748](https://github.com/vedantnd111/MusicPlayer/assets/48761376/b5331d2c-6b49-4ef1-baa5-2f2a94afadd4)
![Screenshot_1693050738](https://github.com/vedantnd111/MusicPlayer/assets/48761376/916e1ac8-33f9-454f-b80a-13bb841092c5)



# Layout:

1. MainActivity
2. HomeFragment
3. SongFragment

- when app launches MainActivity will be shown first. The part above bottom bar in the MainActivity is HomeFragment. HomeFragment is the starting point of the app.
- SongFragment is responsible for displaying song detail page. User can change seekbar position by moving cursor over it.


# Connection between the ui and service:

- `MusicServiceConnection` is the class responsible for connecting ui and service.
- In `MusicServiceConnection` we call `MediaBrowserCompat()` which will Create a media browser for the specified media browse service.
- we call `connect()` along with it which will Connect to the media browse service. Internally, it binds to the service.
- after `connect()` is invoked callbacks provided to `MediaBrowserCompat` will be called .i.e. `MediaBrowserCompatConnectionCallback()`.
- `MediaControllerCompat` is invoked in `MediaBrowserCompatConnectionCallback.onConnected()`.
- `MediaControllerCompat` Allows an app to interact with an ongoing media session. Media buttons and other commands can be sent to the session. A callback may be registered to receive updates from the session, such as metadata and play state changes.
- `MediaControllerCallback` we listen to app interactions such as onPlaybackStateChanged, onMetadataChanged, etc. And update our ui accordingly.

Architecture:

![music_player](https://github.com/vedantnd111/MusicPlayer/assets/48761376/d1550bf7-a4f7-4ea2-8b9a-9ac8488785ea)
