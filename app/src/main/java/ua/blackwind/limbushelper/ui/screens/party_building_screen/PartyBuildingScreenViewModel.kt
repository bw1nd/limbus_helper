package ua.blackwind.limbushelper.ui.screens.party_building_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.data.PreferencesRepository
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.AddIdentityToPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.ChangeSinnerActiveIdentityForParty
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.usecase.GetAllSinners
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.*
import javax.inject.Inject

@HiltViewModel
class PartyBuildingScreenViewModel @Inject constructor(
    private val getPartyUseCase: GetPartyUseCase,
    private val getAllSinners: GetAllSinners,
    private val preferencesRepository: PreferencesRepository,
    private val addIdentityToPartyUseCase: AddIdentityToPartyUseCase,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase,
    private val changeActiveIdentityIdForParty: ChangeSinnerActiveIdentityForParty
): ViewModel() {
    private val rawParty = MutableStateFlow(Party(0, "empty", emptyList()))
    private val _party = MutableStateFlow<List<PartySinnerModel>>(emptyList())
    val party = _party.asStateFlow()

    private val _showOnlyActiveIdentities = MutableStateFlow(false)
    val showOnlyActiveIdentities = _showOnlyActiveIdentities.asStateFlow()

    private val _infoPanelState = MutableStateFlow(
        initialPartyBuildingInfoPanelState()
    )
    val infoPanelState = _infoPanelState.asStateFlow()

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { party ->
                rawParty.update { party }
                updateInfoPanelState(party)
                _party.update {
                    val sinners = getAllSinners()
                    parseIdentityListToSinnerList(
                        party,
                        sinners
                    )
                }
            }
        }
        viewModelScope.launch {
            preferencesRepository.getPartySettings().collectLatest { settings ->
                Log.d("DATA_STORE", "Getting $settings")
                _showOnlyActiveIdentities.update { settings.showOnlyActive }
            }
        }
    }

    private fun updateInfoPanelState(party: Party) {
        val activeList = party.identityList.filter { it.isActive }
        val attackByDamage = intArrayOf(0, 0, 0)
        val attackBySin = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        val defenceByDamage = intArrayOf(0, 0, 0)
        activeList.forEach { pIdentity ->
            val identity = pIdentity.identity
            listOf(identity.slashRes, identity.pierceRes, identity.bluntRes).map { resist ->
                when (resist) {
                    IdentityDamageResistType.NORMAL -> 100
                    IdentityDamageResistType.INEFFECTIVE -> 50
                    IdentityDamageResistType.FATAL -> 200
                }
            }.also {
                it.forEachIndexed { index, value ->
                    defenceByDamage[index] += value
                }
            }

            listOf(
                identity.firstSkill,
                identity.secondSkill,
                identity.thirdSkill
            ).forEach { skill ->
                when (skill.dmgType) {
                    DamageType.SLASH -> attackByDamage[0] += skill.copiesCount
                    DamageType.PIERCE -> attackByDamage[1] += skill.copiesCount
                    DamageType.BLUNT -> attackByDamage[2] += skill.copiesCount
                }
                when (skill.sin) {
                    Sin.WRATH -> attackBySin[0] += skill.copiesCount
                    Sin.LUST -> attackBySin[1] += skill.copiesCount
                    Sin.SLOTH -> attackBySin[2] += skill.copiesCount
                    Sin.GLUTTONY -> attackBySin[3] += skill.copiesCount
                    Sin.GLOOM -> attackBySin[4] += skill.copiesCount
                    Sin.PRIDE -> attackBySin[5] += skill.copiesCount
                    Sin.ENVY -> attackBySin[6] += skill.copiesCount
                }
            }
        }


        _infoPanelState.update {
            val activeIdentityCount = rawParty.value.identityList.count { it.isActive }
            PartyBuildingInfoPanelState(
                AttackByDamageInfo(attackByDamage[0], attackByDamage[1], attackByDamage[2]),
                AttackBySinInfo(
                    attackBySin[0],
                    attackBySin[1],
                    attackBySin[2],
                    attackBySin[3],
                    attackBySin[4],
                    attackBySin[5],
                    attackBySin[6]
                ),
                DefenceByDamageInfo(
                    calculateDamageResistPotency(defenceByDamage[0], activeList.size),
                    calculateDamageResistPotency(defenceByDamage[1], activeList.size),
                    calculateDamageResistPotency(defenceByDamage[2], activeList.size),
                ),
                activeIdentityCount
            )
        }
    }

    fun onShowActiveIdentitiesClick(state: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updatePartySettings(state)
        }
    }

    private fun calculateDamageResistPotency(
        value: Int,
        identityCount: Int
    ): InfoPanelDamageResist {
        if (identityCount == 0) return InfoPanelDamageResist.NA
        return when (value / identityCount) {
            in 0..60 -> InfoPanelDamageResist.Perfect
            in 61 until 90 -> InfoPanelDamageResist.Good
            in 80 until 120 -> InfoPanelDamageResist.Normal
            in 120 until 160 -> InfoPanelDamageResist.Poor
            in 160..200 -> InfoPanelDamageResist.Bad
            else -> throw java.lang.IllegalArgumentException("Pair value: $value/count: $identityCount outside calculation range")
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
        //Placeholder for Detail screen
    }

    fun undoDelete(identity: Identity) {
        viewModelScope.launch { addIdentityToPartyUseCase(identity, rawParty.value) }
    }

    fun onIdentityDeleteButtonClick(identity: Identity) {
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
        }.sortedBy { it.sinner.id }
    }

    private fun initialPartyBuildingInfoPanelState() = PartyBuildingInfoPanelState(
        AttackByDamageInfo(0, 0, 0),
        AttackBySinInfo(0, 0, 0, 0, 0, 0, 0),
        DefenceByDamageInfo(
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal,
            InfoPanelDamageResist.Normal
        ),
        0
    )
}