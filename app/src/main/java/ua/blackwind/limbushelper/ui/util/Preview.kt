package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.*

val previewIdentity = Identity(
    id = 0,
    name = "W Corp. Cleanup Agent",
    sinnerId = 0,
    rarity = 2,
    slashRes = IdentityDamageResistType.NORMAL,
    pierceRes = IdentityDamageResistType.INEFF,
    bluntRes = IdentityDamageResistType.FATAL,
    maxHp = 146,
    maxArmor = 38,
    maxDamage = 30,
    speed = 3 to 6,
    firstSkill = Skill(
        0,
        "Rip",
        DamageType.SLASH,
        Sin.GLOOM,
        3,
        3,
        2,
        2,
        listOf(Effect.BLEED, Effect.BURN, Effect.POISE, Effect.ATT_DOWN, Effect.HASTE)
    ),
    secondSkill = Skill(0, "Rip", DamageType.PIERCE, Sin.WRATH, 3, 3, 2, 2, emptyList()),
    thirdSkill = Skill(0, "Rip", DamageType.BLUNT, Sin.SLOTH, 3, 3, 15, 2, emptyList()),
    defenceSkill = DefenceSkill(0, "Guard", DefenceSkillType.GUARD, 2, 10, 1, null, 30),
    passive = Passive(0, 0, SinCost(emptyList()), ""),
    support = Support(0, 0, SinCost(emptyList()), ""),
    imageUrl = ""
)