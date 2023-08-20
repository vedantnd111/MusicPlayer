package com.example.musicplayer.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.R
import com.example.musicplayer.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @InstallIn(ApplicationComponent::class) - tells that lifespan of this module will be same as the
 * lifespan of application
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMusicServiceConnection(@ApplicationContext context: Context) =
        MusicServiceConnection(context)

    /**
     * We need to mark this method with @singleton so that we don't end up creating many instances of glide.
     * due to this code we'll have single instance of glide throughout application lifespan.
     */
    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_image).error(R.drawable.ic_image)
                .diskCacheStrategy(
                    DiskCacheStrategy.DATA
                )
        )
}