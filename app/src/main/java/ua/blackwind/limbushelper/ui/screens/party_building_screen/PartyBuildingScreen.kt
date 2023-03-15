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
import ua.blackwind.limbushelper.domain.sinner.model.Sinner

import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyIdentityModel
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel

@Destination
@Composable
fun PartyBuildingScreen() {
    val viewModel = hiltViewModel<PartyBuildingScreenViewModel>()
    val party = viewModel.party.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {

            PartyBuildingScreenUi(party.value)

        }
    }
}

@Composable
fun PartyBuildingScreenUi(party: List<PartySinnerModel>) {
    if (party.isEmpty()) {
        Text(text = "Your party is empty.\nStart with filter screen and something here.")
    } else {
        LazyColumn() {
            items(party.size) { index ->
                val sinner = party[index].sinner
                val identities = party[index].identities
                if (identities.isNotEmpty()) {
                    PartySinnerItem(sinner = sinner, identities = identities)
                }
            }
        }
    }
}

@Composable
fun PartySinnerItem(sinner: Sinner, identities: List<PartyIdentityModel>) {
    Card(border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)) {
        Column(Modifier.padding(5.dp)) {
            Text(fontSize = 24.sp, text = sinner.name)
            Column(
                Modifier,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                identities.forEach { identity -> PartyIdentityItem(identity) }
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



