package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.common.IdentityItem
import ua.blackwind.limbushelper.ui.previewIdentity

@Destination
@Composable
fun PartyBuildingScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            PartyBuildingScreenUi()
        }
    }
}

@Composable
fun PartyBuildingScreenUi() {

}

@Composable
fun PartySinnerItem(sinner: Sinner, identities: List<Identity>) {
    Card(border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)) {
        Column(Modifier.padding(5.dp)) {
            Text(fontSize = 24.sp, text = sinner.name)
            Column(
                Modifier,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                identities.forEach { identity -> IdentityItem(identity = identity) }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPartySinnerItem() {
    PartySinnerItem(
        sinner = Sinner(0, "Faust"),
        identities = listOf(previewIdentity, previewIdentity)
    )
}



