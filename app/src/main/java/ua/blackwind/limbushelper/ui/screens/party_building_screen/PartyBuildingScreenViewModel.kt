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
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
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
    private val clearPartyByIdUseCase: ClearPartyByIdUseCase
): ViewModel() {

    private val _party = MutableStateFlow<Party>(Party(DEFAULT_PARTY_ID, "Default", emptyList()))
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
                updateInfoPanelState(party)
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

    private fun updateInfoPanelState(party: Party) {
        //TODO HERESY!! THIS PLACE MUST BE PURGED!!
        val activeList = party.sinners.map { it.identities }.flatten().filter { it.isActive }
        val egoList =
            party.sinners.map { it.ego }.flatten()
                .filter { ego -> activeList.any { it.identity.sinnerId == ego.sinnerId } }
        val attackByDamage = intArrayOf(0, 0, 0)
        val attackBySin = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        val defenceByDamage = intArrayOf(0, 0, 0)
        val resistBySin = mutableMapOf(
            Sin.WRATH to 0, Sin.LUST to 0, Sin.SLOTH to 0, Sin.GLUTTONY to 0,
            Sin.GLOOM to 0, Sin.PRIDE to 0, Sin.ENVY to 0
        )
        activeList.forEach { pIdentity ->
            val identity = pIdentity.identity
            listOf(identity.slashRes, identity.pierceRes, identity.bluntRes).map { resist ->
                when (resist) {
                    IdentityDamageResistType.NORMAL -> 100
                    IdentityDamageResistType.INEFF -> 50
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

        val minEgo =
            egoList.groupBy { it.sinnerId }.map { list -> list.value.minBy { it.risk.ordinal } }
        minEgo.forEach { ego ->
            Sin.values().forEach { sin ->
                when (ego.sinResistances[sin]) {
                    EgoSinResistType.NORMAL -> resistBySin[sin] = resistBySin[sin]!! + 100
                    EgoSinResistType.INEFF -> resistBySin[sin] = resistBySin[sin]!! + 50
                    EgoSinResistType.ENDURE -> resistBySin[sin] = resistBySin[sin]!! + 75
                    EgoSinResistType.FATAL -> resistBySin[sin] = resistBySin[sin]!! + 200
                    null -> resistBySin[sin] = resistBySin[sin]!! + 100
                }
            }
        }

        _infoPanelState.update {
            val identities = _party.value.sinners.map { it.identities }.flatten()
            val activeIdentityCount = identities.count { it.isActive }
            val totalIdentityCount = identities.size
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
                ResistBySinInfo(
                    calculateDamageResistPotency(resistBySin[Sin.WRATH]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.LUST]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.SLOTH]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.GLUTTONY]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.GLOOM]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.PRIDE]!!, activeList.size),
                    calculateDamageResistPotency(resistBySin[Sin.ENVY]!!, activeList.size),
                ),
                activeIdentityCount,
                totalIdentityCount
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