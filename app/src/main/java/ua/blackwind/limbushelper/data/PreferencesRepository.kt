package ua.blackwind.limbushelper.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbus_helper.FilterSettings.FilterDrawerSheetSettings
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterDrawerSheetState
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSheetSettingsMapper
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val filterSheetSettingsDataStore: DataStore<FilterDrawerSheetSettings>,
    private val mapper: FilterSheetSettingsMapper
) {
    fun getFilterSheetSettings(): Flow<FilterDrawerSheetSettings> =
        filterSheetSettingsDataStore.data

    suspend fun updateSettings(state: FilterDrawerSheetState) {
        filterSheetSettingsDataStore.updateData { old ->
            mapper.mapStateToSettings(state, old)
        }
    }
}