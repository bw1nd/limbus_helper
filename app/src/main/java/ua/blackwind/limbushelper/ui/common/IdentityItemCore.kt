package ua.blackwind.limbushelper.ui.common

import ua.blackwind.limbushelper.R
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.ui.theme.identityRarity0
import ua.blackwind.limbushelper.ui.theme.identityRarity00
import ua.blackwind.limbushelper.ui.theme.identityRarity000
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinColor
import ua.blackwind.limbushelper.ui.util.previewIdentity

private const val ITEM_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_IMAGE_WIDTH = 70

@Composable
fun identityItemCore(
    identity: Identity,
    portraitWidthDp: Int
): @Composable (RowScope.() -> Unit) =
    {
        val density = LocalConfiguration.current.densityDpi
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(identity.imageUrl)
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
        Divider(
            color = when (identity.rarity) {
                2 -> identityRarity000
                1 -> identityRarity00
                else -> identityRarity0
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = identity.name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.Top) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 3.dp)
                ) {
                    ResistanceBlock(
                        slashRes = identity.slashRes,
                        pierceRes = identity.pierceRes,
                        bluntRes = identity.bluntRes
                    )
                    EffectsBlock(
                        effects =
                        listOf(
                            identity.firstSkill.effects,
                            identity.secondSkill.effects,
                            identity.thirdSkill.effects
                        ).flatten().toSet()
                    )
                }
                SkillBlock(
                    firstSkill = identity.firstSkill,
                    secondSkill = identity.secondSkill,
                    thirdSkill = identity.thirdSkill
                )
            }
        }
    }

@Composable
fun SkillBlock(firstSkill: Skill, secondSkill: Skill, thirdSkill: Skill) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(190.dp)
            .padding(top = 3.dp)
    ) {
        SkillItem(skill = firstSkill)
        SkillItem(skill = secondSkill)
        SkillItem(skill = thirdSkill)
    }
}

@Composable
fun SkillItem(skill: Skill) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .background(getSinColor(skill.sin))
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
        ) {
            Image(
                painter = painterResource(id = getDamageTypeIcon(skill.dmgType)),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
        }
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp),
            text = "${skill.baseDie}" +
                    (if (skill.coinBonus > 0) "+" else "-") +
                    "${skill.coinBonus}\u00D7${skill.coinCount}"
        )
    }
}

@Composable
fun ResistanceBlock(
    slashRes: IdentityDamageResistType,
    pierceRes: IdentityDamageResistType,
    bluntRes: IdentityDamageResistType
) {
    val res = mapOf(
        slashRes to DamageType.SLASH,
        pierceRes to DamageType.PIERCE,
        bluntRes to DamageType.BLUNT
    )
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.width(105.dp)
    ) {
        ResistanceItem(
            dmgType = (res[IdentityDamageResistType.INEFFECTIVE]!!),
            label = "Ineff."
        )
        ResistanceItem(
            dmgType = res[IdentityDamageResistType.NORMAL]!!,
            label = "Normal"
        )
        ResistanceItem(dmgType = res[IdentityDamageResistType.FATAL]!!, label = "Fatal")
    }
}

@Composable
fun ResistanceItem(dmgType: DamageType, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = getDamageTypeIcon(damageType = dmgType)),
            contentDescription = null,
            modifier = Modifier.size(35.dp, 35.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun EffectsBlock(effects: Set<Effect>) {
    Divider(
        thickness = 2.dp, color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .width(80.dp)
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

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun IdentityItemPreview() {
    Row {
        identityItemCore(identity = previewIdentity, 70).invoke(this)
    }

}