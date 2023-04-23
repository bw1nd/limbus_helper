package ua.blackwind.limbushelper.ui.common

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
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.*
import ua.blackwind.limbushelper.ui.theme.identityRarity0
import ua.blackwind.limbushelper.ui.theme.identityRarity00
import ua.blackwind.limbushelper.ui.theme.identityRarity000
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinColor
import ua.blackwind.limbushelper.ui.util.previewIdentity

private const val ITEM_VERTICAL_SIZE_DP = 100
private const val PORTRAIT_IMAGE_WIDTH = 70
private const val SKILL_IMAGE_SIZE_DP = 52

@Composable
fun identityItemCore(
    identity: Identity
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
                .size(PORTRAIT_IMAGE_WIDTH.dp, ITEM_VERTICAL_SIZE_DP.dp)
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
                .padding(start = 3.dp)
                .padding(vertical = 2.dp)
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
            Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 3.dp)
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
                    thirdSkill = identity.thirdSkill,
                    defenseSkill = identity.defenseSkill
                )
            }
        }
    }

@Composable
fun SkillBlock(
    firstSkill: Skill,
    secondSkill: Skill,
    thirdSkill: Skill,
    defenseSkill: DefenseSkill
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(200.dp)
            .padding(top = 3.dp)
    ) {
        SkillItem(skill = firstSkill)
        SkillItem(skill = secondSkill)
        SkillItem(skill = thirdSkill)
        DefenceSkillItem(item = defenseSkill)
    }
}

@Composable
fun SkillItem(skill: Skill) {
    SkillItemCore(
        dmgType = skill.dmgType,
        sin = skill.sin,
        baseDie = skill.basePower,
        coinBonus = skill.coinPower,
        coinCount = skill.coinCount
    )
}

@Composable
fun DefenceSkillItem(item: DefenseSkill) {
    Column(
        modifier = Modifier
            .height(SKILL_IMAGE_SIZE_DP.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(40.dp)
                .padding(2.dp)
                .border(2.dp, color = MaterialTheme.colorScheme.onPrimary)
                .background(color = item.sin?.let { sin -> getSinColor(sin) }
                    ?: MaterialTheme.colorScheme.primary)
        ) {
            Image(
                painter = painterResource(
                    id =
                    when (item.type) {
                        DefenseSkillType.GUARD -> R.drawable.ego_resist_ic
                        DefenseSkillType.EVADE -> R.drawable.evade_50_ic
                        DefenseSkillType.COUNTER -> R.drawable.counter_50_ic
                    }
                ), contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun EgoSkillItem(skill: EgoSkill) {
    SkillItemCore(
        dmgType = skill.dmgType,
        sin = skill.sin,
        baseDie = skill.basePower,
        coinBonus = skill.coinPower,
        coinCount = skill.coinCount
    )
}

@Composable
fun SkillItemCore(
    dmgType: DamageType,
    sin: Sin,
    baseDie: Int,
    coinBonus: Int,
    coinCount: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(SKILL_IMAGE_SIZE_DP.dp)
                .background(getSinColor(sin))
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
        ) {
            Image(
                painter = painterResource(id = getDamageTypeIcon(dmgType)),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
        }
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp),
            text = "$baseDie" +
                    (if (coinBonus > 0) "+" else "") +
                    "${coinBonus}\u00D7${coinCount}"
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
    ) {
        ResistanceItem(
            dmgType = (res[IdentityDamageResistType.INEFF]!!),
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
        identityItemCore(identity = previewIdentity).invoke(this)
    }

}

@Preview
@Composable
fun SkillBlockPreview() {
    SkillBlock(
        firstSkill = previewIdentity.firstSkill,
        secondSkill = previewIdentity.secondSkill,
        thirdSkill = previewIdentity.thirdSkill,
        defenseSkill = previewIdentity.defenseSkill
    )
}