package com.example.musicplayer.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.other.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.exoplayer.currPlaybackPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * why this view model is required?
 * we have setup a coroutine in this view model which is bound with the view-model to update currPlayerPosition,
 * if we move this to MainViewModel then coroutine will keep running even if its not needed. Hence this class is required.
 */

@HiltViewModel
class SongViewModel @Inject constructor(musicServiceConnection: MusicServiceConnection) :
    ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private val _currPlayingSongDuration = MutableLiveData<Long>()
    val currPlayingSongDuration = _currPlayingSongDuration

    private val _currPlayerPosition = MutableLiveData<Long>()
    val currPlayerPosition = _currPlayerPosition

    init {
        updateCurrentPlayerPosition()
    }

    private fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val pos = playbackState.value?.currPlaybackPosition
                if (currPlayerPosition.value != pos) {
                    _currPlayerPosition.postValue(pos!!)
                    _currPlayingSongDuration.postValue(MusicService.currSongDuration)
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }

}
