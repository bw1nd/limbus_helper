package ua.blackwind.limbushelper.domain.sinner.usecase

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.filter.*
import ua.blackwind.limbushelper.domain.sinner.model.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetFilteredIdentitiesUseCaseTest {

    private val dummyPassive = mockk<Passive>()

    private val dummySupport = mockk<Support>()

    private val repository = mockk<SinnerRepository>()

    private val firstIdentity = generateIdentity(
        name = "TEST IDENTITY #1",
        sinnerId = 1,
        slashResist = IdentityDamageResistType.INEFFECTIVE,
        pierceResist = IdentityDamageResistType.NORMAL,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.LUST, listOf(Effect.BURN)),
        secondSkill = generateSkill(
            DamageType.SLASH,
            Sin.ENVY,
            listOf(Effect.BLEED, Effect.PARALYSIS)
        ),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.WRATH, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )
    private val secondIdentity = generateIdentity(
        name = "TEST IDENTITY #2",
        sinnerId = 2,
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFFECTIVE,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.PIERCE, Sin.PRIDE, listOf(Effect.POISE)),
        secondSkill = generateSkill(
            DamageType.PIERCE,
            Sin.SLOTH,
            listOf(Effect.POISE, Effect.FRAGILE)
        ),
        thirdSkill = generateSkill(DamageType.PIERCE, Sin.ENVY, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )

    private val thirdIdentity = generateIdentity(
        name = "TEST IDENTITY #3",
        sinnerId = 3,
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFFECTIVE,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.GLUTTONY, listOf(Effect.BLEED)),
        secondSkill = generateSkill(DamageType.BLUNT, Sin.GLOOM, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )

    private val fourthIdentity = generateIdentity(
        name = "TEST IDENTITY #4",
        sinnerId = 1,
        slashResist = IdentityDamageResistType.INEFFECTIVE,
        pierceResist = IdentityDamageResistType.FATAL,
        bluntResist = IdentityDamageResistType.NORMAL,
        firstSkill = generateSkill(DamageType.BLUNT, Sin.ENVY, listOf(Effect.POISE)),
        secondSkill = generateSkill(DamageType.PIERCE, Sin.SLOTH, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )

    private val testData = listOf(
        firstIdentity,
        secondIdentity,
        thirdIdentity,
        fourthIdentity
    )

    private val emptyResistArgs = FilterResistSetArg(
        FilterDamageTypeArg.Empty,
        FilterDamageTypeArg.Empty,
        FilterDamageTypeArg.Empty
    )

    private val emptySkillArg = FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty)

    @BeforeEach
    fun prepareTestData() {
        coEvery { repository.getAllIdentities() } answers { testData }
    }

    @Test
    fun `empty filter returns full data`() {

        val expected = testData

        return testBase(expected)
    }

    @Test
    fun `filter with one slash damage type returns identities #1 & #3`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            emptySkillArg,
            emptySkillArg,
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with slash and blunt damage types return identities #1 & #3`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            emptySkillArg,
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with slash and two blunt damage types returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with three pierce type damage returns identity #2`() {
        val expected = listOf(
            secondIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pierce pierce blunt type damage returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with one wrath sin returns identity #1`() {
        val expected = listOf(
            firstIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.WRATH)),
            emptySkillArg,
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with one lust sin returns identities #1 #3 #4`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity,
            fourthIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
            emptySkillArg,
            emptySkillArg,
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with lust and envy sins returns identities #1 #4`() {
        val expected = listOf(
            firstIdentity,
            fourthIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with gluttony gloom lust sins returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.GLOOM)),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.GLUTTONY)),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pride lust sins returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.PRIDE)),
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `strict filter with blunt_gloom returns identity #3`() {
        val expected = listOf(thirdIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            FilterSkillArg(
                FilterDamageTypeArg.Type(DamageType.BLUNT),
                FilterSinTypeArg.Type(Sin.GLOOM)
            ),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `strict filter with pierce_sloth and loose slash returns emptyList`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            emptySkillArg,
            FilterSkillArg(
                FilterDamageTypeArg.Type(DamageType.PIERCE),
                FilterSinTypeArg.Type(Sin.SLOTH)
            ),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `loose filter with blunt lust envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(
                FilterDamageTypeArg.Empty,
                FilterSinTypeArg.Type(Sin.LUST)
            ),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pierce_gloom envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(
                FilterDamageTypeArg.Empty,
                FilterSinTypeArg.Type(Sin.LUST)
            ),
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with ineffective slash resist returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val resistArguments = FilterResistSetArg(
            FilterDamageTypeArg.Type(DamageType.SLASH),
            FilterDamageTypeArg.Empty,
            FilterDamageTypeArg.Empty
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with ineffective pierce fatal blunt resist returns identities #2 #3`() {
        val expected = listOf(secondIdentity, thirdIdentity)

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Type(DamageType.PIERCE),
            normal = FilterDamageTypeArg.Empty,
            fatal = FilterDamageTypeArg.Type(DamageType.BLUNT)
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with ineffective slash fatal pierce normal blunt resist returns identity #4`() {
        val expected = listOf(fourthIdentity)

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Type(DamageType.SLASH),
            normal = FilterDamageTypeArg.Type(DamageType.BLUNT),
            fatal = FilterDamageTypeArg.Type(DamageType.PIERCE)
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with fatal blunt resist and skill with lust sin returns identity #1 #3`() {
        val expected = listOf(firstIdentity, thirdIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
            emptySkillArg,
            emptySkillArg,
        )

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Empty,
            normal = FilterDamageTypeArg.Empty,
            fatal = FilterDamageTypeArg.Type(DamageType.BLUNT)
        )

        return testBase(expected, skillArgs, resistArguments)
    }

    @Test
    fun `filter with normal slash resist and skill with two blunt returns identity #3`() {
        val expected = listOf(thirdIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            emptySkillArg,
        )

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Empty,
            normal = FilterDamageTypeArg.Type(DamageType.SLASH),
            fatal = FilterDamageTypeArg.Empty
        )

        return testBase(expected, skillArgs, resistArguments)
    }

    @Test
    fun `filter with rupture effect returns empty list`() {
        val expected = emptyList<Identity>()

        val effects = listOf(Effect.RUPTURE)

        return testBase(expected, effects = effects)
    }

    @Test
    fun `filter with poise effect returns identity #2 #3 #4`() {
        val expected = listOf(secondIdentity, thirdIdentity, fourthIdentity)

        val effects = listOf(Effect.POISE)

        return testBase(expected, effects = effects)
    }

    @Test
    fun `filter with burn bleed effect returns identity #1`() {
        val expected = listOf(firstIdentity)

        val effects = listOf(Effect.BURN, Effect.BLEED)

        return testBase(expected, effects = effects)
    }

    @Test
    fun `filter with bleed effect fatal blunt resist and slash lust skill returns identity #1 #3`() {
        val expected = listOf(firstIdentity, thirdIdentity)

        val effects = listOf(Effect.BLEED)

        val skillArgs = IdentityFilterSkillsSetArg(
            emptySkillArg,
            FilterSkillArg(
                FilterDamageTypeArg.Type(DamageType.SLASH),
                FilterSinTypeArg.Empty
            ),
            FilterSkillArg(
                FilterDamageTypeArg.Empty,
                FilterSinTypeArg.Type(Sin.LUST)
            ),
        )

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Empty,
            normal = FilterDamageTypeArg.Empty,
            fatal = FilterDamageTypeArg.Type(DamageType.BLUNT)
        )

        return testBase(expected, skillArgs, resistArguments, effects)
    }

    //TODO Add more complex tests combining different data with sinners
    @Test
    fun `filter with one sinner with id 4 returns empty list`() {
        val expected = emptyList<Identity>()

        val sinners = listOf(4)

        return testBase(expected, sinners = sinners)
    }

    @Test
    fun `filter with sinner id 1 returns identity #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val sinners = listOf(1)

        return testBase(expected, sinners = sinners)
    }

    @Test
    fun `filter with sinners id 2 and 3 returns identity #2 #3`() {
        val expected = listOf(secondIdentity, thirdIdentity)

        val sinners = listOf(2, 3)

        return testBase(expected, sinners = sinners)
    }

    private fun testBase(
        expected: List<Identity>,
        skillArgs: IdentityFilterSkillsSetArg = IdentityFilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            emptySkillArg
        ),
        resistSetArg: FilterResistSetArg = emptyResistArgs,
        effects: List<Effect> = emptyList(),
        sinners: List<Int> = emptyList()
    ) {
        val useCase = GetFilteredIdentitiesUseCase(repository)

        val filter = IdentityFilter(resistSetArg, skillArgs, effects, sinners)

        return runTest {
            val result = useCase.invoke(filter).sortedBy { it.name }

            coVerify(exactly = 1) { repository.getAllIdentities() }
            confirmVerified(repository)
            assertEquals(expected, result)
        }
    }


    private fun generateSkill(damage: DamageType, sin: Sin, effects: List<Effect>) =
        Skill(0, "test skill", damage, sin, 3, 3, 2, 2, effects)

    private fun generateIdentity(
        name: String,
        sinnerId: Int,
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
        sinnerId = sinnerId,
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