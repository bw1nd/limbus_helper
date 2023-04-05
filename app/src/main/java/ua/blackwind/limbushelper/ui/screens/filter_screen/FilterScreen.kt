package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.ui.common.FilterModeSegmentedButton
import ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet.FilterDrawerSheet
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterDataModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities by viewModel.filteredItems.collectAsState()
    val filterDrawerSheetState by viewModel.filterDrawerShitState.collectAsState()
    val filterDrawerTab by viewModel.filterDrawerSheetTab.collectAsState()
    val sinPickerState by viewModel.sinPickerState.collectAsState()
    val filterMode by viewModel.filterMode.collectAsState()


    val filterSheetStateMethods = FilterDrawerSheetMethods(
        onSwitchChange = viewModel::onFilterTabSwitch,
        onFilterButtonClick = viewModel::onFilterButtonClick,
        onClearFilterButtonPress = viewModel::onClearFilterButtonPress,
        onSkillButtonClick = viewModel::onFilterSkillButtonClick,
        onSkillButtonLongPress = viewModel::onFilterSkillButtonLongPress,
        onSinPickerClick = viewModel::onFilterSinPickerPress,
        onIdentityResistButtonClick = viewModel::onIdentityFilterResistButtonClick,
        onEgoResistButtonClick = viewModel::onEgoFilterResistButtonClick,
        onEgoResistButtonLongPress = viewModel::onEgoResistButtonLongPress,
        onEgoPriceButtonLongPress = viewModel::onEgoFilterPriceButtonLongPress,
        onEffectCheckedChange = viewModel::onEffectCheckedChange,
        onSinnerCheckedChange = viewModel::onSinnerCheckedChange
    )

    FilterScreenUi(
        list = identities,
        filterDrawerSheetTab = filterDrawerTab,
        filterDrawerSheetState = filterDrawerSheetState,
        filterMode = filterMode,
        sinPickerState = sinPickerState,
        filterSheetMethods = filterSheetStateMethods,
        onFilterModeChanged = viewModel::onFilterModeSwitch,
        onInPartyChecked = viewModel::onItemInPartyChecked,
        onInPartyUnChecked = viewModel::onItemInPartyUnChecked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenUi(
    list: List<FilterDataModel>,
    filterDrawerSheetTab: FilterSheetTab,
    filterDrawerSheetState: FilterDrawerSheetState,
    filterMode: FilterMode,
    sinPickerState: SinPickerState,
    filterSheetMethods: FilterDrawerSheetMethods,
    onFilterModeChanged: (Int) -> Unit,
    onInPartyChecked: (FilterDataModel) -> Unit,
    onInPartyUnChecked: (FilterDataModel) -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            FilterDrawerSheet(
                mode = filterDrawerSheetTab,
                filterState = filterDrawerSheetState,
                sinPickerState = sinPickerState,
                methods = filterSheetMethods
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            FilterModeSegmentedButton(
                state = filterMode,
                color = MaterialTheme.colorScheme.onPrimary,
                onItemSelection = onFilterModeChanged
            )

            if (list.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight(.3f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = stringResource(id = R.string.empty_filter)
                    )
                }
            } else {
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
                            items(list.size) {
                                FilterListItem(
                                    listItem = list[it],
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
    }
}