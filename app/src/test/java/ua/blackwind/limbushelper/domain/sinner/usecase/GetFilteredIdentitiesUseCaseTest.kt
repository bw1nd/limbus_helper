package ua.blackwind.limbushelper.domain.sinner.usecase

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
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

    private val repository = mockk<SinnerRepository>()

    private val firstIdentity = generateIdentity(
        name = "TEST IDENTITY #1",
        slashResist = IdentityDamageResistType.INEFFECTIVE,
        pierceResist = IdentityDamageResistType.NORMAL,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.LUST, listOf(Effect.BURN)),
        secondSkill = generateSkill(DamageType.SLASH, Sin.ENVY, listOf(Effect.BLEED)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.WRATH, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )
    private val secondIdentity = generateIdentity(
        name = "TEST IDENTITY #2",
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFFECTIVE,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.PIERCE, Sin.PRIDE, listOf(Effect.POISE)),
        secondSkill = generateSkill(DamageType.PIERCE, Sin.SLOTH, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.PIERCE, Sin.ENVY, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )

    private val thirdIdentity = generateIdentity(
        name = "TEST IDENTITY #3",
        slashResist = IdentityDamageResistType.NORMAL,
        pierceResist = IdentityDamageResistType.INEFFECTIVE,
        bluntResist = IdentityDamageResistType.FATAL,
        firstSkill = generateSkill(DamageType.SLASH, Sin.GLUTTONY, listOf(Effect.POISE)),
        secondSkill = generateSkill(DamageType.BLUNT, Sin.GLOOM, listOf(Effect.POISE)),
        thirdSkill = generateSkill(DamageType.BLUNT, Sin.LUST, emptyList()),
        passive = dummyPassive,
        support = dummySupport
    )

    private val fourthIdentity = generateIdentity(
        name = "TEST IDENTITY #4",
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

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            emptySkillArg,
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with one slash damage type returns identities #1 & #3`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            emptySkillArg,
            emptySkillArg,
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with slash and blunt damage types return identities #1 & #3`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            emptySkillArg,
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with slash and two blunt damage types returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with three pierce type damage returns identity #2`() {
        val expected = listOf(
            secondIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with pierce pierce blunt type damage returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.PIERCE), FilterSinTypeArg.Empty),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with one wrath sin returns identity #1`() {
        val expected = listOf(
            firstIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.WRATH)),
            emptySkillArg,
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with one lust sin returns identities #1 #3 #4`() {
        val expected = listOf(
            firstIdentity,
            thirdIdentity,
            fourthIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
            emptySkillArg,
            emptySkillArg,
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with lust and envy sins returns identities #1 #4`() {
        val expected = listOf(
            firstIdentity,
            fourthIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with gluttony gloom lust sins returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.GLOOM)),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.GLUTTONY)),
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with pride lust sins returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.PRIDE)),
            emptySkillArg,
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.LUST)),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `strict filter with blunt_gloom returns identity #3`() {
        val expected = listOf(thirdIdentity)

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            FilterSkillArg(
                FilterDamageTypeArg.Type(DamageType.BLUNT),
                FilterSinTypeArg.Type(Sin.GLOOM)
            ),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `strict filter with pierce_sloth and loose slash returns emptyList`() {
        val expected = emptyList<Identity>()

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.SLASH), FilterSinTypeArg.Empty),
            emptySkillArg,
            FilterSkillArg(
                FilterDamageTypeArg.Type(DamageType.PIERCE),
                FilterSinTypeArg.Type(Sin.SLOTH)
            ),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `loose filter with blunt lust envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(
                FilterDamageTypeArg.Empty,
                FilterSinTypeArg.Type(Sin.LUST)
            ),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with pierce_gloom envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = FilterSkillsSetArg(
            FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Type(Sin.ENVY)),
            FilterSkillArg(FilterDamageTypeArg.Type(DamageType.BLUNT), FilterSinTypeArg.Empty),
            FilterSkillArg(
                FilterDamageTypeArg.Empty,
                FilterSinTypeArg.Type(Sin.LUST)
            ),
        )

        return testBase(expected, skillArgs, emptyResistArgs, emptyList())
    }

    @Test
    fun `filter with ineffective slash resist returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            emptySkillArg,
        )

        val resistArguments = FilterResistSetArg(
            FilterDamageTypeArg.Type(DamageType.SLASH),
            FilterDamageTypeArg.Empty,
            FilterDamageTypeArg.Empty
        )

        return testBase(expected, skillArgs, resistArguments, emptyList())
    }

    @Test
    fun `filter with ineffective pierce fatal blunt resist returns identities #2 #3`() {
        val expected = listOf(secondIdentity, thirdIdentity)

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            emptySkillArg,
        )

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Type(DamageType.PIERCE),
            normal = FilterDamageTypeArg.Empty,
            fatal = FilterDamageTypeArg.Type(DamageType.BLUNT)
        )

        return testBase(expected, skillArgs, resistArguments, emptyList())
    }

    @Test
    fun `filter with ineffective slash fatal pierce normal blunt resist returns identity #4`() {
        val expected = listOf(fourthIdentity)

        val skillArgs = FilterSkillsSetArg(
            emptySkillArg,
            emptySkillArg,
            emptySkillArg,
        )

        val resistArguments = FilterResistSetArg(
            ineffective = FilterDamageTypeArg.Type(DamageType.SLASH),
            normal = FilterDamageTypeArg.Type(DamageType.BLUNT),
            fatal = FilterDamageTypeArg.Type(DamageType.PIERCE)
        )

        return testBase(expected, skillArgs, resistArguments, emptyList())
    }

    //TODO write more tests for combined damage/sin types cases
    //TODO write tests with combined resistances and skills
    private fun testBase(
        expected: List<Identity>,
        skillArgs: FilterSkillsSetArg,
        resistSetArg: FilterResistSetArg,
        effects: List<Effect>
    ) {
        val useCase = GetFilteredIdentitiesUseCase(repository)

        val filter = IdentityFilter(resistSetArg, skillArgs, effects)

        return runTest {
            val result = useCase.invoke(filter).sortedBy { it.name }

            coVerify(exactly = 1) { repository.getAllIdentities() }
            confirmVerified(repository)
            assertEquals(expected, result)
        }
    }

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