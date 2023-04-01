package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.util.previewIdentity

@Composable
fun PartySinnerItem(
    sinner: Sinner, identities: List<PartyIdentity>,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    onDeleteButtonClick: (Identity) -> Unit,
) {
    Column(Modifier.padding(horizontal = 5.dp)) {
        Text(fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, text = sinner.name)
        Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 2.dp)
        Spacer(modifier = Modifier.size(5.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            identities.forEach { previewIdentity ->
                PartyIdentityItem(
                    previewIdentity,
                    onIdentityItemClick,
                    onIdentityItemLongPress,
                    onDeleteButtonClick
                )
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
        {}, { _, _ -> }, {},
    )
}
