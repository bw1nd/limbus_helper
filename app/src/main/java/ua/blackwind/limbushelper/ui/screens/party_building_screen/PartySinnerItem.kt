package ua.blackwind.limbushelper.ui.screens.party_building_screen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.util.previewIdentity

private const val SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP = 150

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
    Column(Modifier.padding(horizontal = 5.dp)) {
        Text(fontSize = 22.sp, color = MaterialTheme.colorScheme.tertiary, text = sinner.name)
        Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 2.dp)
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
                    //TODO need to fix bug in snapTo function which throws IllegalArgument and crashes app
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
