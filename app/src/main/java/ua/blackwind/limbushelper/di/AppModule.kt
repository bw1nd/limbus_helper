package ua.blackwind.limbushelper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.blackwind.limbus_helper.EgoFilterSettings
import ua.blackwind.limbus_helper.FilterModeSettings
import ua.blackwind.limbus_helper.IdentityFilterSettings
import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import ua.blackwind.limbushelper.data.datastore.EgoFilterSettingsSerializer
import ua.blackwind.limbushelper.data.datastore.FilterModeSettingsSerializer
import ua.blackwind.limbushelper.data.datastore.IdentityFilterSettingsSerializer
import ua.blackwind.limbushelper.ui.screens.party_building_screen.state.PartySettingsSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.identityFilterDataStore: DataStore<IdentityFilterSettings.IdentitySettings> by dataStore(
        fileName = "identity_filter_settings.pb",
        serializer = IdentityFilterSettingsSerializer()
    )

    private val Context.egoFilterDataStore: DataStore<EgoFilterSettings.EgoSettings> by dataStore(
        fileName = "ego_filter_settings.pb",
        serializer = EgoFilterSettingsSerializer()
    )

    private val Context.partyScreenDataStore: DataStore<PartySettings> by dataStore(
        fileName = "party_settings.pb",
        serializer = PartySettingsSerializer()
    )

    private val Context.filterModeDataStore: DataStore<FilterModeSettings.FilterMode> by dataStore(
        fileName = "filter_mode_settings.pb",
        serializer = FilterModeSettingsSerializer()
    )

    @Provides
    @Singleton
    fun provideIdentityFilterSettingsDataStore(@ApplicationContext context: Context): DataStore<IdentityFilterSettings.IdentitySettings> =
        context.identityFilterDataStore

    @Provides
    @Singleton
    fun provideEgoFilterSettingsDataStore(@ApplicationContext context: Context): DataStore<EgoFilterSettings.EgoSettings> =
        context.egoFilterDataStore

    @Provides
    @Singleton
    fun providePartySettingsDataStore(@ApplicationContext context: Context): DataStore<PartySettings> =
        context.partyScreenDataStore

    @Provides
    @Singleton
    fun provideFilterModeSettingsDataStore(@ApplicationContext context: Context): DataStore<FilterModeSettings.FilterMode> =
        context.filterModeDataStore
}