package ua.blackwind.limbushelper.data

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbus_helper.EgoFilterSettings
import ua.blackwind.limbus_helper.FilterModeSettings
import ua.blackwind.limbus_helper.IdentityFilterSettings

import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import ua.blackwind.limbushelper.data.datastore.EgoFilterSettingsMapper
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterDrawerSheetState
import ua.blackwind.limbushelper.data.datastore.IdentityFilterSettingsMapper
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterMode
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val filterModeDataStore: DataStore<FilterModeSettings.FilterMode>,
    private val identityFilterSettingsDataStore: DataStore<IdentityFilterSettings.IdentitySettings>,
    private val egoFilterSettingsDataStore: DataStore<EgoFilterSettings.EgoSettings>,
    private val partySettingsDataStore: DataStore<PartySettings>,
    private val mapper: IdentityFilterSettingsMapper,
    private val egoMapper: EgoFilterSettingsMapper
) {
    fun getIdentityFilterSheetSettings(): Flow<IdentityFilterSettings.IdentitySettings> =
        identityFilterSettingsDataStore.data

    fun getEgoFilterSheetSettings(): Flow<EgoFilterSettings.EgoSettings> =
        egoFilterSettingsDataStore.data

    fun getFilterModeSettings(): Flow<FilterModeSettings.FilterMode> =
        filterModeDataStore.data

    suspend fun updateIdentityFilterSheetSettings(state: FilterDrawerSheetState.IdentityMode) {
        identityFilterSettingsDataStore.updateData { old ->
            mapper.mapStateToSettings(state, old)
        }
    }

    suspend fun updateEgoFilterSheetSettings(state: FilterDrawerSheetState.EgoMode){
        egoFilterSettingsDataStore.updateData { old ->
            egoMapper.mapStateToSettings(state, old)
        }
    }

    fun getPartySettings(): Flow<PartySettings> =
        partySettingsDataStore.data

    suspend fun updatePartySettings(showOnlyActive: Boolean) {
        Log.d("DATA_STORE", "Updating with $showOnlyActive")
        partySettingsDataStore.updateData {
            it.toBuilder().setShowOnlyActive(showOnlyActive).build()
        }
    }

    suspend fun updateFilterModeSettings(state: FilterMode) {
        filterModeDataStore.updateData {
            it.toBuilder().setMode(
                state.label
            ).build()
        }
    }
}