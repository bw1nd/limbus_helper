package ua.blackwind.limbushelper.ui.screens.filter_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.data.PreferencesRepository
import ua.blackwind.limbushelper.data.datastore.EgoFilterSettingsMapper
import ua.blackwind.limbushelper.data.datastore.IdentityFilterSettingsMapper
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.filter.*
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterDataModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterItemTypeModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.StateType
import ua.blackwind.limbushelper.ui.util.toFilterDamageTypeArg
import ua.blackwind.limbushelper.ui.util.toFilterSinTypeArg
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase,
    private val getFilteredEgoUseCase: GetFilteredEgoUseCase,
    private val addIdentityToPartyUseCase: AddIdentityToPartyUseCase,
    private val removeIdentityFromPartyUseCase: RemoveIdentityFromPartyUseCase,
    private val addEgoToPartyUseCase: AddEgoToPartyUseCase,
    private val removeEgoFromPartyUseCase: RemoveEgoFromPartyUseCase,
    private val getPartyUseCase: GetPartyUseCase,
    private val filterSheetSettingsMapper: IdentityFilterSettingsMapper,
    private val filterEgoSettingsMapper: EgoFilterSettingsMapper
): ViewModel() {
    //TODO Everything inside of this class is a huge mess
    private val party = MutableStateFlow(Party(0, "Default", emptyList(), emptyList()))

    private val _filteredItems = MutableStateFlow<List<FilterDataModel>>(emptyList())
    val filteredItems: StateFlow<List<FilterDataModel>> = _filteredItems

    private val _filterMode: MutableStateFlow<FilterMode> = MutableStateFlow(FilterMode.Ego)
    val filterMode = _filterMode.asStateFlow()

    private val _filterDrawerShitState: MutableStateFlow<FilterDrawerSheetState> = MutableStateFlow(
        FilterDrawerSheetState.IdentityMode.getDefaultState()
    )
    val filterDrawerShitState = _filterDrawerShitState.asStateFlow()

    private val _filterDrawerSheetTab = MutableStateFlow<FilterSheetTab>(FilterSheetTab.Type)
    val filterDrawerSheetTab = _filterDrawerSheetTab.asStateFlow()

    private val _sinPickerState = MutableStateFlow<SinPickerState>(SinPickerState.Gone)
    val sinPickerState = _sinPickerState.asStateFlow()

    private var selectedSheetButtonPosition: FilterSheetButtonPosition =
        FilterSheetButtonPosition.None

    init {
        viewModelScope.launch {
            getPartyUseCase().collectLatest { newParty ->
                party.update { newParty }
                if (_filteredItems.value.isNotEmpty() && party.value.id != 0) {
                    _filteredItems.update { list ->
                        itemListToFilterItemList(
                            list.map { it.item },
                            newParty
                        )
                    }
                }
            }
        }
        val filterModeFlow = preferencesRepository.getFilterModeSettings()
        val filterSheetIdentitySettingsFlow =
            preferencesRepository.getIdentityFilterSheetSettings()
        val filterSheetEgoSettingsFlow =
            preferencesRepository.getEgoFilterSheetSettings()

        viewModelScope.launch {
            filterSheetIdentitySettingsFlow.combine(filterSheetEgoSettingsFlow) { identity, ego ->
                identity to ego
            }.combine(
                filterModeFlow
            ) { (identity, ego), mode ->
                when (mode.mode) {
                    FilterMode.Identity.label -> {
                        _filterMode.update { FilterMode.Identity }
                        filterSheetSettingsMapper.mapFilterSheetDataStoreSettingsToState(identity)
                    }
                    FilterMode.Ego.label -> {
                        _filterMode.update { FilterMode.Ego }
                        filterEgoSettingsMapper.mapFilterSheetDataStoreSettingsToState(ego)
                    }
                    else -> {
                        _filterMode.update { FilterMode.Identity }
                        FilterDrawerSheetState.IdentityMode.getDefaultState()
                    }
                }
            }.collectLatest { newState ->
                Log.d("FILTER", "Updating state")
                _filterDrawerShitState.update { newState }
                onFilterButtonClick()
            }
        }
    }

    fun onFilterButtonClick() {
        //TODO add dispatchers injection
        viewModelScope.launch(Dispatchers.Default) {
            _filteredItems.update {
                when (filterDrawerShitState.value) {
                    is FilterDrawerSheetState.IdentityMode -> {
                        val current =
                            _filterDrawerShitState.value as FilterDrawerSheetState.IdentityMode
                        val skillState = current.skillState
                        val resistState = current.resistState
                        val effectState = current.effectsState
                        val sinnerState = current.sinnersState
                        itemListToFilterItemList(
                            getFilteredIdentitiesUseCase(
                                formIdentityFilter(
                                    resistState,
                                    skillState,
                                    effectState,
                                    sinnerState
                                )
                            ).map { FilterItemTypeModel.IdentityType(it) }, party.value
                        )
                    }
                    is FilterDrawerSheetState.EgoMode -> {
                        val current = _filterDrawerShitState.value as FilterDrawerSheetState.EgoMode
                        itemListToFilterItemList(
                            getFilteredEgoUseCase(
                                formEgoFilter(state = current)
                            ).map { FilterItemTypeModel.EgoType(it) }, party.value
                        )
                    }
                }
            }
        }
    }

    fun onItemInPartyChecked(item: FilterDataModel) {
        viewModelScope.launch {
            when (item.item) {
                is FilterItemTypeModel.EgoType -> addEgoToPartyUseCase(
                    item.item.ego,
                    party.value
                )
                is FilterItemTypeModel.IdentityType -> addIdentityToPartyUseCase(
                    item.item.identity,
                    party.value
                )
            }
        }
    }

    fun onItemInPartyUnChecked(item: FilterDataModel) {
        viewModelScope.launch {
            when (item.item) {
                is FilterItemTypeModel.EgoType -> removeEgoFromPartyUseCase(
                    item.item.ego,
                    party.value
                )
                is FilterItemTypeModel.IdentityType -> removeIdentityFromPartyUseCase(
                    item.item.identity,
                    party.value
                )
            }

        }
    }

    fun onFilterTabSwitch(id: Int) {
        val newTab = when (id) {
            0 -> FilterSheetTab.Type
            1 -> FilterSheetTab.Effects
            2 -> FilterSheetTab.Sinners
            else -> throw IllegalArgumentException("Wrong switch button id: $id")
        }
        _filterDrawerSheetTab.update { newTab }
    }

    fun onFilterModeSwitch(id: Int) {
        val newMode = when (id) {
            0 -> FilterMode.Identity
            1 -> FilterMode.Ego
            else -> throw IllegalArgumentException("Wrong switch button id: $id")
        }
        viewModelScope.launch { preferencesRepository.updateFilterModeSettings(newMode) }
    }

    fun onClearFilterButtonPress() {
        when (_filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> updateFilterDrawerSheetState(
                FilterDrawerSheetState.EgoMode.getDefaultState()
            )
            is FilterDrawerSheetState.IdentityMode -> updateFilterDrawerSheetState(
                FilterDrawerSheetState.IdentityMode.getDefaultState()
            )
        }
        updateFilterDrawerSheetState(FilterDrawerSheetState.IdentityMode.getDefaultState())
        _sinPickerState.update { SinPickerState.Gone }
    }

    fun onEffectCheckedChange(checked: Boolean, effect: Effect) {
        val oldList = when (val value = _filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> value.effectsState.effects
            is FilterDrawerSheetState.IdentityMode -> value.effectsState.effects
        }

        val new = oldList.toMutableMap()
        new[effect] = !checked
        val newState = FilterEffectBlockState(new)
        updateFilterDrawerSheetState(
            when (val value = _filterDrawerShitState.value) {
                is FilterDrawerSheetState.EgoMode -> value.copy(effectsState = newState)
                is FilterDrawerSheetState.IdentityMode -> value.copy(effectsState = newState)
            }
        )
    }

    fun onSinnerCheckedChange(sinner: FilterSinnerModel) {
        val oldList = when (val value = _filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> value.sinnersState.sinners
            is FilterDrawerSheetState.IdentityMode -> value.sinnersState.sinners
        }

        val new = oldList.toMutableMap()
        new[sinner] = new[sinner]?.not() ?: false
        val newState = FilterSinnersBlockState(new)
        updateFilterDrawerSheetState(
            when (val value = _filterDrawerShitState.value) {
                is FilterDrawerSheetState.EgoMode -> value.copy(sinnersState = newState)
                is FilterDrawerSheetState.IdentityMode -> value.copy(sinnersState = newState)
            }
        )
    }

    fun onFilterSkillButtonLongPress(selected: FilterSheetButtonPosition) {
        selectedSheetButtonPosition = selected
        _sinPickerState.update { SinPickerState.SkillSelected }
    }

    fun onEgoResistButtonLongPress(selected: FilterSheetButtonPosition) {
        selectedSheetButtonPosition = selected
        _sinPickerState.update { SinPickerState.EgoResistSelected }
    }

    fun onFilterSinPickerPress(sin: StateType<Sin>) {
        when (val value = _filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> {
                if (_sinPickerState.value is SinPickerState.SkillSelected) {
                    updateFilterDrawerSheetState(
                        value.copy(
                            skillState = EgoFilterSkillBlockState(
                                value.skillState.damageType,
                                sin
                            )
                        )
                    )
                }
                if (_sinPickerState.value is SinPickerState.EgoResistSelected) {
                    updateFilterDrawerSheetState(
                        value.copy(
                            resistState =
                            when (selectedSheetButtonPosition) {
                                FilterSheetButtonPosition.First -> {
                                    value.resistState.copy(
                                        first = EgoFilterResistArg(
                                            resist = value.resistState.first.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                FilterSheetButtonPosition.Second -> {
                                    value.resistState.copy(
                                        second = EgoFilterResistArg(
                                            resist = value.resistState.second.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                FilterSheetButtonPosition.Third -> {
                                    value.resistState.copy(
                                        third = EgoFilterResistArg(
                                            resist = value.resistState.third.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                FilterSheetButtonPosition.None -> throw IllegalArgumentException(
                                    "Trying to update resist button with none selected"
                                )
                            }
                        )
                    )
                }
            }
            is FilterDrawerSheetState.IdentityMode -> {
                val oldSinState = when (selectedSheetButtonPosition) {
                    FilterSheetButtonPosition.None -> {
                        throw IllegalStateException("Trying to update filter buttons state with none selected")
                    }
                    FilterSheetButtonPosition.First -> {
                        value.skillState
                    }
                    FilterSheetButtonPosition.Second -> {
                        value.skillState
                    }
                    FilterSheetButtonPosition.Third -> {
                        value.skillState
                    }
                }
                updateFilterDrawerSheetState(
                    value.copy(
                        skillState = FilterSkillBlockState(
                            oldSinState.damage,
                            updateSinStateBundle(
                                selectedSheetButtonPosition,
                                sin,
                                oldSinState.sin
                            )
                        )
                    )
                )
            }
        }
        _sinPickerState.update { SinPickerState.Gone }
        selectedSheetButtonPosition = FilterSheetButtonPosition.None
    }

    fun onFilterSkillButtonClick(button: FilterSheetButtonPosition) {
        when (val old = _filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> {
                updateFilterDrawerSheetState(
                    old.copy(
                        skillState = old.skillState.copy(damageType = cycleSkillDamageTypes(old.skillState.damageType))
                    )
                )
            }
            is FilterDrawerSheetState.IdentityMode -> {
                updateFilterDrawerSheetState(
                    old.copy(
                        skillState = FilterSkillBlockState(
                            updateDamageStateBundle(button, old.skillState.damage, false),
                            old.skillState.sin
                        )
                    )
                )
            }
        }
    }

    fun onIdentityFilterResistButtonClick(button: FilterSheetButtonPosition) {
        if (_filterDrawerShitState.value is FilterDrawerSheetState.IdentityMode) {
            val oldState = _filterDrawerShitState.value as FilterDrawerSheetState.IdentityMode
            updateFilterDrawerSheetState(
                oldState.copy(
                    resistState = updateDamageStateBundle(button, oldState.resistState, true)
                )
            )
        }
    }

    fun onEgoFilterPriceButtonLongPress(button: FilterSheetButtonPosition) {

    }

    //TODO refactor this heresy to use dataStore for all such operations
    fun onEgoFilterResistButtonClick(buttonPosition: FilterSheetButtonPosition) {
        if (_filterDrawerShitState.value is FilterDrawerSheetState.EgoMode) {
            val old = _filterDrawerShitState.value as FilterDrawerSheetState.EgoMode
            val new = when (buttonPosition) {
                FilterSheetButtonPosition.First -> {
                    old.copy(
                        resistState = old.resistState.copy(
                            first = old.resistState.first.copy(
                                resist = cycleEgoResistButtonState(old.resistState.first.resist)
                            )
                        )
                    )

                }
                FilterSheetButtonPosition.Second -> old.copy(
                    resistState = old.resistState.copy(
                        second = old.resistState.second.copy(
                            resist = cycleEgoResistButtonState(old.resistState.second.resist)
                        )
                    )
                )
                FilterSheetButtonPosition.Third -> old.copy(
                    resistState = old.resistState.copy(
                        third = old.resistState.third.copy(
                            resist = cycleEgoResistButtonState(old.resistState.third.resist)
                        )
                    )
                )
                FilterSheetButtonPosition.None -> throw IllegalArgumentException("Impossible button position")
            }
            updateFilterDrawerSheetState(new)
        }
    }

    private fun cycleEgoResistButtonState(
        type: EgoSinResistType
    ): EgoSinResistType {
        return when (type) {
            EgoSinResistType.INEFF -> EgoSinResistType.ENDURE
            EgoSinResistType.ENDURE -> EgoSinResistType.NORMAL
            EgoSinResistType.NORMAL -> EgoSinResistType.FATAL
            EgoSinResistType.FATAL -> EgoSinResistType.INEFF
        }
    }

    private fun updateFilterDrawerSheetState(newState: FilterDrawerSheetState) {
        viewModelScope.launch {
            when (newState) {
                is FilterDrawerSheetState.EgoMode -> {
                    preferencesRepository.updateEgoFilterSheetSettings(
                        newState
                    )
                }
                is FilterDrawerSheetState.IdentityMode -> {
                    preferencesRepository.updateIdentityFilterSheetSettings(
                        newState
                    )
                }
            }
        }
    }

    private fun itemListToFilterItemList(
        list: List<FilterItemTypeModel>,
        party: Party
    ): List<FilterDataModel> {
        return list.map { item ->
            when (item) {
                is FilterItemTypeModel.IdentityType ->
                    FilterDataModel(
                        item,
                        party.identityList.any { it.identity.id == item.identity.id }
                    )
                is FilterItemTypeModel.EgoType ->
                    FilterDataModel(item,
                        party.egoList.any { it.id == item.ego.id })
            }
        }
    }

    /**
     * Updates damage bundle with new value
     * @param unique this parameter decides if returning result must have unique damage types
     * (for resistance block) or non unique (for skills block)
     */
    private fun updateDamageStateBundle(
        button: FilterSheetButtonPosition,
        input: FilterDamageStateBundle,
        unique: Boolean
    ): FilterDamageStateBundle {
        var (first, second, third) = input
        when (button) {
            FilterSheetButtonPosition.First -> first = cycleSkillDamageTypes(first)
            FilterSheetButtonPosition.Second -> second = cycleSkillDamageTypes(second)
            FilterSheetButtonPosition.Third -> third = cycleSkillDamageTypes(third)
            FilterSheetButtonPosition.None ->
                throw IllegalStateException(UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED)
        }
        val result = FilterDamageStateBundle(first, second, third)
        return if (unique && !result.isUnique()) {
            updateDamageStateBundle(button, FilterDamageStateBundle(first, second, third), true)
        } else result
    }

    private fun updateSinStateBundle(
        button: FilterSheetButtonPosition,
        sin: StateType<Sin>,
        input: FilterSinStateBundle
    ): FilterSinStateBundle {
        var (first, second, third) = input
        when (button) {
            FilterSheetButtonPosition.First -> first = sin
            FilterSheetButtonPosition.Second -> second = sin
            FilterSheetButtonPosition.Third -> third = sin
            FilterSheetButtonPosition.None ->
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
        effectState: FilterEffectBlockState,
        sinnersState: FilterSinnersBlockState
    ): IdentityFilter {
        return IdentityFilter(
            resist = FilterResistSetArg(
                ineffective = resistState.first.toFilterDamageTypeArg(),
                normal = resistState.second.toFilterDamageTypeArg(),
                fatal = resistState.third.toFilterDamageTypeArg()
            ),
            skills = IdentityFilterSkillsSetArg(
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
            effects = effectState.effects.filter { it.value }.keys.toList(),
            sinners = sinnersState.sinners.filter { it.value }.keys.map { it.id }.toList()
        )
    }

    private fun formEgoFilter(state: FilterDrawerSheetState.EgoMode): EgoFilter {

        return EgoFilter(
            skillFilterArg = FilterSkillArg(
                damageType = state.skillState.damageType.toFilterDamageTypeArg(),
                sin = state.skillState.sinType.toFilterSinTypeArg()
            ),
            resistSetArg = EgoFilterSinResistTypeArg(
                state.resistState.toFilterArg()
            ),
            priceSetArg = EgoFilterPriceSetArg(emptyList<Sin>()),
            effects =
            state.effectsState.effects.filter { it.value }.keys.toList(),
            sinners = state.sinnersState.sinners.filter { it.value }.keys.map { it.id }.toList()

        )
    }

    companion object {
        private const val UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED =
            "Trying to update filter buttons state with none selected"
    }
}

