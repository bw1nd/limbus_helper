package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    }

    fun onFilterSkillButtonClick(id: Int) {

    }

    fun onFilterResistButtonClick(id: Int) {

    }
}