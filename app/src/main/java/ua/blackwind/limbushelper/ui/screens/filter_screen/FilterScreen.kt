package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterIdentityModel
import ua.blackwind.limbushelper.ui.util.*

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities by viewModel.filteredIdentities.collectAsState()
    val filterSheetMode by viewModel.filterSheetMode.collectAsState()
    val filterSkillState by viewModel.filterSkillsState.collectAsState()
    val filterResistState by viewModel.filterResistState.collectAsState()
    val filterEffectsState by viewModel.filterEffectBlockState.collectAsState()
    val sinPickerVisible by viewModel.sinPickerVisible.collectAsState()
    val labels = FilterResistButtonLabels(
        stringResource(R.string.res_ineff),
        stringResource(R.string.res_normal),
        stringResource(R.string.res_fatal)
    )

    FilterScreenUi(
        identities = identities,
        filterSheetMode = filterSheetMode,
        skillState = filterSkillState,
        resistState = filterResistState,
        effectsState = filterEffectsState,
        resistLabels = labels,
        sinPickerVisible = sinPickerVisible,
        onSwitchChange = viewModel::onFilterModeSwitch,
        onFilterButtonPress = viewModel::onFilterButtonClick,
        onClearFilterButtonPress = viewModel::onClearFilterButtonPress,
        onSkillButtonClick = viewModel::onFilterSkillButtonClick,
        onSkillButtonLongPress = viewModel::onFilterSkillButtonLongPress,
        onSinPickerClick = viewModel::onFilterSinPickerPress,
        onResistButtonClick = viewModel::onFilterResistButtonClick,
        onEffectCheckedChange = viewModel::onEffectCheckedChange,
        onInPartyChecked = viewModel::onIdentityItemInPartyChecked,
        onInPartyUnChecked = viewModel::onIdentityItemInPartyUnChecked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenUi(
    identities: List<FilterIdentityModel>,
    filterSheetMode: FilterSheetMode,
    skillState: FilterSkillBlockState,
    resistState: FilterDamageStateBundle,
    effectsState: FilterEffectBlockState,
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSwitchChange: (Int) -> Unit,
    onFilterButtonPress: () -> Unit,
    onClearFilterButtonPress: () -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit,
    onEffectCheckedChange: (Boolean, Effect) -> Unit,
    onInPartyChecked: (Identity) -> Unit,
    onInPartyUnChecked: (Identity) -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            FilterDrawerSheet(
                filterSheetMode = filterSheetMode,
                skillState = skillState,
                resistState = resistState,
                effectsState = effectsState,
                resistLabels = resistLabels,
                sinPickerVisible = sinPickerVisible,
                onSwitchChange = onSwitchChange,
                onFilterButtonClick = onFilterButtonPress,
                onClearFilterButtonPress = onClearFilterButtonPress,
                onSkillButtonClick = onSkillButtonClick,
                onSkillButtonLongPress = onSkillButtonLongPress,
                onResistButtonClick = onResistButtonClick,
                onSinPickerClick = onSinPickerClick,
                onEffectCheckedChange = onEffectCheckedChange
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(padding)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    items(identities.size) {
                        FilterIdentityItem(
                            viewIdentity = identities[it],
                            onInPartyChecked = onInPartyChecked,
                            onInPartyUnChecked = onInPartyUnChecked
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }
    }
}

//@Preview(showSystemUi = true, widthDp = 412, heightDp = 846)
//@Composable
//fun PreviewFilterScreen() {
//    FilterScreenUi(identities = listOf(previewIdentity, previewIdentity, previewIdentity))
//}