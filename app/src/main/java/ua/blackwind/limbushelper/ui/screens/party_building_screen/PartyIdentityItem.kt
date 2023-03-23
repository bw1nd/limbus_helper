package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.ui.common.identityItemCore
import ua.blackwind.limbushelper.ui.theme.activeIdentityBorderColor
import ua.blackwind.limbushelper.ui.theme.inactiveIdentityBorderColor
import ua.blackwind.limbushelper.ui.util.previewIdentity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PartyIdentityItem(
    viewIdentity: PartyIdentity,
    onClick: (Int) -> Unit,
    onLongClick: (Int, Int) -> Unit
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
            .width(
                if (viewIdentity.isActive) ACTIVE_ITEM_WIDTH_DP.dp
                else NON_ACTIVE_ITEM_WIDTH_DP.dp
            )
            .combinedClickable(
                enabled = true,
                onClick = { onClick(viewIdentity.identity.id) },
                onLongClick = {
                    onLongClick(
                        viewIdentity.identity.id,
                        viewIdentity.identity.sinnerId
                    )
                }
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
        PartyIdentityItem(
            viewIdentity = PartyIdentity(previewIdentity, true),
            {},
            { _, _ -> })
        PartyIdentityItem(
            viewIdentity = PartyIdentity(previewIdentity, false),
            {}, { _, _ -> })
    }
}

private const val ACTIVE_ITEM_WIDTH_DP = 380
private const val NON_ACTIVE_ITEM_WIDTH_DP = 360