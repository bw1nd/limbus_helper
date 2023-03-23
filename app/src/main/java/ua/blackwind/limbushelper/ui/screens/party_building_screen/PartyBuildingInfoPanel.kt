package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.*
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinIcon

@Composable
fun PartyBuildingInfoPanel(state: PartyBuildingInfoPanelState) {
    val size = LocalConfiguration.current.screenWidthDp / 11

    Row(
        Modifier.padding(10.dp)
    ) {
        val measuredDivider =
            @Composable { size: Dp, times: Int -> Divider(Modifier.width(size * times)) }
        Column(
        ) {
            Image(
                painter = painterResource(id = R.drawable.att_ic), contentDescription = null,
                Modifier.size(size.dp)
            )
            measuredDivider(size.dp, 1)
            Spacer(modifier = Modifier.size(size.dp))
            measuredDivider(size.dp, 1)
            Image(
                painter = painterResource(id = R.drawable.def_ic), contentDescription = null,
                Modifier.size(size.dp)
            )
        }

        listOf(
            DamageType.SLASH,
            DamageType.PIERCE,
            DamageType.BLUNT
        ).forEach { type ->
            @DrawableRes val res = getDamageTypeIcon(type)
            val (damageCount, defencePotency) = when (type) {
                DamageType.SLASH -> state.attackByDamage.slash to state.defenceByDamage.slash
                DamageType.PIERCE -> state.attackByDamage.pierce to state.defenceByDamage.pierce
                DamageType.BLUNT -> state.attackByDamage.blunt to state.defenceByDamage.blunt
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = damageCount.toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Image(
                    painter = painterResource(id = res),
                    contentDescription = null,
                    Modifier.size(size.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = defencePotency.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        listOf(
            Sin.WRATH,
            Sin.LUST,
            Sin.SLOTH,
            Sin.GLUTTONY,
            Sin.GLOOM,
            Sin.PRIDE,
            Sin.ENVY
        ).forEach { sin ->
            @DrawableRes val res = getSinIcon(sin)
            val text = when (sin) {
                Sin.WRATH -> state.attackBySin.wrath
                Sin.LUST -> state.attackBySin.lust
                Sin.SLOTH -> state.attackBySin.sloth
                Sin.GLUTTONY -> state.attackBySin.gluttony
                Sin.GLOOM -> state.attackBySin.gloom
                Sin.PRIDE -> state.attackBySin.pride
                Sin.ENVY -> state.attackBySin.envy
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(size.dp)
                ) {
                    Text(
                        text = text.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }
                Image(
                    painter = painterResource(id = res),
                    contentDescription = null,
                    Modifier.size(size.dp)
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true, device = Devices.NEXUS_5
)
@Composable
private fun PartyInfoPanelPreview() {
    PartyBuildingInfoPanel(
        PartyBuildingInfoPanelState(
            AttackByDamageInfo(5, 10, 10),
            AttackBySinInfo(1, 2, 3, 5, 1, 1, 0),
            DefenceByDamageInfo(
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Good,
                InfoPanelDamageResist.Bad
            )
        ),

        )
}