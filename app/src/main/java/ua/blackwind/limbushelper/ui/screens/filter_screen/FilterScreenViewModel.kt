package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect

import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.filter.*
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.AddIdentityToPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterIdentityModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase,
    private val addIdentityToPartyUseCase: AddIdentityToPartyUseCase,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase,
    private val getPartyUseCase: GetPartyUseCase
): ViewModel() {

    private val party = MutableStateFlow(Party(0, "Default", emptyList()))

    private val _filteredIdentities = MutableStateFlow<List<FilterIdentityModel>>(emptyList())
    val filteredIdentities: StateFlow<List<FilterIdentityModel>> = _filteredIdentities

    private val _filterDrawerShitState = MutableStateFlow(
        FilterDrawerSheetState(
            FilterSheetMode.Type,
            emptyFilterSkillBlockState(),
            emptyFilterResistStateBundle(),
            emptyFilterEffectBlockState()
        )
    )
    val filterDrawerShitState = _filterDrawerShitState.asStateFlow()

    private val _sinPickerVisible = MutableStateFlow(false)
    val sinPickerVisible = _sinPickerVisible.asStateFlow()

    private var selectedFilterSheetButtonPosition: SelectedButtonPosition =
        SelectedButtonPosition.None

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { newParty ->
                party.update { newParty }
                if (_filteredIdentities.value.isNotEmpty() && party.value.id != 0) {
                    _filteredIdentities.update { list ->
                        identityListToFilterIdentityList(
                            list.map { it.identity },
                            newParty
                        )
                    }
                }
            }
        }
    }

    fun onFilterButtonClick() {
        //TODO add dispatchers injection
        viewModelScope.launch(Dispatchers.Default) {
            _filteredIdentities.update {
                val skillState = _filterDrawerShitState.value.skillState
                val resistState = _filterDrawerShitState.value.resistState
                val effectState = _filterDrawerShitState.value.effectsState
                identityListToFilterIdentityList(
                    getFilteredIdentitiesUseCase(
                        formIdentityFilter(resistState, skillState, effectState)
                    ), party.value
                )
            }
        }
    }

    fun onIdentityItemInPartyChecked(identity: Identity) {
        viewModelScope.launch {
            addIdentityToPartyUseCase(
                identity,
                party.value
            )
        }
    }

    fun onIdentityItemInPartyUnChecked(identity: Identity) {
        viewModelScope.launch {
            deleteIdentityFromPartyUseCase(identity, party.value)
        }
    }

    fun onFilterModeSwitch(id: Int) {
        val newMode = when (id) {
            0 -> FilterSheetMode.Type
            1 -> FilterSheetMode.Effects
            else -> throw IllegalArgumentException("Wrong switch button id: $id")
        }
        updateFilterDrawerSheetState(
            _filterDrawerShitState.value.copy(
                filterSheetMode = newMode
            )
        )
    }

    fun onClearFilterButtonPress() {
        _filterDrawerShitState.update { old ->
            old.copy(
                skillState = emptyFilterSkillBlockState(),
                resistState = emptyFilterResistStateBundle(),
                effectsState = emptyFilterEffectBlockState()
            )
        }
    }

    fun onEffectCheckedChange(checked: Boolean, effect: Effect) {
        val new = filterDrawerShitState.value.effectsState.effects.toMutableMap()
        new[effect] = checked
        val newState = FilterEffectBlockState(
            new
        )
        updateFilterDrawerSheetState(
            _filterDrawerShitState.value.copy(
                effectsState = newState
            )
        )

    }

    fun onFilterSkillButtonLongPress(selected: SelectedButtonPosition) {
        selectedFilterSheetButtonPosition = selected
        _sinPickerVisible.update { true }
    }

    fun onFilterSinPickerPress(sin: StateType<Sin>) {
        _sinPickerVisible.update { false }
        val oldSinState = when (selectedFilterSheetButtonPosition) {
            SelectedButtonPosition.None ->
                throw IllegalStateException("Trying to update filter buttons state with none selected")
            SelectedButtonPosition.First -> _filterDrawerShitState.value.skillState
            SelectedButtonPosition.Second -> _filterDrawerShitState.value.skillState
            SelectedButtonPosition.Third -> _filterDrawerShitState.value.skillState
        }
        updateFilterDrawerSheetState(
            _filterDrawerShitState.value.copy(
                skillState = FilterSkillBlockState(
                    oldSinState.damage,
                    updateSinStateBundle(selectedFilterSheetButtonPosition, sin, oldSinState.sin)
                )
            )
        )

        selectedFilterSheetButtonPosition = SelectedButtonPosition.None
    }

    fun onFilterSkillButtonClick(button: SelectedButtonPosition) {
        _filterDrawerShitState.update { old ->
            old.copy(
                skillState = FilterSkillBlockState(
                    updateDamageStateBundle(button, old.skillState.damage, false),
                    old.skillState.sin
                )
            )
        }
    }

    fun onFilterResistButtonClick(button: SelectedButtonPosition) {
        val oldState = _filterDrawerShitState.value.resistState
        updateFilterDrawerSheetState(
            _filterDrawerShitState.value.copy(
                resistState = updateDamageStateBundle(button, oldState, true)
            )
        )
    }

    private fun updateFilterDrawerSheetState(newState: FilterDrawerSheetState) {
        _filterDrawerShitState.update { newState }
    }

    private fun identityListToFilterIdentityList(
        list: List<Identity>,
        party: Party
    ): List<FilterIdentityModel> {
        return list.map { identity ->
            FilterIdentityModel(
                identity,
                party.identityList.any { it.identity.id == identity.id }
            )
        }
    }

    /**
     * Updates damage bundle with new value
     * @param unique this parameter decides if returning result must have unique damage types
     * (for resistance block) or non unique (for skills block)
     */
    private fun updateDamageStateBundle(
        button: SelectedButtonPosition,
        input: FilterDamageStateBundle,
        unique: Boolean
    ): FilterDamageStateBundle {
        var (first, second, third) = input
        when (button) {
            SelectedButtonPosition.First -> first = cycleSkillDamageTypes(first)
            SelectedButtonPosition.Second -> second = cycleSkillDamageTypes(second)
            SelectedButtonPosition.Third -> third = cycleSkillDamageTypes(third)
            SelectedButtonPosition.None ->
                throw IllegalStateException(UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED)
        }
        val result = FilterDamageStateBundle(first, second, third)
        return if (unique && !result.isUnique()) {
            updateDamageStateBundle(button, FilterDamageStateBundle(first, second, third), unique)
        } else result
    }

    private fun updateSinStateBundle(
        button: SelectedButtonPosition,
        sin: StateType<Sin>,
        input: FilterSinStateBundle
    ): FilterSinStateBundle {
        var (first, second, third) = input
        when (button) {
            SelectedButtonPosition.First -> first = sin
            SelectedButtonPosition.Second -> second = sin
            SelectedButtonPosition.Third -> third = sin
            SelectedButtonPosition.None ->
                throw IllegalStateException(UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED)
        }
        return FilterSinStateBundle(first, second, third)
    }

    private fun cycleSkillDamageTypes(type: StateType<DamageType>): StateType<DamageType> {
        if (type is StateType.Empty) return StateType.Value(DamageType.SLASH)
        val value = type as StateType.Value<DamageType>
        return when (value.value) {
            DamageType.SLASH -> StateType.Value(DamageType.PIERCE)
            DamageType.PIERCE -> StateType.Value(DamageType.BLUNT)
            DamageType.BLUNT -> StateType.Empty
        }
    }

    private fun formIdentityFilter(
        resistState: FilterDamageStateBundle,
        skillState: FilterSkillBlockState,
        effectState: FilterEffectBlockState
    ) = IdentityFilter(
        resist = FilterResistSetArg(
            ineffective = resistState.first.toFilterDamageTypeArg(),
            normal = resistState.second.toFilterDamageTypeArg(),
            fatal = resistState.third.toFilterDamageTypeArg()
        ),
        skills = FilterSkillsSetArg(
            FilterSkillArg(
                skillState.damage.first.toFilterDamageTypeArg(),
                skillState.sin.first.toFilterSinTypeArg()
            ),
            FilterSkillArg(
                skillState.damage.second.toFilterDamageTypeArg(),
                skillState.sin.second.toFilterSinTypeArg()
            ),
            FilterSkillArg(
                skillState.damage.third.toFilterDamageTypeArg(),
                skillState.sin.third.toFilterSinTypeArg()
            ),
        ),
        effects = effectState.effects.filter { it.value }.keys.toList()
    )

    private fun emptyFilterSkillBlockState() = FilterSkillBlockState(
        FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty),
        FilterSinStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)
    )

    private fun emptyFilterResistStateBundle() =
        FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)

    private fun emptyFilterEffectBlockState() = FilterEffectBlockState(
        Effect.values().associateWith { false }
    )

    companion object {
        private const val UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED =
            "Trying to update filter buttons state with none selected"
    }
}