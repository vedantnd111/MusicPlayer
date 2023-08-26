package com.example.musicplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.musicplayer.data.entities.Song

fun MediaMetadataCompat.toSong(): Song? =
    description?.let {
        Song(
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaId!!,
            it.iconUri.toString(),
            it.mediaUri.toString()
        )
    }
