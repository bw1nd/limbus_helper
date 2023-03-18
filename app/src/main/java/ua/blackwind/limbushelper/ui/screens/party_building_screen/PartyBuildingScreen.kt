package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.previewIdentity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel

private const val SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP = 150

@Destination
@Composable
fun PartyBuildingScreen() {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {

            PartyBuildingScreenUi(
                party.value,
                viewModel::onIdentitySwipe,
                viewModel::onIdentityClick,
                viewModel::onIdentityLongPress
            )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PartyBuildingScreenUi(
    party: List<PartySinnerModel>,
    onIdentityItemSwipe: (Identity) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit
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
                        sinner = sinner,
                        identities = identities,
                        onIdentityItemSwipe = onIdentityItemSwipe,
                        onIdentityItemClick,
                        onIdentityItemLongPress
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PartySinnerItem(
    sinner: Sinner, identities: List<PartyIdentity>,
    onIdentityItemSwipe: (Identity) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit
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
            identities.forEach { previewIdentity ->
                key(previewIdentity.identity.id) {
                    val current by rememberUpdatedState(newValue = previewIdentity)
                    SwipeToDismiss(
                        state = rememberDismissState(
                            confirmValueChange = { dismiss ->
                                if (dismiss != DismissValue.Default) {
                                    val identity = current.identity
                                    onIdentityItemSwipe(identity)
                                    true
                                } else {
                                    false
                                }
                            },
                            positionalThreshold = {
                                SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP.dp.toPx()
                            }
                        ),
                        background = { Spacer(Modifier) },
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
        sinner = Sinner(0, "Faust"),
        identities = listOf(
            PartyIdentity(previewIdentity, true), PartyIdentity(previewIdentity, false)
        ),
        {}, {}, { _, _ -> }
    )
}



