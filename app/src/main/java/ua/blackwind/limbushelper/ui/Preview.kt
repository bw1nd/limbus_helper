package ua.blackwind.limbushelper.ui

import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity

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
    speed = Pair(3, 6),
    firstSkillId = 0,
    secondSkillId = 0,
    thirdSkillId = 0,
    passiveId = 0,
    supportId = 0,
    imageUrl = ""
)