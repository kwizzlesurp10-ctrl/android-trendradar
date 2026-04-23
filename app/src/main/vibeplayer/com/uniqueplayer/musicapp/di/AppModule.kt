package com.uniqueplayer.musicapp.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.uniqueplayer.musicapp.data.local.VibePlayerDatabase
import com.uniqueplayer.musicapp.data.local.dao.MusicDao
import com.uniqueplayer.musicapp.data.playback.ExoPlayerController
import com.uniqueplayer.musicapp.data.repository.OfflineMusicRepository
import com.uniqueplayer.musicapp.domain.playback.PlayerController
import com.uniqueplayer.musicapp.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {
    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        repository: OfflineMusicRepository
    ): MusicRepository

    @Binds
    @Singleton
    abstract fun bindPlayerController(
        controller: ExoPlayerController
    ): PlayerController
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VibePlayerDatabase = Room.databaseBuilder(
        context,
        VibePlayerDatabase::class.java,
        "vibeplayer.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideMusicDao(
        database: VibePlayerDatabase
    ): MusicDao = database.musicDao()

}
