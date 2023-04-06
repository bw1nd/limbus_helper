package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartySinnerModel
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinColor

@Composable
fun PartySinnerItem(
    sinner: PartySinnerModel,
    showInactive: Boolean,
    onIdentityItemClick: (Int) -> Unit,
    onIdentityItemLongPress: (Int, Int) -> Unit,
    onIdentityDeleteButtonClick: (Identity) -> Unit,
    onEgoDeleteButtonClick: (Ego) -> Unit
) {
    val mode = rememberSaveable() {
        mutableStateOf<RiskLevel?>(null)
    }
    Column(Modifier.padding(horizontal = 5.dp)) {
        Row {
            Text(
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = sinner.sinner.name
            )
            Spacer(modifier = Modifier.weight(1f))
            SinnerEgoBlock(
                sinner.egos
            ) { risk -> mode.value = if (mode.value != risk) risk else null }
        }
        Divider(color = MaterialTheme.colorScheme.tertiary, thickness = 2.dp)
        Spacer(modifier = Modifier.size(5.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            mode.value?.let { risk ->
                val ego = sinner.egos.find { it.risk == risk }
                if (ego == null) {
                    mode.value = null
                } else {
                    PartyEgoItem(ego = ego, onDeleteButtonClick = onEgoDeleteButtonClick)
                }
            }
            sinner.identities.forEach { previewIdentity ->
                if (showInactive || previewIdentity.isActive) {
                    PartyIdentityItem(
                        previewIdentity,
                        onIdentityItemClick,
                        onIdentityItemLongPress,
                        onIdentityDeleteButtonClick
                    )
                }
            }
        }
    }
}

@Composable
fun SinnerEgoBlock(egos: List<Ego>, onItemClick: (RiskLevel) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        RiskLevel.values().forEach { risk ->
            SinnerEgoBadge(
                risk = risk,
                ego = egos.find { it.risk == risk },
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun SinnerEgoBadge(risk: RiskLevel, ego: Ego?, onClick: (RiskLevel) -> Unit) {
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
                .clickable { onClick(risk) }
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