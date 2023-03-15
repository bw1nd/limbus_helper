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
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetIdentitiesFromParty
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.usecase.GetAllSinners
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyIdentityModel
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class PartyBuildingScreenViewModel @Inject constructor(
    private val getPartyUseCase: GetPartyUseCase,
    private val getAllSinners: GetAllSinners,
    private val getIdentitiesFromParty: GetIdentitiesFromParty,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase
): ViewModel() {
    private val _party = MutableStateFlow(emptyList<PartySinnerModel>())
    val party: StateFlow<List<PartySinnerModel>> = _party

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { party ->
                val sinners = getAllSinners.invoke()
                _party.update {
                    parseIdentityListToSinnerList(
                        getIdentitiesFromParty(party),
                        party,
                        sinners
                    )
                }
            }
        }
    }

    private fun parseIdentityListToSinnerList(
        list: List<Identity>,
        party: Party,
        sinners: List<Sinner>
    ): List<PartySinnerModel> {
        val partyIdentities = list.map { identity ->
            val isActive = false
            PartyIdentityModel(
                identity,
                isActive
                    ?: throw IllegalArgumentException("Identity: $identity not found in party: $party")
            )
        }

        return sinners.map { sinner ->
            PartySinnerModel(
                sinner,
                partyIdentities.filter { identity -> identity.identity.sinnerId == sinner.id }
            )
        }
    }
}