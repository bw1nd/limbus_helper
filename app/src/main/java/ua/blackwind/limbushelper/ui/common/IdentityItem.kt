package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
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
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.ui.previewIdentity

@Composable
fun IdentityItem(identity: Identity) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.size(width = 380.dp, height = 100.dp)
    ) {
        Row() {
            //this box is image placeholder
            Box(Modifier.size(50.dp, 100.dp))
            Column(Modifier.width(290.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = identity.name, fontSize = 20.sp)
                    Text(
                        text = "0".repeat(identity.rarity),
                        fontSize = 22.sp
                    )
                }
                Row() {
                    Row() {
                        ResistanceBlock(
                            slashRes = identity.slashRes,
                            pierceRes = identity.pierceRes,
                            bluntRes = identity.bluntRes
                        )
                        SkillBlock(
                            firstSkill = identity.firstSkill,
                            secondSkill = identity.secondSkill,
                            thirdSkill = identity.thirdSkill
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkillBlock(firstSkill: Skill, secondSkill: Skill, thirdSkill: Skill) {
    Row() {
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
            .size(55.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = painterResource(id = chooseDamageTypeIcon(skill.dmgType)),
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
    Row {
        ResistanceItem(dmgType = (res[IdentityDamageResistType.INEFFECTIVE]!!), label = "Ineff.")
        ResistanceItem(dmgType = res[IdentityDamageResistType.NORMAL]!!, label = "Normal")
        ResistanceItem(dmgType = res[IdentityDamageResistType.FATAL]!!, label = "Fatal")
    }
}

@Composable
fun ResistanceItem(dmgType: DamageType, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = chooseDamageTypeIcon(damageType = dmgType)),
            contentDescription = null,
            modifier = Modifier.size(40.dp, 40.dp)
        )
        Text(text = label, fontSize = 12.sp)
    }
}

private fun chooseDamageTypeIcon(damageType: DamageType): Int {
    return when (damageType) {
        DamageType.SLASH -> R.drawable.slash_ic
        DamageType.PIERCE -> R.drawable.pierce_ic
        DamageType.BLUNT -> R.drawable.blunt_ic
    }
}

@Preview
@Composable
private fun IdentityItemPreview() {
    IdentityItem(
        identity = previewIdentity
    )
}