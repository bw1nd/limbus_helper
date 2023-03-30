package ua.blackwind.limbushelper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.blackwind.limbus_helper.FilterSettings.FilterDrawerSheetSettings
import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import ua.blackwind.limbushelper.data.db.AppDatabase
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSettingsSerializer
import ua.blackwind.limbushelper.ui.screens.party_building_screen.state.PartySettingsSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.filterSheetDataStore: DataStore<FilterDrawerSheetSettings> by dataStore(
        fileName = "filter_settings.pb",
        serializer = FilterSettingsSerializer()
    )

    private val Context.partyScreenDataStore: DataStore<PartySettings> by dataStore(
        fileName = "party_settings.pb",
        serializer = PartySettingsSerializer()
    )

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .createFromAsset("app.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideFilterSettingsDataStore(@ApplicationContext context: Context): DataStore<FilterDrawerSheetSettings> {
        return context.filterSheetDataStore
    }

    @Provides
    @Singleton
    fun providePartySettingsDataStore(@ApplicationContext context: Context): DataStore<PartySettings> {
        return context.partyScreenDataStore
    }

}