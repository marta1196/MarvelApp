package com.myebra.marvelapp.di

import android.content.Context
import androidx.room.Room
import com.myebra.marvelapp.data.datasource.cache.MarvelAppDatabase
import com.myebra.marvelapp.data.datasource.features.character.cache.impl.CharacterCacheDataStoreImpl
import com.myebra.marvelapp.data.datastore.features.characters.CharacterDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val MARVEL_DB_NAME = "marvel_db"

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
    Room.databaseBuilder(context, MarvelAppDatabase::class.java,MARVEL_DB_NAME).build()

    @Named("cache_characters")
    @Provides
    fun providesCacheCharactersDataStore(marvelAppDatabase: MarvelAppDatabase) : CharacterDataStore =
        CharacterCacheDataStoreImpl(marvelAppDatabase)
}