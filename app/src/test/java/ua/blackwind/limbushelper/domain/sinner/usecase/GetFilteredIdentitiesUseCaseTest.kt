package ua.blackwind.limbushelper.domain.sinner.usecase


import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Passive
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.domain.sinner.model.Support

@OptIn(ExperimentalCoroutinesApi::class)
class GetFilteredIdentitiesUseCaseTest {

    private val dummyPassive = mockk<Passive>()

    private val dummySupport = mockk<Support>()

    private val testData = listOf(
        generateIdentity(
            "TEST IDENTITY #1",
            IdentityDamageResistType.INEFFECTIVE,
            IdentityDamageResistType.NORMAL,
            IdentityDamageResistType.FATAL,
            generateSkill(DamageType.SLASH, Sin.LUST, listOf(Effect.BURN)),
            generateSkill(DamageType.SLASH, Sin.ENVY, listOf(Effect.BLEED)),
            generateSkill(DamageType.BLUNT, Sin.WRATH, emptyList()),
            dummyPassive,
            dummySupport
        ),
        generateIdentity(
            "TEST IDENTITY #2",
            IdentityDamageResistType.NORMAL,
            IdentityDamageResistType.INEFFECTIVE,
            IdentityDamageResistType.FATAL,
            generateSkill(DamageType.PIERCE, Sin.PRIDE, listOf(Effect.POISE)),
            generateSkill(DamageType.PIERCE, Sin.PRIDE, listOf(Effect.POISE)),
            generateSkill(DamageType.PIERCE, Sin.PRIDE, emptyList()),
            dummyPassive,
            dummySupport
        ),
        generateIdentity(
            "TEST IDENTITY #3",
            IdentityDamageResistType.NORMAL,
            IdentityDamageResistType.FATAL,
            IdentityDamageResistType.INEFFECTIVE,
            generateSkill(DamageType.SLASH, Sin.GLUTTONY, listOf(Effect.POISE)),
            generateSkill(DamageType.BLUNT, Sin.GLOOM, listOf(Effect.POISE)),
            generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
            dummyPassive,
            dummySupport
        ),
    )


    @Test
    fun `empty filter returns full data`() {

        val expected = testData
        val repository = mockk<SinnerRepository>()

        coEvery { repository.getAllIdentities() } answers { testData }

        val useCase = GetFilteredIdentitiesUseCase(repository)

        val resistArgs = FilterResistSetArg(
            FilterDamageTypeArg.Empty,
            FilterDamageTypeArg.Empty,
            FilterDamageTypeArg.Empty
        )

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty),
        )

        val filter = IdentityFilter(resistArgs, skillArgs, emptyList())

        return runTest {
            val result = useCase.invoke(filter)
            coVerify(atLeast = 1) { repository.getAllIdentities() }
            confirmVerified(repository)
            assertEquals(expected, result)
        }
    }

    @Test

    private fun generateSkill(damage: DamageType, sin: Sin, effects: List<Effect>) =
        Skill(0, "test skill", 0, damage, sin, 3, 3, 2, 2, effects)

    private fun generateIdentity(
        name: String,
        slashResist: IdentityDamageResistType,
        pierceResist: IdentityDamageResistType,
        bluntResist: IdentityDamageResistType,
        firstSkill: Skill,
        secondSkill: Skill,
        thirdSkill: Skill,
        passive: Passive,
        support: Support
    ) = Identity(
        id = 0,
        name = name,
        sinnerId = 1,
        rarity = 0,
        slashRes = slashResist,
        pierceRes = pierceResist,
        bluntRes = bluntResist,
        maxHp = 100,
        maxArmor = 30,
        maxDamage = 30,
        speed = 2 to 5,
        firstSkill = firstSkill,
        secondSkill = secondSkill,
        thirdSkill = thirdSkill,
        passive = passive,
        support = support,
        imageUrl = ""
    )
}