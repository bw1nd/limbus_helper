package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.data.PreferencesRepository
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.*
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.*
import javax.inject.Inject

@HiltViewModel
class PartyBuildingScreenViewModel @Inject constructor(
    private val getPartyUseCase: GetPartyUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val addIdentityToPartyUseCase: AddIdentityToPartyUseCase,
    private val removeIdentityFromPartyUseCase: RemoveIdentityFromPartyUseCase,
    private val changeActiveIdentityIdForParty: ChangeSinnerActiveIdentityForParty,
    private val removeEgoFromPartyUseCase: RemoveEgoFromPartyUseCase,
    private val clearPartyByIdUseCase: ClearPartyByIdUseCase,
    getPartyInfoUseCase: GetPartyInfoUseCase
): ViewModel() {

    private val _party = MutableStateFlow(Party(DEFAULT_PARTY_ID, "Default", emptyList()))
    val party = _party.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _showOnlyActiveIdentities = MutableStateFlow(false)
    val showOnlyActiveIdentities = _showOnlyActiveIdentities.asStateFlow()

    private val _infoPanelState = MutableStateFlow(
        initialPartyBuildingInfoPanelState()
    )
    val infoPanelState = _infoPanelState.asStateFlow()

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { party ->
                _party.update { party }
                _infoPanelState.update {
                    PartyBuildingInfoPanelState.fromInfoData(
                        getPartyInfoUseCase(party)
                    )
                }
            }
        }
        viewModelScope.launch {
            preferencesRepository.getPartySettings().collectLatest { settings ->
                _showOnlyActiveIdentities.update { settings.showOnlyActive }
            }
        }
    }

    fun onAlertDialogDismiss() {
        _showDialog.update { false }
    }

    fun onClearPartyClick() {
        _showDialog.update { true }
    }

    fun onCleaPartyAcceptClick() {
        _showDialog.update { false }
        viewModelScope.launch {
            clearPartyByIdUseCase(_party.value.id)
        }
    }

    fun onShowActiveIdentitiesClick(state: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updatePartySettings(state)
        }
    }

    fun onEgoDeleteButtonClick(ego: Ego) {
        viewModelScope.launch { removeEgoFromPartyUseCase(party = _party.value, ego = ego) }
    }

    fun onIdentityLongPress(identityId: Int, sinnerId: Int) {
        viewModelScope.launch {
            changeActiveIdentityIdForParty(
                DEFAULT_PARTY_ID,
                sinnerId,
                identityId
            )
        }
    }

    fun onIdentityClick(identityId: Int) {
        //Placeholder for Detail screen
    }

    fun undoDelete(identity: Identity) {
        viewModelScope.launch { addIdentityToPartyUseCase(identity, _party.value) }
    }

    fun onIdentityDeleteButtonClick(identity: Identity) {
        viewModelScope.launch { removeIdentityFromPartyUseCase(identity, _party.value) }
    }

    private fun initialPartyBuildingInfoPanelState() = PartyBuildingInfoPanelState(
        AttackByDamageInfo(0, 0, 0),
        AttackBySinInfo(0, 0, 0, 0, 0, 0, 0),
        DefenceByDamageInfo(
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal
        ),
        ResistBySinInfo(
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
        ),
        0, 0
    )
}