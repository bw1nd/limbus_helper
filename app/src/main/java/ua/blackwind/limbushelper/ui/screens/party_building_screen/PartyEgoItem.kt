package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.ui.common.egoItemCore

@Composable
fun PartyEgoItem(
    ego: Ego,
    onDeleteButtonClick: (Ego) -> Unit
) {
    Card(
        border = BorderStroke(
            2.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = CutCornerShape(topStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(content = egoItemCore(ego = ego))
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
}