package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.common.identityItemCore
import ua.blackwind.limbushelper.ui.previewIdentity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyIdentityModel

@Composable
fun PartyIdentityItem(viewIdentity: PartyIdentityModel) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .width(
                if (viewIdentity.isActive) ACTIVE_ITEM_WIDTH_DP.dp
                else NON_ACTIVE_ITEM_WIDTH_DP.dp
            )
    ) {
        val identity = viewIdentity.identity
        Row(content = identityItemCore(identity))
    }
}

@Preview
@Composable
fun PreviewPartyIdentityItem() {
    Column() {
        PartyIdentityItem(viewIdentity = PartyIdentityModel(previewIdentity, true))
        PartyIdentityItem(viewIdentity = PartyIdentityModel(previewIdentity, false))
    }
}

private const val ACTIVE_ITEM_WIDTH_DP = 380
private const val NON_ACTIVE_ITEM_WIDTH_DP = 360