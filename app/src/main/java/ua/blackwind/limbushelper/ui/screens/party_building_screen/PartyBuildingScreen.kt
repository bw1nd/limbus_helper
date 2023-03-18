package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner

import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel

@Destination
@Composable
fun PartyBuildingScreen() {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {

            PartyBuildingScreenUi(
                party.value,
                viewModel::onIdentityClick,
                viewModel::onIdentityLongPress
            )

        }
    }
}

@Composable
fun PartyBuildingScreenUi(
    party: List<PartySinnerModel>,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit
) {
    if (party.isEmpty()) {
        Text(text = "Your party is empty.\nStart with filter screen and something here.")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(5.dp)
        ) {
            items(party.size) { index ->
                val sinner = party[index].sinner
                val identities = party[index].identities.sortedBy { it.identity.id }
                if (identities.isNotEmpty()) {
                    PartySinnerItem(
                        sinner = sinner,
                        identities = identities,
                        onIdentityItemClick,
                        onIdentityItemLongPress
                    )
                }
            }
        }
    }
}

@Composable
fun PartySinnerItem(
    sinner: Sinner, identities: List<PartyIdentity>,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit
) {
    Card(border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)) {
        Column(Modifier.padding(5.dp)) {
            Text(fontSize = 24.sp, text = sinner.name)
            Column(
                Modifier,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.End
            ) {
                identities.forEach { identity ->
                    PartyIdentityItem(
                        identity,
                        onIdentityItemClick,
                        onIdentityItemLongPress
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun PreviewPartySinnerItem() {
//    PartySinnerItem(
//        sinner = Sinner(0, "Faust"),
//        identities = listOf(previewIdentity, previewIdentity)
//    )
//}



