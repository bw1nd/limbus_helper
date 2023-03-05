package ua.blackwind.limbushelper.ui

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.*

val previewIdentity = Identity(
    id = 0,
    name = "W Corp. Cleanup Agent",
    sinnerId = 0,
    rarity = 2,
    slashRes = IdentityDamageResistType.NORMAL,
    pierceRes = IdentityDamageResistType.INEFFECTIVE,
    bluntRes = IdentityDamageResistType.FATAL,
    maxHp = 146,
    maxArmor = 38,
    maxDamage = 30,
    speed = 3 to 6,
    firstSkill = Skill(
        0,
        "Rip",
        0,
        DamageType.SLASH,
        Sin.SLOTH,
        3,
        3,
        2,
        2,
        listOf(Effect.BLEED, Effect.BURN)
    ),
    secondSkill = Skill(0, "Rip", 0, DamageType.PIERCE, Sin.SLOTH, 3, 3, 2, 2, emptyList()),
    thirdSkill = Skill(0, "Rip", 0, DamageType.BLUNT, Sin.SLOTH, 3, 3, 2, 2, emptyList()),
    passive = Passive(0, 0, SinCost(emptyList()), ""),
    support = Support(0, 0, SinCost(emptyList()), ""),
    imageUrl = ""
)