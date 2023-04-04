package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinIcon
import ua.blackwind.limbushelper.ui.util.previewIdentity


private const val ITEM_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_IMAGE_WIDTH = 70
private const val SIN_ICON_SIZE_DP = 28

@Composable
fun egoItemCore(ego: Ego, portraitWidthDp: Int): @Composable (RowScope.() -> Unit) =
    {
        val density = LocalConfiguration.current.densityDpi
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(ego.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.vroom_im)
                .size(
                    Size(
                        Dimension(PORTRAIT_IMAGE_WIDTH * density),
                        Dimension(ITEM_VERTICAL_SIZE_DP * density)
                    )
                )
                .build(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = null, modifier = Modifier
                .size(portraitWidthDp.dp, ITEM_VERTICAL_SIZE_DP.dp)
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.weight(1f))
                EgoRiskLevel(ego = ego)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkillItem(skill = ego.awakeningSkill)
                Column {
                    EgoCostBlock(ego = ego)
                    Divider(thickness = 2.dp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        EgoEffectsBlock(ego = ego)
                        EgoSanityItem(ego = ego)
                        //Divider(thickness = 2.dp)
                        EgoResistBlock(ego = ego)
                    }
                }
            }
        }
    }

@Composable
fun EgoRiskLevel(ego: Ego) {
    Box(
        Modifier
            .padding(2.dp)
            .border(2.dp, MaterialTheme.colorScheme.onPrimary, RectangleShape)
    ) {
        Text(text = ego.risk.name, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun EgoSanityItem(ego: Ego) {
    Row() {
        Text(text = ego.sanityCost.toString())
        Image(painter = painterResource(id = R.drawable.sanity_ic), contentDescription = null)
    }
}

@Composable
fun EgoEffectsBlock(ego: Ego) {
    Row() {
        ego.awakeningSkill.effects.forEach { effect ->
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
fun EgoResistBlock(ego: Ego) {
    Row() {
        ego.sinResistances.forEach { (sin, resist) ->
            EgoResistItem(sin = sin, egoSinResistType = resist)
        }
    }
}

@Composable
fun EgoResistItem(sin: Sin, egoSinResistType: EgoSinResistType) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = getSinIcon(sin)), contentDescription = null,
            modifier = Modifier.size(SIN_ICON_SIZE_DP.dp)
        )
        Text(
            text =
            when (egoSinResistType) {
                EgoSinResistType.INEFFECTIVE -> stringResource(id = R.string.res_ineff)
                EgoSinResistType.ENDURE -> stringResource(id = R.string.res_endure)
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        ego.resourceCost.forEach { (sin, cost) ->
            EgoCostItem(sin = sin, cost = cost)
        }
    }
}

@Composable
fun EgoCostItem(sin: Sin, cost: Int) {
    Row() {
        Image(
            painter = painterResource(id = getSinIcon(sin)),
            contentDescription = null,
            modifier = Modifier.size(SIN_ICON_SIZE_DP.dp)
        )
        Text(
            text = "X ${if (cost != 0) cost else "Error"}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PreviewEgoItemCore() {
    Row() {
        egoItemCore(
            ego =
            Ego(
                0,
                "Test Ego",
                0,
                RiskLevel.ALEPH,
                previewIdentity.firstSkill,
                previewIdentity.secondSkill,
                previewIdentity.passive,
                mapOf(Sin.LUST to 3),
                20,
                mapOf(Sin.ENVY to EgoSinResistType.ENDURE),
                ""
            ), portraitWidthDp = PORTRAIT_IMAGE_WIDTH
        ).invoke(this)
    }

}