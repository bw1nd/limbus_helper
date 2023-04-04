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
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.filter.*
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.usecase.AddIdentityToPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.DeleteIdentityFromPartyUseCase
import ua.blackwind.limbushelper.domain.party.usecase.GetPartyUseCase
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterDataModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterItemTypeModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.StateType
import ua.blackwind.limbushelper.ui.util.toFilterDamageTypeArg
import ua.blackwind.limbushelper.ui.util.toFilterSinTypeArg
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase,
    private val getFilteredEgoUseCase: GetFilteredEgoUseCase,
    private val addIdentityToPartyUseCase: AddIdentityToPartyUseCase,
    private val deleteIdentityFromPartyUseCase: DeleteIdentityFromPartyUseCase,
    private val getPartyUseCase: GetPartyUseCase,
    private val filterSheetSettingsMapper: IdentityFilterSettingsMapper,
    private val filterEgoSettingsMapper: EgoFilterSettingsMapper
): ViewModel() {

    private val party = MutableStateFlow(Party(0, "Default", emptyList()))

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

    private val _skillSinPickerVisible = MutableStateFlow(false)
    val skillSinPickerVisible = _skillSinPickerVisible.asStateFlow()

    private val _resistSinPickerVisible = MutableStateFlow(false)
    val resistSinPickerVisible = _resistSinPickerVisible.asStateFlow()

    private var selectedSkillSheetButtonPosition: SelectedSkillButtonPosition =
        SelectedSkillButtonPosition.None
    private var selectedResistSheetButtonPosition: SelectedResistButtonPosition =
        SelectedResistButtonPosition.None

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
                _filterDrawerShitState.update { newState }
                onFilterButtonClick()
            }
        }
    }

    fun onFilterButtonClick() {
        //TODO add dispatchers injection
        val filterTime = measureTimeMillis {
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
                        is FilterDrawerSheetState.EgoMode -> getFilteredEgoUseCase(
                            EgoFilter(
                                FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty),
                                EgoFilterSinResistTypeArg(emptyList()),
                                EgoFilterPriceSetArg(emptyList()),
                                emptyList(), emptyList()
                            )
                        ).map { FilterDataModel(FilterItemTypeModel.EgoType(it), false) }
                    }
                }
            }
        }
        Log.d("USECASE", "Execution time $filterTime")
    }

    fun onIdentityItemInPartyChecked(identity: FilterDataModel) {
        viewModelScope.launch {
            when (identity.item) {
                is FilterItemTypeModel.EgoType -> TODO()
                is FilterItemTypeModel.IdentityType -> addIdentityToPartyUseCase(
                    identity.item.identity,
                    party.value
                )
            }

        }
    }

    fun onIdentityItemInPartyUnChecked(identity: FilterDataModel) {
        viewModelScope.launch {
            when (identity.item) {
                is FilterItemTypeModel.EgoType -> TODO()
                is FilterItemTypeModel.IdentityType -> deleteIdentityFromPartyUseCase(
                    identity.item.identity,
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
        updateFilterDrawerSheetState(FilterDrawerSheetState.IdentityMode.getDefaultState())
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

    fun onFilterSkillButtonLongPress(selected: SelectedSkillButtonPosition) {
        selectedSkillSheetButtonPosition = selected
        _resistSinPickerVisible.update { false }
        _skillSinPickerVisible.update { true }
    }

    fun onEgoResistButtonLongPress(selected: SelectedResistButtonPosition) {
        selectedResistSheetButtonPosition = selected
        _skillSinPickerVisible.update { false }
        _resistSinPickerVisible.update { true }
    }

    fun onFilterSinPickerPress(sin: StateType<Sin>) {
        when (val value = _filterDrawerShitState.value) {
            is FilterDrawerSheetState.EgoMode -> {
                if (_skillSinPickerVisible.value) {
                    updateFilterDrawerSheetState(
                        value.copy(
                            skillState = EgoFilterSkillBlockState(
                                value.skillState.damageType,
                                sin
                            )
                        )
                    )
                }
                if (_resistSinPickerVisible.value) {
                    Log.d("DATA_STORE", "Trying to update resist sin")
                    updateFilterDrawerSheetState(
                        value.copy(
                            resistState =
                            when (selectedResistSheetButtonPosition) {
                                SelectedResistButtonPosition.First -> {
                                    value.resistState.copy(
                                        first = EgoFilterResistArg(
                                            resist = value.resistState.first.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                SelectedResistButtonPosition.Second -> {
                                    value.resistState.copy(
                                        second = EgoFilterResistArg(
                                            resist = value.resistState.second.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                SelectedResistButtonPosition.Third -> {
                                    value.resistState.copy(
                                        third = EgoFilterResistArg(
                                            resist = value.resistState.third.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                SelectedResistButtonPosition.Fourth -> {
                                    value.resistState.copy(
                                        fourth = EgoFilterResistArg(
                                            resist = value.resistState.fourth.resist,
                                            sin = sin
                                        )
                                    )
                                }
                                SelectedResistButtonPosition.None -> throw IllegalArgumentException(
                                    "Trying to update resist button with none selected"
                                )
                            }
                        )
                    )
                }
            }
            is FilterDrawerSheetState.IdentityMode -> {
                val oldSinState = when (selectedSkillSheetButtonPosition) {
                    SelectedSkillButtonPosition.None -> {
                        throw IllegalStateException("Trying to update filter buttons state with none selected")
                    }
                    SelectedSkillButtonPosition.First -> {
                        value.skillState
                    }
                    SelectedSkillButtonPosition.Second -> {
                        value.skillState
                    }
                    SelectedSkillButtonPosition.Third -> {
                        value.skillState
                    }
                }
                updateFilterDrawerSheetState(
                    value.copy(
                        skillState = FilterSkillBlockState(
                            oldSinState.damage,
                            updateSinStateBundle(
                                selectedSkillSheetButtonPosition,
                                sin,
                                oldSinState.sin
                            )
                        )
                    )
                )
            }
        }
        _skillSinPickerVisible.update { false }
        _resistSinPickerVisible.update { false }
        selectedResistSheetButtonPosition = SelectedResistButtonPosition.None
        selectedSkillSheetButtonPosition = SelectedSkillButtonPosition.None
    }

    fun onFilterSkillButtonClick(button: SelectedSkillButtonPosition) {
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

    fun onFilterResistButtonClick(button: SelectedSkillButtonPosition) {
        val oldState = _filterDrawerShitState.value as FilterDrawerSheetState.IdentityMode

        updateFilterDrawerSheetState(
            oldState.copy(
                resistState = updateDamageStateBundle(button, oldState.resistState, true)
            )
        )
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
                    FilterDataModel(item, false)
            }
        }
    }

    /**
     * Updates damage bundle with new value
     * @param unique this parameter decides if returning result must have unique damage types
     * (for resistance block) or non unique (for skills block)
     */
    private fun updateDamageStateBundle(
        button: SelectedSkillButtonPosition,
        input: FilterDamageStateBundle,
        unique: Boolean
    ): FilterDamageStateBundle {
        var (first, second, third) = input
        when (button) {
            SelectedSkillButtonPosition.First -> first = cycleSkillDamageTypes(first)
            SelectedSkillButtonPosition.Second -> second = cycleSkillDamageTypes(second)
            SelectedSkillButtonPosition.Third -> third = cycleSkillDamageTypes(third)
            SelectedSkillButtonPosition.None ->
                throw IllegalStateException(UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED)
        }
        val result = FilterDamageStateBundle(first, second, third)
        return if (unique && !result.isUnique()) {
            updateDamageStateBundle(button, FilterDamageStateBundle(first, second, third), true)
        } else result
    }

    private fun updateSinStateBundle(
        button: SelectedSkillButtonPosition,
        sin: StateType<Sin>,
        input: FilterSinStateBundle
    ): FilterSinStateBundle {
        var (first, second, third) = input
        when (button) {
            SelectedSkillButtonPosition.First -> first = sin
            SelectedSkillButtonPosition.Second -> second = sin
            SelectedSkillButtonPosition.Third -> third = sin
            SelectedSkillButtonPosition.None ->
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

    companion object {
        private const val UPDATE_FILTER_BUTTONS_WITH_NONE_SELECTED =
            "Trying to update filter buttons state with none selected"
    }
}

