package ua.blackwind.limbushelper.ui.screens.party_building_screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.previewIdentity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel

private const val SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP = 150

@Destination
@Composable
fun PartyBuildingScreen(showSnackBar: suspend (String, String) -> SnackbarResult) {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {

            PartyBuildingScreenUi(
                coroutineScope,
                party.value,
                viewModel::onIdentitySwipe,
                viewModel::onIdentityClick,
                viewModel::onIdentityLongPress,
                showSnackBar,
                viewModel::undoDelete
            )
        }
    }
}

@Composable
fun PartyBuildingScreenUi(
    coroutineScope: CoroutineScope,
    party: List<PartySinnerModel>,
    onIdentityItemSwipe: (Identity) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    showSnackBar: suspend (String, String) -> SnackbarResult,
    undoDelete: (Identity) -> Unit
) {
    if (party.isEmpty()) {
        Text(text = "Your party is empty.\nStart with filter screen and something here.")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            items(party.size, key = { it }) { index ->
                val sinner = party[index].sinner
                val identities = party[index].identities.sortedByDescending { it.identity.id }
                if (identities.isNotEmpty()) {
                    PartySinnerItem(
                        coroutineScope,
                        sinner = sinner,
                        identities = identities,
                        onIdentityItemSwipe = onIdentityItemSwipe,
                        onIdentityItemClick,
                        onIdentityItemLongPress,
                        showSnackBar,
                        undoDelete
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartySinnerItem(
    coroutineScope: CoroutineScope,
    sinner: Sinner, identities: List<PartyIdentity>,
    onIdentityItemSwipe: (Identity) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    showSnackBar: suspend (String, String) -> SnackbarResult,
    undoDelete: (Identity) -> Unit
) {
    Column(Modifier.padding(5.dp)) {
        Text(fontSize = 24.sp, text = sinner.name)
        Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
        Spacer(modifier = Modifier.size(5.dp))
        Column(
            Modifier,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            val undoLabel = stringResource(R.string.undo_delete)
            val removedLabel = stringResource(R.string.removed_from_party)
            identities.forEach { previewIdentity ->
                key(previewIdentity.identity) {

                    val current by rememberUpdatedState(newValue = previewIdentity)
                    val dismissState = rememberDismissState(
                        confirmValueChange = { dismiss ->
                            if (dismiss != DismissValue.Default) {
                                val identity = current.identity
                                onIdentityItemSwipe(identity)
                                coroutineScope.launch {
                                    val snackResult = showSnackBar(
                                        "${identity.name} $removedLabel",
                                        undoLabel
                                    )
                                    if (snackResult == SnackbarResult.ActionPerformed) {
                                        undoDelete(identity)
                                    }
                                }
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = {
                            SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP.dp.toPx()
                        }
                    )
                    LaunchedEffect(key1 = Unit) {
                        try {
                            coroutineScope.launch { dismissState.snapTo(DismissValue.Default) }
                        } catch (e: Exception) {
                            Log.e("DELETE_UNDO", "SnapTo exception: ${e.message}")
                        }
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        background = { Spacer(Modifier) },
                        directions = setOf(DismissDirection.StartToEnd),
                        dismissContent = {
                            PartyIdentityItem(
                                previewIdentity,
                                onIdentityItemClick,
                                onIdentityItemLongPress
                            )
                        },
                        modifier = Modifier
                            .animateContentSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPartySinnerItem() {
    PartySinnerItem(
        rememberCoroutineScope(),
        sinner = Sinner(0, "Faust"),
        identities = listOf(
            PartyIdentity(previewIdentity, true), PartyIdentity(previewIdentity, false)
        ),
        {}, {}, { _, _ -> }, { _, _ -> SnackbarResult.ActionPerformed }, {}
    )
}



