package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinColor

@Composable
fun PartySinnerItem(
    sinner: Sinner,
    identities: List<PartyIdentity>,
    egos: List<Ego>,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    onDeleteButtonClick: (Identity) -> Unit,
) {
    Column(Modifier.padding(horizontal = 5.dp)) {
        Row {
            Text(
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = sinner.name
            )
            Spacer(modifier = Modifier.weight(1f))
            SinnerEgoBlock(egos)
        }
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

@Composable
fun SinnerEgoBlock(egos: List<Ego>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        RiskLevel.values().forEach { risk ->
            SinnerEgoBadge(
                risk = risk,
                ego = egos.find { it.risk == risk })
        }
    }
}

@Composable
fun SinnerEgoBadge(risk: RiskLevel, ego: Ego?) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(46.dp, 20.dp)
                .background(
                    color = if (ego == null) MaterialTheme.colorScheme.primary else getSinColor(
                        ego.awakeningSkill.sin
                    )
                )
                .border(2.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        )
        if (ego == null) {
            Text(
                text = risk.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Image(
                painter = painterResource(id = getDamageTypeIcon(ego.awakeningSkill.dmgType)),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}