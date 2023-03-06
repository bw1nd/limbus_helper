package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.usecase.*
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    getFilteredIdentitiesUseCase: GetFilteredIdentitiesUseCase
): ViewModel() {
    private val _filteredIdentities = MutableStateFlow<List<Identity>>(emptyList())
    val filteredIdentities: StateFlow<List<Identity>> = _filteredIdentities

    private val _filterSkillsState = MutableStateFlow(
        FilterSkillBlockState(
            first = FilterSkillButtonState(null, null),
            second = FilterSkillButtonState(null, null),
            third = FilterSkillButtonState(null, null)
        )
    )
    val filterSkillsState: StateFlow<FilterSkillBlockState> = _filterSkillsState

    private val _filterResistState = MutableStateFlow(
        FilterResistBlockState(null, null, null)
    )
    val filterResistState: StateFlow<FilterResistBlockState> = _filterResistState

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

    fun onFilterSkillButtonClick(id: Int) {
        val newSkillState: (FilterSkillButtonState, DamageType?) -> FilterSkillButtonState =
            { oldState, type ->
                FilterSkillButtonState(type, oldState.sin)
            }
        _filterSkillsState.update { state ->
            when (id) {
                1 -> FilterSkillBlockState(
                    newSkillState(
                        state.first, cycleSkillDamageTypes(state.first.type)
                    ),
                    state.second,
                    state.third
                )
                2 -> FilterSkillBlockState(
                    state.first,
                    newSkillState(
                        state.second, cycleSkillDamageTypes(state.second.type)
                    ),
                    state.third
                )
                3 -> FilterSkillBlockState(
                    state.first,
                    state.second,
                    newSkillState(
                        state.third, cycleSkillDamageTypes(state.third.type)
                    )
                )
                else -> throw IllegalArgumentException(
                    "Skill button id: $id out of range 1..3"
                )
            }
        }
    }

    fun onFilterResistButtonClick(id: Int) {
        _filterResistState.update { oldState ->
            when (id) {
                1 -> FilterResistBlockState(
                    cycleSkillDamageTypes(oldState.ineffective),
                    oldState.normal,
                    oldState.fatal
                )
                2 -> FilterResistBlockState(
                    oldState.ineffective,
                    cycleSkillDamageTypes(oldState.normal),
                    oldState.fatal
                )
                3 -> FilterResistBlockState(
                    oldState.ineffective,
                    oldState.normal,
                    cycleSkillDamageTypes(oldState.fatal)
                )
                else ->
                    throw java.lang.IllegalArgumentException("Resist button id: $id out of range 1..3")
            }
        }
    }

    private fun cycleSkillDamageTypes(type: DamageType?): DamageType? {
        return when (type) {
            DamageType.SLASH -> DamageType.PIERCE
            DamageType.PIERCE -> DamageType.BLUNT
            DamageType.BLUNT -> null
            null -> DamageType.SLASH
        }
    }
}