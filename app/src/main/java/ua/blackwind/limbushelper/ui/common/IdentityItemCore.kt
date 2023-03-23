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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinColor
import ua.blackwind.limbushelper.ui.util.previewIdentity

@Composable
fun identityItemCore(identity: Identity): @Composable() (RowScope.() -> Unit) =
    {
        //this box is image placeholder
        Box(Modifier.size(50.dp, 100.dp))
        Column(Modifier.width(290.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = identity.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.Top) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 3.dp)
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
            .width(185.dp)
            .padding(vertical = 3.dp)
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
            fontSize = 15.sp,
            modifier = Modifier.padding(vertical = 2.dp),
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
        slashRes to DamageType.SLASH, pierceRes to DamageType.PIERCE, bluntRes to DamageType.BLUNT
    )
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.width(105.dp)
    ) {
        ResistanceItem(dmgType = (res[IdentityDamageResistType.INEFFECTIVE]!!), label = "Ineff.")
        ResistanceItem(dmgType = res[IdentityDamageResistType.NORMAL]!!, label = "Normal")
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
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary)
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
    Row() {
        identityItemCore(identity = previewIdentity).invoke(this)
    }

}