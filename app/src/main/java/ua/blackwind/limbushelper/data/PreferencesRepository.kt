package ua.blackwind.limbushelper.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ua.blackwind.limbus_helper.EgoFilterSettings
import ua.blackwind.limbus_helper.FilterModeSettings
import ua.blackwind.limbus_helper.IdentityFilterSettings
import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import ua.blackwind.limbushelper.data.datastore.EgoFilterSettingsMapper
import ua.blackwind.limbushelper.data.datastore.IdentityFilterSettingsMapper
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterDrawerSheetState
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterMode
import java.io.IOException
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val filterModeDataStore: DataStore<FilterModeSettings.FilterMode>,
    private val identityFilterSettingsDataStore: DataStore<IdentityFilterSettings.IdentitySettings>,
    private val egoFilterSettingsDataStore: DataStore<EgoFilterSettings.EgoSettings>,
    private val partySettingsDataStore: DataStore<PartySettings>,
    private val mapper: IdentityFilterSettingsMapper,
    private val egoMapper: EgoFilterSettingsMapper
) {
    fun getIdentityFilterSheetSettings(): Flow<IdentityFilterSettings.IdentitySettings> {
        return identityFilterSettingsDataStore.data.catch {
            if (it is IOException) {
                emit(IdentityFilterSettings.IdentitySettings.getDefaultInstance())
            } else throw it
        }
    }

    fun getEgoFilterSheetSettings(): Flow<EgoFilterSettings.EgoSettings> {
        return egoFilterSettingsDataStore.data.catch {
            if (it is IOException) {
                emit(EgoFilterSettings.EgoSettings.getDefaultInstance())
            } else throw it
        }
    }

    fun getFilterModeSettings(): Flow<FilterModeSettings.FilterMode> {
        val defaultModeSettings = FilterModeSettings.FilterMode.newBuilder()
            .setMode(FilterMode.Identity.label)
            .build()
        return filterModeDataStore.data.map {
            if (it.mode.isBlank()) {
                defaultModeSettings
            } else {
                it
            }
        }.catch {
            if (it is IOException) {
                emit(
                    defaultModeSettings
                )
            } else throw it
        }
    }

    suspend fun updateIdentityFilterSheetSettings(state: FilterDrawerSheetState.IdentityMode) {
        identityFilterSettingsDataStore.updateData { old ->
            mapper.mapStateToSettings(state, old)
        }
    }

    suspend fun updateEgoFilterSheetSettings(state: FilterDrawerSheetState.EgoMode) {
        egoFilterSettingsDataStore.updateData { old ->
            egoMapper.mapStateToSettings(state, old)
        }
    }

    fun getPartySettings(): Flow<PartySettings> =
        partySettingsDataStore.data

    suspend fun updatePartySettings(showOnlyActive: Boolean) {
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


