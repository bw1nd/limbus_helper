package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.common.AlternativeCheckbox
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.InfoPanelDamageResist
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.PartyBuildingInfoPanelState
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinIcon

private const val PARTY_SIZE = 5
private const val INFO_PANEL_COLUMNS_PLUS_ONE = 12

@Composable
fun PartyBuildingInfoPanel(
    state: PartyBuildingInfoPanelState,
    isShowActiveIdentitiesChecked: Boolean,
    onShowActiveIdentitiesClick: (Boolean) -> Unit,
    onClearPartyClick: () -> Unit
) {
    val columnWidth = LocalConfiguration.current.screenWidthDp / INFO_PANEL_COLUMNS_PLUS_ONE
    val (partySizeCheckerColor, partySizeCheckerSign) = when (true) {
        (state.activeIdentityCount < PARTY_SIZE) -> Color.Yellow to "<"
        (state.activeIdentityCount == PARTY_SIZE) -> Color.Green to "="
        else -> Color.Red to ">"
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Start)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.party_size_indicator_ic),
                contentDescription = null,
                tint = partySizeCheckerColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "$partySizeCheckerSign $PARTY_SIZE",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "(${state.totalIdentityCount})",
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.7f
                ),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Clear Party",
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.clickable { onClearPartyClick() }
            )
            Spacer(modifier = Modifier.width(20.dp))
            AlternativeCheckbox(
                checked = isShowActiveIdentitiesChecked,
                onCheckedChange = onShowActiveIdentitiesClick,
                modifier = Modifier.requiredSize(18.dp)
            )
            Text(
                stringResource(R.string.active_only),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoPanelTypeIconsBlock(columnWidth)
            Spacer(modifier = Modifier.width(5.dp))
            InfoPanelDamageItem(state, columnWidth)
            Spacer(modifier = Modifier.width(5.dp))
            InfoPanelSinItem(state, columnWidth)
        }
    }
}

@Composable
private fun InfoPanelSinItem(
    state: PartyBuildingInfoPanelState,
    columnWidth: Int
) {
    Sin.values().forEach { sin ->
        @DrawableRes val res = getSinIcon(sin)
        val (text, resistPotency) = when (sin) {
            Sin.WRATH -> state.attackBySin.wrath to state.resistBySin.wrath
            Sin.LUST -> state.attackBySin.lust to state.resistBySin.lust
            Sin.SLOTH -> state.attackBySin.sloth to state.resistBySin.sloth
            Sin.GLUTTONY -> state.attackBySin.gluttony to state.resistBySin.gluttony
            Sin.GLOOM -> state.attackBySin.gloom to state.resistBySin.gloom
            Sin.PRIDE -> state.attackBySin.pride to state.resistBySin.pride
            Sin.ENVY -> state.attackBySin.envy to state.resistBySin.envy
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(columnWidth.dp)
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
                Modifier.size(columnWidth.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(columnWidth.dp)
            ) {
                //TODO make custom colors for these statuses
                val (iconResId, color) = when (resistPotency) {
                    InfoPanelDamageResist.NA -> 0 to Color.Transparent
                    InfoPanelDamageResist.Bad -> R.drawable.info_warning_ic to Color.Red
                    InfoPanelDamageResist.Poor -> R.drawable.info_down_ic to Color.Yellow
                    InfoPanelDamageResist.Normal -> R.drawable.info_normal_ic to Color.Cyan
                    InfoPanelDamageResist.Good -> R.drawable.info_up_ic to Color.Green
                    InfoPanelDamageResist.Perfect -> R.drawable.info_perfect_ic to Color.Green
                }
                if (iconResId != 0) {
                    Icon(
                        painterResource(id = iconResId),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(columnWidth.dp * 0.6f)
                    )
                }

            }
        }
    }
}


@Composable
private fun InfoPanelDamageItem(
    state: PartyBuildingInfoPanelState,
    columnWidth: Int
) {
    DamageType.values().forEach { type ->
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
                modifier = Modifier.size(columnWidth.dp)
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
                Modifier.size(columnWidth.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(columnWidth.dp)
            ) {
                //TODO make custom colors for these statuses
                val (iconResId, color) = when (defencePotency) {
                    InfoPanelDamageResist.NA -> 0 to Color.Transparent
                    InfoPanelDamageResist.Bad -> R.drawable.info_warning_ic to Color.Red
                    InfoPanelDamageResist.Poor -> R.drawable.info_down_ic to Color.Yellow
                    InfoPanelDamageResist.Normal -> R.drawable.info_normal_ic to Color.Cyan
                    InfoPanelDamageResist.Good -> R.drawable.info_up_ic to Color.Green
                    InfoPanelDamageResist.Perfect -> R.drawable.info_perfect_ic to Color.Green
                }
                if (iconResId != 0) {
                    Icon(
                        painterResource(id = iconResId),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(columnWidth.dp * 0.6f)
                    )
                }

            }
        }
    }
}

@Composable
private fun InfoPanelTypeIconsBlock(columnWidth: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(columnWidth.dp * 0.8f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.att_ic),
            contentDescription = null,
            Modifier.size(columnWidth.dp)
        )
        Divider(
            thickness = 3.dp, modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.def_ic),
            contentDescription = null,
            Modifier.size(columnWidth.dp)
        )
    }
}

//    @Preview(
//        showSystemUi = true,
//        showBackground = true,
//        uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED,
//        device = Devices.NEXUS_5
//    )
//    @Composable
//    private fun PartyInfoPanelPreview() {
//        PartyBuildingInfoPanel(
//            PartyBuildingInfoPanelState(
//                AttackByDamageInfo(5, 10, 10),
//                AttackBySinInfo(1, 2, 3, 5, 1, 1, 0),
//                DefenceByDamageInfo(
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Good,
//                    InfoPanelDamageResist.Bad
//                ),
//                ResistBySinInfo(
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                    InfoPanelDamageResist.Normal,
//                ), 4, 8
//            ), true, {}
//        )
//    }