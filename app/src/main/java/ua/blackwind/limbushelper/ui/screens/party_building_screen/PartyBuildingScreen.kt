package ua.blackwind.limbushelper.ui.screens.party_building_screen

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.util.previewIdentity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.*
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinIcon

private const val SWIPE_DISTANCE_TO_DELETE_IDENTITY_DP = 150

@Destination
@Composable
fun PartyBuildingScreen(showSnackBar: suspend (String, String) -> SnackbarResult) {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    val infoPanelState by viewModel.infoPanelState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {

            PartyBuildingScreenUi(
                coroutineScope,
                party.value,
                infoPanelState,
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
    infoPanelState: PartyBuildingInfoPanelState,
    onIdentityItemSwipe: (Identity) -> Unit,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    showSnackBar: suspend (String, String) -> SnackbarResult,
    undoDelete: (Identity) -> Unit
) {
    if (party.isEmpty()) {
        Text(text = "Your party is empty.\nStart with filter screen and something here.")
    } else {
        Column {
            PartyBuildingInfoPanel(infoPanelState)
            Divider(
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
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
}

@Composable
fun PartyBuildingInfoPanel(state: PartyBuildingInfoPanelState) {
    val size = LocalConfiguration.current.screenWidthDp / 11

    Row(
        Modifier.padding(10.dp)
    ) {
        val measuredDivider =
            @Composable { size: Dp, times: Int -> Divider(Modifier.width(size * times)) }
        Column(
        ) {
            Image(
                painter = painterResource(id = R.drawable.att_ic), contentDescription = null,
                Modifier.size(size.dp)
            )
            measuredDivider(size.dp, 1)
            Spacer(modifier = Modifier.size(size.dp))
            measuredDivider(size.dp, 1)
            Image(
                painter = painterResource(id = R.drawable.def_ic), contentDescription = null,
                Modifier.size(size.dp)
            )
        }

        listOf(
            DamageType.SLASH,
            DamageType.PIERCE,
            DamageType.BLUNT
        ).forEach { type ->
            @DrawableRes val res = getDamageTypeIcon(type)
            val (damageCount, defencePotency) = when (type) {
                DamageType.SLASH -> state.attackByDamage.slash to state.defenceByDamage.slash
                DamageType.PIERCE -> state.attackByDamage.pierce to state.defenceByDamage.pierce
                DamageType.BLUNT -> state.attackByDamage.blunt to state.defenceByDamage.blunt
            }
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = SpaceEvenly
            ) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = damageCount.toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Image(
                    painter = painterResource(id = res),
                    contentDescription = null,
                    Modifier.size(size.dp)
                )
                Box(
                    contentAlignment = Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = defencePotency.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        listOf(
            Sin.WRATH,
            Sin.LUST,
            Sin.SLOTH,
            Sin.GLUTTONY,
            Sin.GLOOM,
            Sin.PRIDE,
            Sin.ENVY
        ).forEach { sin ->
            @DrawableRes val res = getSinIcon(sin)
            val text = when (sin) {
                Sin.WRATH -> state.attackBySin.wrath
                Sin.LUST -> state.attackBySin.lust
                Sin.SLOTH -> state.attackBySin.sloth
                Sin.GLUTTONY -> state.attackBySin.gluttony
                Sin.GLOOM -> state.attackBySin.gloom
                Sin.PRIDE -> state.attackBySin.pride
                Sin.ENVY -> state.attackBySin.envy
            }
            Column(
                horizontalAlignment = CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = text.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }
                Image(
                    painter = painterResource(id = res),
                    contentDescription = null,
                    Modifier.size(size.dp)
                )
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
        Text(fontSize = 24.sp, color = MaterialTheme.colorScheme.tertiary, text = sinner.name)
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

@Preview(
    showSystemUi = true, device = Devices.NEXUS_5
)
@Composable
private fun PartyInfoPanelPreview() {
    PartyBuildingInfoPanel(
        PartyBuildingInfoPanelState(
            AttackByDamageInfo(5, 10, 10),
            AttackBySinInfo(1, 2, 3, 5, 1, 1, 0),
            DefenceByDamageInfo(
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Good,
                InfoPanelDamageResist.Bad
            )
        ),

        )
}



