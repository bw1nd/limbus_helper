package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.common.identityItemCore
import ua.blackwind.limbushelper.ui.theme.activeIdentityBorderColor
import ua.blackwind.limbushelper.ui.theme.inactiveIdentityBorderColor
import ua.blackwind.limbushelper.ui.util.previewIdentity

private const val REMOVE_FROM_PARTY_BANNER_SIZE_DP = 34

@Composable
fun PartyIdentityItem(
    viewIdentity: PartyIdentity,
    onClick: (Int, Int) -> Unit,
    onDeleteButtonClick: (Identity) -> Unit
) {
    Card(
        border = BorderStroke(
            2.dp,
            if (viewIdentity.isActive) activeIdentityBorderColor
            else inactiveIdentityBorderColor
        ),
        shape = CutCornerShape(topStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth(
                if (viewIdentity.isActive) 1f
                else .98f
            )
            .clickable { onClick(viewIdentity.identity.id, viewIdentity.identity.sinnerId) }
    ) {
        val identity = viewIdentity.identity
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(content = identityItemCore(identity))
                Spacer(Modifier.weight(1f))
            }
            RemoveFromPartyButton(
                size = REMOVE_FROM_PARTY_BANNER_SIZE_DP.dp,
                onClick = { onDeleteButtonClick(identity) },
                identityIsActive = viewIdentity.isActive,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Preview
@Composable
fun PreviewPartyIdentityItem() {
    Column {
        PartyIdentityItem(
            viewIdentity = PartyIdentity(previewIdentity, true),
            { _, _ -> }, {})
        PartyIdentityItem(
            viewIdentity = PartyIdentity(previewIdentity, false),
            { _, _ -> }, {})
    }
}