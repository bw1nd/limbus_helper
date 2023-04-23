package util

import io.mockk.mockk
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.domain.sinner.model.*

class TestEntitiesCreator {

    private val dummyPassive = mockk<Passive>()

    private val dummySupport = mockk<Support>()

    val firstIdentity = generateIdentity(
        name = "TEST IDENTITY #1",
        sinnerId = 1,
        slashResist = IdentityDamageResistType.INEFF,
        pierceResist = IdentityDamageResistType.NORMAL,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.LUST, listOf(Effect.BURN)),
        secondSkill = generateSkill(
            DamageType.SLASH,
            Sin.ENVY,
            listOf(Effect.BLEED, Effect.PARALYSIS)
        ),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.WRATH, emptyList()),
        defenseSkill = DefenseSkill(0, "Guard", DefenseSkillType.GUARD, 0, 0, 0, null),
        passive = dummyPassive,
        support = dummySupport
    )
    val secondIdentity = generateIdentity(
        name = "TEST IDENTITY #2",
        sinnerId = 2,
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFF,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.PIERCE, Sin.PRIDE, listOf(Effect.POISE)),
        secondSkill = generateSkill(
            DamageType.PIERCE,
            Sin.SLOTH,
            listOf(Effect.POISE, Effect.FRAGILE)
        ),
        thirdSkill = generateSkill(DamageType.PIERCE, Sin.ENVY, emptyList()),
        defenseSkill = DefenseSkill(
            0,
            "Counter Wrath",
            DefenseSkillType.COUNTER,
            0,
            0,
            0,
            Sin.WRATH
        ),
        passive = dummyPassive,
        support = dummySupport
    )

    val thirdIdentity = generateIdentity(
        name = "TEST IDENTITY #3",
        sinnerId = 3,
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFF,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.GLUTTONY, listOf(Effect.BLEED)),
        secondSkill = generateSkill(DamageType.BLUNT, Sin.GLOOM, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        defenseSkill = DefenseSkill(
            0,
            "Counter Wrath",
            DefenseSkillType.COUNTER,
            0,
            0,
            0,
            Sin.WRATH
        ),
        passive = dummyPassive,
        support = dummySupport
    )

    val fourthIdentity = generateIdentity(
        name = "TEST IDENTITY #4",
        sinnerId = 1,
        slashResist = IdentityDamageResistType.INEFF,
        pierceResist = IdentityDamageResistType.FATAL,
        bluntResist = IdentityDamageResistType.NORMAL,
        firstSkill = generateSkill(DamageType.BLUNT, Sin.ENVY, listOf(Effect.POISE)),
        secondSkill = generateSkill(DamageType.PIERCE, Sin.SLOTH, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        defenseSkill = DefenseSkill(0, "Counter Lust", DefenseSkillType.COUNTER, 0, 0, 0, Sin.LUST),
        passive = dummyPassive,
        support = dummySupport
    )

    val fifthIdentity = generateIdentity(
        name = "TEST IDENTITY #5",
        sinnerId = 4,
        slashResist = IdentityDamageResistType.INEFF,
        pierceResist = IdentityDamageResistType.FATAL,
        bluntResist = IdentityDamageResistType.NORMAL,
        firstSkill = generateSkill(DamageType.BLUNT, Sin.ENVY, listOf(Effect.POISE)),
        secondSkill = generateSkill(DamageType.PIERCE, Sin.SLOTH, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        defenseSkill = DefenseSkill(0, "Counter Lust", DefenseSkillType.COUNTER, 0, 0, 0, Sin.LUST),
        passive = dummyPassive,
        support = dummySupport
    )

    val firstEgo = generateEgo(
        name = "TEST EGO #1",
        sinnerId = 2,
        riskLevel = RiskLevel.ZAYIN,
        awakeningSkill = generateEgoSkill(DamageType.BLUNT, Sin.PRIDE, listOf(Effect.ATT_UP)),
        resourceCost = mapOf(Sin.LUST to 2, Sin.PRIDE to 2),
        sinResistances = mapOf(
            Sin.LUST to EgoSinResistType.FATAL,
            Sin.GLUTTONY to EgoSinResistType.FATAL,
            Sin.PRIDE to EgoSinResistType.ENDURE
        )
    )

    val secondEgo = generateEgo(
        "TEST EGO #2",
        4,
        RiskLevel.HE,
        generateEgoSkill(
            DamageType.SLASH,
            Sin.WRATH,
            effects = listOf(Effect.BURN)
        ),
        mapOf(Sin.WRATH to 4, Sin.PRIDE to 2),
        mapOf(
            Sin.WRATH to EgoSinResistType.INEFF,
            Sin.SLOTH to EgoSinResistType.FATAL,
            Sin.GLOOM to EgoSinResistType.FATAL,
            Sin.ENVY to EgoSinResistType.ENDURE
        )
    )

    val thirdEgo = generateEgo(
        "TEST EGO #3",
        2,
        RiskLevel.TETH,
        generateEgoSkill(DamageType.SLASH, Sin.ENVY, listOf(Effect.FRAGILE)),
        mapOf(Sin.ENVY to 6),
        mapOf(
            Sin.WRATH to EgoSinResistType.FATAL,
            Sin.LUST to EgoSinResistType.FATAL,
            Sin.ENVY to EgoSinResistType.INEFF
        )
    )

    val fourthEgo = generateEgo(
        "TEST EGO #4",
        3,
        RiskLevel.TETH,
        generateEgoSkill(DamageType.SLASH, Sin.ENVY, listOf(Effect.FRAGILE)),
        mapOf(Sin.ENVY to 6),
        mapOf(
            Sin.WRATH to EgoSinResistType.FATAL,
            Sin.LUST to EgoSinResistType.FATAL,
            Sin.ENVY to EgoSinResistType.INEFF
        )
    )

    val fifthEgo = generateEgo(
        "TEST EGO #5",
        1,
        RiskLevel.TETH,
        generateEgoSkill(DamageType.SLASH, Sin.ENVY, listOf(Effect.FRAGILE)),
        mapOf(Sin.ENVY to 6),
        mapOf(
            Sin.WRATH to EgoSinResistType.FATAL,
            Sin.GLOOM to EgoSinResistType.FATAL,
            Sin.ENVY to EgoSinResistType.INEFF
        )
    )

    fun generateSkill(damage: DamageType, sin: Sin, effects: List<Effect>) =
        Skill(0, "test skill", damage, sin, 3, 3, 2, 2, effects)

    fun generateIdentity(
        name: String,
        sinnerId: Int,
        slashResist: IdentityDamageResistType,
        pierceResist: IdentityDamageResistType,
        bluntResist: IdentityDamageResistType,
        firstSkill: Skill,
        secondSkill: Skill,
        thirdSkill: Skill,
        defenseSkill: DefenseSkill,
        passive: Passive = dummyPassive,
        support: Support = dummySupport
    ) = Identity(
        id = 0,
        name = name,
        sinnerId = sinnerId,
        rarity = 0,
        slashRes = slashResist,
        pierceRes = pierceResist,
        bluntRes = bluntResist,
        hp = 100,
        defense = 30,
        offense = 30,
        speed = 2 to 5,
        firstSkill = firstSkill,
        secondSkill = secondSkill,
        thirdSkill = thirdSkill,
        defenseSkill = defenseSkill,
        passive = passive,
        support = support,
        imageUrl = ""
    )

    fun generateEgo(
        name: String,
        sinnerId: Int,
        riskLevel: RiskLevel,
        awakeningSkill: EgoSkill,
        resourceCost: Map<Sin, Int>,
        sinResistances: Map<Sin, EgoSinResistType>
    ) = Ego(
        id = 0,
        name = name,
        sinnerId = sinnerId,
        risk = riskLevel,
        awakeningSkill = awakeningSkill,
        corrosionSkill = null,
        passive = Passive(0, 0, SinCost(emptyList()), ""),
        resourceCost = resourceCost,
        sanityCost = 0,
        sinResistances = sinResistances,
        imageUrl = ""
    )

    fun generateEgoSkill(dmgType: DamageType, sin: Sin, effects: List<Effect>) = EgoSkill(
        id = 0,
        name = "",
        dmgType = dmgType,
        sin = sin,
        offense = 0,
        basePower = 0,
        coinPower = 0,
        coinCount = 0,
        sanityCost = 0,
        effects = effects
    )
}
