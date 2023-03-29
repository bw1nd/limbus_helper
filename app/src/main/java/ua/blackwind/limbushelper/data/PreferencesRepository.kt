package ua.blackwind.limbushelper.data

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbus_helper.FilterSettings.FilterDrawerSheetSettings
import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterDrawerSheetState
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSheetSettingsMapper
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val filterSheetSettingsDataStore: DataStore<FilterDrawerSheetSettings>,
    private val partySettingsDataStore: DataStore<PartySettings>,
    private val mapper: FilterSheetSettingsMapper
) {
    fun getFilterSheetSettings(): Flow<FilterDrawerSheetSettings> =
        filterSheetSettingsDataStore.data

    suspend fun updateFilterSheetSettings(state: FilterDrawerSheetState) {
        filterSheetSettingsDataStore.updateData { old ->
            mapper.mapStateToSettings(state, old)
        }
    }

    fun getPartySettings(): Flow<PartySettings> =
        partySettingsDataStore.data

    suspend fun updatePartySettings(showOnlyActive: Boolean) {
        Log.d("DATA_STORE","Updating with $showOnlyActive")
        partySettingsDataStore.updateData {
            it.toBuilder().setShowOnlyActive(showOnlyActive).build()
        }
    }

}