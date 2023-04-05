package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.ui.common.egoItemCore

private const val PORTRAIT_WIDTH_DP = 70

@Composable
fun PartyEgoItem(
    ego: Ego,
    onDeleteButtonClick: (Ego) -> Unit
) {
    Row() {
        Row(content = egoItemCore(ego = ego, portraitWidthDp = PORTRAIT_WIDTH_DP))
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.delete_ic),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(30.dp)
                .clickable { onDeleteButtonClick(ego) }
        )
        Spacer(Modifier.weight(1f))
    }

}