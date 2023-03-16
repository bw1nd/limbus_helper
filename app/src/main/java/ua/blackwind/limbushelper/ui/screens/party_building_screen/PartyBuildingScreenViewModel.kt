package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.ChangeSinnerActiveIdentityForParty
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetSinnerActiveIdentityIdForParty
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.usecase.GetAllSinners
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyIdentityModel
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel
import javax.inject.Inject

@HiltViewModel
class PartyBuildingScreenViewModel @Inject constructor(
    private val getPartyUseCase: GetPartyUseCase,
    private val getAllSinners: GetAllSinners,
    private val getSinnerActiveIdentityIdForParty: GetSinnerActiveIdentityIdForParty,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase,
    private val changeActiveIdentityIdForParty: ChangeSinnerActiveIdentityForParty
): ViewModel() {
    private val _party = MutableStateFlow(emptyList<PartySinnerModel>())
    val party: StateFlow<List<PartySinnerModel>> = _party

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { party ->
                val sinners = getAllSinners()
                _party.update {
                    parseIdentityListToSinnerList(
                        party,
                        sinners
                    )
                }
            }
        }
    }

    //TODO implement this. think about this screen data models
    fun onIdentityPortraitClick(identityId: Int, sinnerId: Int, partyId: Int) {
        viewModelScope.launch { changeActiveIdentityIdForParty(partyId, sinnerId, identityId) }
    }

    private suspend fun parseIdentityListToSinnerList(
        party: Party,
        sinners: List<Sinner>
    ): List<PartySinnerModel> {
        return party.identityList.groupBy { it.sinnerId }.map { pair ->
            val activeId = getSinnerActiveIdentityIdForParty(pair.key, party.id)
            PartySinnerModel(
                sinners.find { it.id == pair.key }!!,
                pair.value.map { identity ->
                    PartyIdentityModel(
                        identity,
                        identity.id == activeId
                    )
                })
        }
    }
}