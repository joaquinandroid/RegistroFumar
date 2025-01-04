package com.example.registrofumar

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  /*  @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }*/

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): PitillosDatabase {
        return Room.databaseBuilder(
            appContext,
            PitillosDatabase::class.java,
            "pitillos"
        ).build()
    }

    @Provides
    fun providePitilloDao(database: PitillosDatabase): PitillosDao {
        return database.pitillosDao()
    }
}