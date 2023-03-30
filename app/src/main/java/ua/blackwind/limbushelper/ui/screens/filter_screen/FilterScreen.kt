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
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet.FilterDrawerSheet
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterIdentityModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities by viewModel.filteredIdentities.collectAsState()
    val filterDrawerSheetState by viewModel.filterDrawerShitState.collectAsState()
    val filterDrawerMode by viewModel.filterDrawerSheetMode.collectAsState()
    val filterSkillSinPickerVisible by viewModel.sinPickerVisible.collectAsState()
    val labels = FilterResistButtonLabels(
        stringResource(R.string.res_ineff),
        stringResource(R.string.res_normal),
        stringResource(R.string.res_fatal)
    )
    val filterSheetStateMethods = FilterDrawerSheetMethods(
        onSwitchChange = viewModel::onFilterModeSwitch,
        onFilterButtonClick = viewModel::onFilterButtonClick,
        onClearFilterButtonPress = viewModel::onClearFilterButtonPress,
        onSkillButtonClick = viewModel::onFilterSkillButtonClick,
        onSkillButtonLongPress = viewModel::onFilterSkillButtonLongPress,
        onSinPickerClick = viewModel::onFilterSinPickerPress,
        onResistButtonClick = viewModel::onFilterResistButtonClick,
        onEffectCheckedChange = viewModel::onEffectCheckedChange,
        onSinnerCheckedChange = viewModel::onSinnerCheckedChange
    )

    FilterScreenUi(
        identities = identities,
        filterDrawerMode = filterDrawerMode,
        filterDrawerSheetState = filterDrawerSheetState,
        filterSkillSinPickerVisible = filterSkillSinPickerVisible,
        resistLabels = labels,
        filterSheetMethods = filterSheetStateMethods,
        onInPartyChecked = viewModel::onIdentityItemInPartyChecked,
        onInPartyUnChecked = viewModel::onIdentityItemInPartyUnChecked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenUi(
    identities: List<FilterIdentityModel>,
    filterDrawerMode: FilterSheetMode,
    filterDrawerSheetState: FilterDrawerSheetState,
    filterSkillSinPickerVisible: Boolean,
    resistLabels: FilterResistButtonLabels,
    filterSheetMethods: FilterDrawerSheetMethods,
    onInPartyChecked: (Identity) -> Unit,
    onInPartyUnChecked: (Identity) -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            FilterDrawerSheet(
                mode = filterDrawerMode,
                filterState = filterDrawerSheetState,
                sinPickerVisible = filterSkillSinPickerVisible,
                resistLabels = resistLabels,
                methods = filterSheetMethods
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