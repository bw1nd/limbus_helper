package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyBuildingInfoPanelState
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel
import ua.blackwind.limbushelper.ui.theme.wrath

@Destination
@Composable
fun PartyBuildingScreen(showSnackBar: suspend (String, String) -> SnackbarResult) {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    val infoPanelState by viewModel.infoPanelState.collectAsState()
    val isShowActiveChecked by viewModel.showOnlyActiveIdentities.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val undoLabel = stringResource(R.string.undo_delete)
    val removedLabel = stringResource(R.string.removed_from_party)
    val coroutineScope = rememberCoroutineScope()

    val onDeleteButtonClick: (Identity) -> Unit = { identity ->
        viewModel.onIdentityDeleteButtonClick(identity)
        coroutineScope.launch {
            val snackResult =
                showSnackBar.invoke(String.format(removedLabel, identity.name), undoLabel)
            if (snackResult == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete(identity)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            PartyBuildingScreenUi(
                party = party.value,
                infoPanelState = infoPanelState,
                showDialog = showDialog,
                isShowActiveIdentitiesChecked = isShowActiveChecked,
                onClearPartyClick = viewModel::onClearPartyClick,
                onDismissDialog = viewModel::onAlertDialogDismiss,
                onAcceptPartyClear = viewModel::onCleaPartyAcceptClick,
                onShowActiveIdentitiesClick = viewModel::onShowActiveIdentitiesClick,
                onIdentityDeleteButtonClick = onDeleteButtonClick,
                onIdentityItemClick = viewModel::onIdentityClick,
                onIdentityItemLongPress = viewModel::onIdentityLongPress,
                onEgoDeleteButtonClick = viewModel::onEgoDeleteButtonClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyBuildingScreenUi(
    party: List<PartySinnerModel>,
    infoPanelState: PartyBuildingInfoPanelState,
    showDialog: Boolean,
    isShowActiveIdentitiesChecked: Boolean,
    onClearPartyClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onAcceptPartyClear: () -> Unit,
    onShowActiveIdentitiesClick: (Boolean) -> Unit,
    onIdentityDeleteButtonClick: (Identity) -> Unit,
    onEgoDeleteButtonClick: (Ego) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
) {
    if (party.isEmpty()) {
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
                text = stringResource(id = R.string.empty_party)
            )
        }
    } else {
        Column {
            PartyBuildingInfoPanel(
                state = infoPanelState,
                isShowActiveIdentitiesChecked = isShowActiveIdentitiesChecked,
                onShowActiveIdentitiesClick = onShowActiveIdentitiesClick,
                onClearPartyClick = onClearPartyClick
            )
            Divider(
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (showDialog) {
                AlertDialog(onDismissRequest = { onDismissDialog() }) {
                    Surface(shape = RoundedCornerShape(6)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .size(300.dp, 260.dp)
                                .padding(10.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(.2f))
                            Text(
                                text = "This will delete\nall Ego and Identity\nfrom party.\nProceed?",
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row() {
                                Button(onClick = { onAcceptPartyClear() }) {
                                    Text("Accept", textDecoration = TextDecoration.Underline)
                                }
                                Spacer(modifier = Modifier.width(50.dp))
                                Button(onClick = { onDismissDialog() }) {
                                    Text("Dismiss", textDecoration = TextDecoration.Underline)
                                }
                            }
                        }
                    }
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                items(party.size, key = { it }) { index ->
                    val sinnerModel = party[index]
                    val show = if (isShowActiveIdentitiesChecked) {
                        sinnerModel.identities.any { it.isActive }
                    } else {
                        sinnerModel.identities.isNotEmpty() || sinnerModel.egos.isNotEmpty()
                    }
                    if (show) {
                        PartySinnerItem(
                            sinnerModel,
                            showInactive = !isShowActiveIdentitiesChecked,
                            onIdentityItemClick = onIdentityItemClick,
                            onIdentityItemLongPress = onIdentityItemLongPress,
                            onIdentityDeleteButtonClick = onIdentityDeleteButtonClick,
                            onEgoDeleteButtonClick = onEgoDeleteButtonClick
                        )
                    }
                }
            }
        }
    }
}


