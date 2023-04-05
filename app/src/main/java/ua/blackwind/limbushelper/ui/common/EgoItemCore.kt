package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.EgoSkill
import ua.blackwind.limbushelper.ui.screens.filter_screen.FilterListItem
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterDataModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterItemTypeModel
import ua.blackwind.limbushelper.ui.theme.*
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinIcon
import ua.blackwind.limbushelper.ui.util.previewIdentity

private const val ITEM_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_IMAGE_WIDTH = 70
private const val SIN_ICON_SIZE_DP = 26

@Composable
fun egoItemCore(ego: Ego, portraitWidthDp: Int): @Composable (RowScope.() -> Unit) =
    {
        val density = LocalConfiguration.current.densityDpi
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(ego.imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.vroom_im)
                    .size(
                        Size(
                            Dimension(PORTRAIT_IMAGE_WIDTH * density),
                            Dimension(PORTRAIT_VERTICAL_SIZE_DP * density)
                        )
                    )
                    .build(),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                contentDescription = null, modifier = Modifier
                    .size(portraitWidthDp.dp, PORTRAIT_VERTICAL_SIZE_DP.dp)
            )
            EgoRiskLevel(ego = ego)
        }
        Divider(
            color = when (ego.risk) {
                RiskLevel.ZAYIN -> zayin
                RiskLevel.TETH -> teth
                RiskLevel.HE -> he
                RiskLevel.WAW -> waw
                RiskLevel.ALEPH -> aleph
            },
            modifier = Modifier
                .size(4.dp, ITEM_VERTICAL_SIZE_DP.dp)
        )
        Column(
            Modifier
                .width(265.dp)
                .padding(start = 5.dp)
                .padding(vertical = 4.dp)
        ) {
            Row() {
                Text(
                    text = ego.name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.width(60.dp)) {
                    EgoSkillItem(skill = ego.awakeningSkill)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    EgoCostBlock(ego = ego)
                    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            EgoSanityItem(ego = ego)
                            EgoEffectsBlock(ego.awakeningSkill.effects.toSet())
                        }
                        EgoResistBlock(ego = ego)
                    }
                }
            }
        }
    }

@Composable
fun EgoEffectsBlock(effects: Set<Effect>) {
    Divider(
        thickness = 2.dp, color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .width(50.dp)
            .padding(top = 2.dp)
    )
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(top = 4.dp)
    ) {
        for (effect in effects.toSet()) {
            Image(
                painter = painterResource(id = getEffectIcon(effect)),
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp)
            )
        }
    }
}

@Composable
fun EgoRiskLevel(ego: Ego) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            //.padding(top = 5.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .width(PORTRAIT_IMAGE_WIDTH.dp)
    ) {
        Text(
            text = ego.risk.name,
            textAlign = TextAlign.Center,
            color = when (ego.risk) {
                RiskLevel.ZAYIN -> zayin
                RiskLevel.TETH -> teth
                RiskLevel.HE -> he
                RiskLevel.WAW -> waw
                RiskLevel.ALEPH -> aleph
            }
        )
    }
}

@Composable
fun EgoSanityItem(ego: Ego) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = ego.sanityCost.toString(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Image(
            painter = painterResource(id = R.drawable.sanity_ic), contentDescription = null,
            Modifier.size((SIN_ICON_SIZE_DP - 6).dp)
        )
    }
}

@Composable
fun EgoResistBlock(ego: Ego) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        ego.sinResistances.forEach { (sin, resist) ->
            EgoResistItem(sin = sin, egoSinResistType = resist)
        }
    }
}

@Composable
fun EgoResistItem(sin: Sin, egoSinResistType: EgoSinResistType) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 2.dp)
            .width(28.dp)
    ) {
        Image(
            painter = painterResource(id = getSinIcon(sin)), contentDescription = null,
            modifier = Modifier.size(SIN_ICON_SIZE_DP.dp)
        )
        Text(
            text =
            when (egoSinResistType) {
                EgoSinResistType.INEFF -> stringResource(id = R.string.res_ineff)
                EgoSinResistType.ENDURE -> stringResource(id = R.string.res_endure).dropLast(3) + "."
                EgoSinResistType.FATAL -> stringResource(id = R.string.res_fatal)
                EgoSinResistType.NORMAL -> throw IllegalArgumentException("Ego Card should not have info on Normal resistances.")
            },
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun EgoCostBlock(ego: Ego) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(bottom = 2.dp)
            .fillMaxWidth()
    ) {
        ego.resourceCost.forEach { (sin, cost) ->
            EgoCostItem(sin = sin, cost = cost)
        }
    }
}

@Composable
fun EgoCostItem(sin: Sin, cost: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = getSinIcon(sin)),
            contentDescription = null,
            modifier = Modifier.size(SIN_ICON_SIZE_DP.dp)
        )
        Text(
            text = "\u00D7${if (cost != 0) cost else "Error"}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    widthDp = 400,
    heightDp = 100,
    backgroundColor = 0xFF1E1E1E
)
@Composable
private fun PreviewEgoItemCore() {
    Box(modifier = Modifier.size(400.dp, 100.dp)) {
        FilterListItem(listItem = FilterDataModel(
            FilterItemTypeModel.EgoType(
                Ego(
                    0,
                    "Test Ego",
                    0,
                    RiskLevel.ALEPH,
                    EgoSkill(0, "", DamageType.BLUNT, Sin.LUST, 30, 15, 10, 1, 20, emptyList()),
                    EgoSkill(0, "", DamageType.BLUNT, Sin.LUST, 30, 15, 10, 1, 20, emptyList()),
                    previewIdentity.passive,
                    mapOf(
                        Sin.LUST to 3,
                        Sin.PRIDE to 3,
                        Sin.GLOOM to 1,
                        Sin.WRATH to 2,
                        Sin.ENVY to 5
                    ),
                    20,
                    mapOf(
                        Sin.ENVY to EgoSinResistType.ENDURE,
                        Sin.GLUTTONY to EgoSinResistType.FATAL,
                        Sin.WRATH to EgoSinResistType.FATAL,
                        Sin.GLOOM to EgoSinResistType.INEFF
                    ),
                    ""
                )
            ), true
        ), {}, {}
        )
    }

}
