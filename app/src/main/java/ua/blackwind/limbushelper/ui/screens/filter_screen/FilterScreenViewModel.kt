package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.usecase.*
import ua.blackwind.limbushelper.ui.util.*
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase
): ViewModel() {
    private val _filteredIdentities = MutableStateFlow<List<Identity>>(emptyList())
    val filteredIdentities: StateFlow<List<Identity>> = _filteredIdentities

    private val _filterSkillsState = MutableStateFlow(
        emptyFilterSkillBlockState()
    )

    val filterSkillsState: StateFlow<FilterSkillBlockState> = _filterSkillsState

    private val _filterResistState = MutableStateFlow(
        emptyFilterResistStateBundle()
    )
    val filterResistState: StateFlow<FilterDamageStateBundle> = _filterResistState

    private val _filterEffectBlockState = MutableStateFlow(
        emptyFilterEffectBlockState()
    )
    val filterEffectBlockState: StateFlow<FilterEffectBlockState> = _filterEffectBlockState

    private val _sinPickerVisible = MutableStateFlow(false)
    val sinPickerVisible: StateFlow<Boolean> = _sinPickerVisible

    private val _filterSheetMode = MutableStateFlow<FilterSheetMode>(FilterSheetMode.Type)
    val filterSheetMode: StateFlow<FilterSheetMode> = _filterSheetMode


    private var selectedSkillButtonId = 0

    fun onFilterButtonClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _filteredIdentities.update {
                val skillState = _filterSkillsState.value
                val resistState = _filterResistState.value
                val effectState = _filterEffectBlockState.value
                getFilteredIdentitiesUseCase(
                    formIdentityFilter(resistState, skillState, effectState)
                )
            }
        }
    }

    fun onFilterModeSwitch(id: Int) {
        when (id) {
            0 -> _filterSheetMode.update { FilterSheetMode.Type }
            1 -> _filterSheetMode.update { FilterSheetMode.Effects }
            else -> throw IllegalArgumentException("Wrong switch button id: $id")
        }
    }

    fun onClearFilterButtonPress() {
        viewModelScope.launch {
            _filterSkillsState.update { emptyFilterSkillBlockState() }
            _filterResistState.update { emptyFilterResistStateBundle() }
            _filterEffectBlockState.update { emptyFilterEffectBlockState() }
        }

    }

    fun onEffectCheckedChange(checked: Boolean, effect: Effect) {
        _filterEffectBlockState.update { state ->
            val new = state.effects.toMutableMap()
            new[effect] = checked
            FilterEffectBlockState(
                new
            )
        }
    }

    fun onFilterSkillButtonLongPress(id: Int) {
        selectedSkillButtonId = id
        _sinPickerVisible.update { true }
    }

    fun onFilterSinPickerPress(sin: StateType<Sin>) {
        _sinPickerVisible.update { false }

        _filterSkillsState.update { old ->
            FilterSkillBlockState(
                old.damage,
                updateSinStateBundle(selectedSkillButtonId, sin, old.sin)
            )
        }
        selectedSkillButtonId = 0
    }

    fun onFilterSkillButtonClick(id: Int) {
        _filterSkillsState.update { state ->
            FilterSkillBlockState(
                updateDamageStateBundle(id, state.damage, false),
                state.sin
            )
        }
    }

    fun onFilterResistButtonClick(id: Int) {
        _filterResistState.update { state ->
            updateDamageStateBundle(id, state, true)
        }
    }

    /**
     * Updates damage bundle with new value
     * @param unique this parameter decides if returning result must have unique damage types
     * (for resistance block) or non unique (for skills block)
     */
    private fun updateDamageStateBundle(
        buttonId: Int,
        input: FilterDamageStateBundle,
        unique: Boolean
    ): FilterDamageStateBundle {
        var (first, second, third) = input
        when (buttonId) {
            1 -> first = cycleSkillDamageTypes(first)
            2 -> second = cycleSkillDamageTypes(second)
            3 -> third = cycleSkillDamageTypes(third)
        }
        val result = FilterDamageStateBundle(first, second, third)
        return if (unique && !result.isUnique()) {
            updateDamageStateBundle(buttonId, FilterDamageStateBundle(first, second, third), unique)
        } else result
    }

    private fun updateSinStateBundle(
        buttonId: Int,
        sin: StateType<Sin>,
        input: FilterSinStateBundle
    ): FilterSinStateBundle {
        var (first, second, third) = input
        when (buttonId) {
            1 -> first = sin
            2 -> second = sin
            3 -> third = sin
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
}