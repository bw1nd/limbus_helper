package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.usecase.*
import ua.blackwind.limbushelper.ui.util.*
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase
): ViewModel() {
    private val _filteredIdentities = MutableStateFlow<List<Identity>>(emptyList())
    val filteredIdentities: StateFlow<List<Identity>> = _filteredIdentities

    private val _filterSkillsState = MutableStateFlow(
        FilterSkillBlockState(
            FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty),
            FilterSinStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)
        )
    )
    val filterSkillsState: StateFlow<FilterSkillBlockState> = _filterSkillsState

    private val _filterResistState = MutableStateFlow(
        FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)
    )
    val filterResistState: StateFlow<FilterDamageStateBundle> = _filterResistState

    private val _sinPickerVisible = MutableStateFlow(false)
    val sinPickerVisible: StateFlow<Boolean> = _sinPickerVisible

    private var selectedSkillButtonId = 0

    init {
        viewModelScope.launch {
            _filteredIdentities.update {
                getFilteredIdentitiesUseCase(
                    IdentityFilter(
                        resist = FilterResistSetArg(
                            ineffective = FilterDamageTypeArg.Empty,
                            normal = FilterDamageTypeArg.Empty,
                            fatal = FilterDamageTypeArg.Empty
                        ),
                        skills = FilterSkillsSetArg(
                            first = FilterSkillArg(
                                FilterDamageTypeArg.Empty,
                                FilterSinTypeArg.Empty
                            ),
                            second = FilterSkillArg(
                                FilterDamageTypeArg.Empty,
                                FilterSinTypeArg.Empty
                            ),
                            third = FilterSkillArg(
                                FilterDamageTypeArg.Empty,
                                FilterSinTypeArg.Empty
                            )
                        ),
                        effects = emptyList()
                    )
                )
            }
        }
    }

    fun onFilterModeSwitch(checked: Boolean) {
        //TODO add mode switch logic
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
}