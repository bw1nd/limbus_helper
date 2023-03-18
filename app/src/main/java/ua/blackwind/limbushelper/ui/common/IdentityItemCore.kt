package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.ui.previewIdentity
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import ua.blackwind.limbushelper.ui.util.getSinColor

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
                Text(text = identity.name, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(Modifier.padding(bottom = 5.dp)) {
                ResistanceBlock(
                    slashRes = identity.slashRes,
                    pierceRes = identity.pierceRes,
                    bluntRes = identity.bluntRes
                )
                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(2.dp, 45.dp)
                )
                SkillBlock(
                    firstSkill = identity.firstSkill,
                    secondSkill = identity.secondSkill,
                    thirdSkill = identity.thirdSkill
                )
            }
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
            EffectsBlock(
                effects = listOf(
                    identity.firstSkill.effects,
                    identity.secondSkill.effects,
                    identity.thirdSkill.effects
                ).flatten()
            )
        }
    }

@Composable
fun SkillBlock(firstSkill: Skill, secondSkill: Skill, thirdSkill: Skill) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(185.dp)
    ) {
        SkillItem(skill = firstSkill)
        SkillItem(skill = secondSkill)
        SkillItem(skill = thirdSkill)
    }
}

@Composable
fun SkillItem(skill: Skill) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(getSinColor(skill.sin))
    ) {
        Image(
            painter = painterResource(id = getDamageTypeIcon(skill.dmgType)),
            contentDescription = null,
            modifier = Modifier.size(40.dp)

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
        verticalAlignment = Alignment.CenterVertically,
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
        Text(text = label, fontSize = 10.sp)
    }
}

@Composable
fun EffectsBlock(effects: List<Effect>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 2.dp, bottom = 5.dp)
    ) {
        for (effect in effects) {
            Image(
                painter = painterResource(id = getEffectIcon(effect)), contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun IdentityItemPreview() {
    identityItemCore(identity = previewIdentity)
}