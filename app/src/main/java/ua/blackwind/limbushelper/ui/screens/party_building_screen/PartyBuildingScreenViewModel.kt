package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.ChangeSinnerActiveIdentityForParty
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.usecase.GetAllSinners
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel
import javax.inject.Inject

@HiltViewModel
class PartyBuildingScreenViewModel @Inject constructor(
    private val getPartyUseCase: GetPartyUseCase,
    private val getAllSinners: GetAllSinners,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase,
    private val changeActiveIdentityIdForParty: ChangeSinnerActiveIdentityForParty
): ViewModel() {
    private val rawParty = MutableStateFlow(Party(0, "empty", emptyList()))
    private val _party = MutableStateFlow<List<PartySinnerModel>>(emptyList())
    val party: StateFlow<List<PartySinnerModel>> = _party

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { party ->
                rawParty.update { party }
                _party.update {
                    val sinners = getAllSinners()
                    parseIdentityListToSinnerList(
                        party,
                        sinners
                    )
                }

            }
        }
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

    }

    fun onIdentitySwipe(identity: Identity) {
        viewModelScope.launch { deleteIdentityFromPartyUseCase(identity, rawParty.value) }
    }

    private fun parseIdentityListToSinnerList(
        party: Party,
        sinners: List<Sinner>
    ): List<PartySinnerModel> {
        return party.identityList.groupBy { it.identity.sinnerId }.map { pair ->

            PartySinnerModel(
                sinners.find { it.id == pair.key }!!,
                pair.value
            )
        }
    }
}