package ua.blackwind.limbushelper.domain.sinner.usecase

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.domain.filter.*
import ua.blackwind.limbushelper.domain.sinner.model.*
import util.TestEntitiesCreator

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetFilteredIdentitiesUseCaseTest {
    private val repository = mockk<SinnerRepository>()
    private val entityGenerator = TestEntitiesCreator()

    private val firstIdentity = entityGenerator.firstIdentity
    private val secondIdentity = entityGenerator.secondIdentity
    private val thirdIdentity = entityGenerator.thirdIdentity
    private val fourthIdentity = entityGenerator.fourthIdentity

    private val testData = listOf(
        firstIdentity,
        secondIdentity,
        thirdIdentity,
        fourthIdentity
    )

    private val emptyResistArgs = FilterResistSetArg(
        TypeHolder.Empty,
        TypeHolder.Empty,
        TypeHolder.Empty
    )

    private val emptySkillArg = FilterSkillArg(TypeHolder.Empty, TypeHolder.Empty)

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
            FilterSkillArg(TypeHolder.Value(DamageType.SLASH), TypeHolder.Empty),
            emptySkillArg,
            emptySkillArg,
            false
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
            FilterSkillArg(TypeHolder.Value(DamageType.SLASH), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            emptySkillArg,
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with slash and two blunt damage types returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Value(DamageType.SLASH), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with three pierce type damage returns identity #2`() {
        val expected = listOf(
            secondIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Value(DamageType.PIERCE), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.PIERCE), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.PIERCE), TypeHolder.Empty),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pierce pierce blunt type damage returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Value(DamageType.PIERCE), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.PIERCE), TypeHolder.Empty),
            false
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
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.WRATH)),
            emptySkillArg,
            false
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
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.LUST)),
            emptySkillArg,
            emptySkillArg,
            false
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
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.LUST)),
            emptySkillArg,
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.ENVY)),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with gluttony gloom lust sins returns identity #3`() {
        val expected = listOf(
            thirdIdentity
        ).sortedBy { it.name }

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.GLOOM)),
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.GLUTTONY)),
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.LUST)),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pride lust sins returns empty list`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.PRIDE)),
            emptySkillArg,
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.LUST)),
            false
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
                TypeHolder.Value(DamageType.BLUNT),
                TypeHolder.Value(Sin.GLOOM)
            ),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `strict filter with pierce_sloth and loose slash returns emptyList`() {
        val expected = emptyList<Identity>()

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Value(DamageType.SLASH), TypeHolder.Empty),
            emptySkillArg,
            FilterSkillArg(
                TypeHolder.Value(DamageType.PIERCE),
                TypeHolder.Value(Sin.SLOTH)
            ),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `loose filter with blunt lust envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.ENVY)),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            FilterSkillArg(
                TypeHolder.Empty,
                TypeHolder.Value(Sin.LUST)
            ),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with pierce_gloom envy returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.ENVY)),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            FilterSkillArg(
                TypeHolder.Empty,
                TypeHolder.Value(Sin.LUST)
            ),
            false
        )

        return testBase(expected, skillArgs)
    }

    @Test
    fun `filter with ineffective slash resist returns identities #1 #4`() {
        val expected = listOf(firstIdentity, fourthIdentity)

        val resistArguments = FilterResistSetArg(
            TypeHolder.Value(DamageType.SLASH),
            TypeHolder.Empty,
            TypeHolder.Empty
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with ineffective pierce fatal blunt resist returns identities #2 #3`() {
        val expected = listOf(secondIdentity, thirdIdentity)

        val resistArguments = FilterResistSetArg(
            ineffective = TypeHolder.Value(DamageType.PIERCE),
            normal = TypeHolder.Empty,
            fatal = TypeHolder.Value(DamageType.BLUNT)
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with ineffective slash fatal pierce normal blunt resist returns identity #4`() {
        val expected = listOf(fourthIdentity)

        val resistArguments = FilterResistSetArg(
            ineffective = TypeHolder.Value(DamageType.SLASH),
            normal = TypeHolder.Value(DamageType.BLUNT),
            fatal = TypeHolder.Value(DamageType.PIERCE)
        )

        return testBase(expected = expected, resistSetArg = resistArguments)
    }

    @Test
    fun `filter with fatal blunt resist and skill with lust sin returns identity #1 #3`() {
        val expected = listOf(firstIdentity, thirdIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.LUST)),
            emptySkillArg,
            emptySkillArg,
            false
        )

        val resistArguments = FilterResistSetArg(
            ineffective = TypeHolder.Empty,
            normal = TypeHolder.Empty,
            fatal = TypeHolder.Value(DamageType.BLUNT)
        )

        return testBase(expected, skillArgs, resistArguments)
    }

    @Test
    fun `filter with normal slash resist and skill with two blunt returns identity #3`() {
        val expected = listOf(thirdIdentity)

        val skillArgs = IdentityFilterSkillsSetArg(
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty),
            emptySkillArg,
            false
        )

        val resistArguments = FilterResistSetArg(
            ineffective = TypeHolder.Empty,
            normal = TypeHolder.Value(DamageType.SLASH),
            fatal = TypeHolder.Empty
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
                TypeHolder.Value(DamageType.SLASH),
                TypeHolder.Empty
            ),
            FilterSkillArg(
                TypeHolder.Empty,
                TypeHolder.Value(Sin.LUST)
            ),
            false
        )

        val resistArguments = FilterResistSetArg(
            ineffective = TypeHolder.Empty,
            normal = TypeHolder.Empty,
            fatal = TypeHolder.Value(DamageType.BLUNT)
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
            emptySkillArg,
            false
        ),
        resistSetArg: FilterResistSetArg = emptyResistArgs,
        effects: List<Effect> = emptyList(),
        sinners: List<Int> = emptyList()
    ) {
        val useCase = GetFilteredIdentitiesUseCase(repository)

        val filter = IdentityFilter(skillArgs, resistSetArg, effects, sinners)

        return runTest {
            val result = useCase.invoke(filter).sortedBy { it.name }

            coVerify(exactly = 1) { repository.getAllIdentities() }
            confirmVerified(repository)
            assertEquals(expected, result)
        }
    }
}