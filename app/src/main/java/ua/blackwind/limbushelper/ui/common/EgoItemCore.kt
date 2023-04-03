package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import ua.blackwind.limbushelper.ui.util.previewIdentity


private const val ITEM_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_IMAGE_WIDTH = 70

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ego.name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            SkillItem(skill = ego.awakeningSkill)
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