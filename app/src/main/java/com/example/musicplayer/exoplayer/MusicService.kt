package com.example.musicplayer.exoplayer

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.musicplayer.exoplayer.callback.MusicPlaybackPreparer
import com.example.musicplayer.exoplayer.callback.MusicPlayerEventListener
import com.example.musicplayer.exoplayer.callback.MusicPlayerNotificationListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var defaultDataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private val serviceJob = Job()

    // Dispatchers.Main + serviceJob means we are going to merge scope of Main and serviceJob
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    // contains information regarding session
    private lateinit var mediaSession: MediaSessionCompat

    // this will be used to connent to the mediaSession
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var musicNotificationManager: MusicNotificationManager
    var isForegroundService = false
    private var currPlayingSong: MediaMetadataCompat? = null

    override fun onCreate() {
        super.onCreate()
        // when we click on notification it should open the app with song name, below code will do that
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken
        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {
            musicNotificationManager.showNotification(exoPlayer)
        }

        val musicPlaybackPreparer = MusicPlaybackPreparer(firebaseMusicSource) {
            currPlayingSong = it
            preparePlayer(firebaseMusicSource.songs, it, true)
        }
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setPlayer(exoPlayer)

        exoPlayer.addListener(MusicPlayerEventListener(this))

    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val currSong = if (currPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        exoPlayer.prepare(firebaseMusicSource.asMediaSource(dataSourceFactory = defaultDataSourceFactory))
        exoPlayer.seekTo(currSong, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}
