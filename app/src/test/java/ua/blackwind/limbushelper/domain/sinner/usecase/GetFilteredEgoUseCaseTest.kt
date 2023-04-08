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

class GetFilteredEgoUseCaseTest {

//TODO write tests for this use case

    private val repository = mockk<SinnerRepository>()

    private val firstEgo = generateEgo(
        name = "TEST EGO #1",
        sinnerId = 2,
        riskLevel = RiskLevel.ZAYIN,
        awakeningSkill = generateEgoSkill(DamageType.BLUNT, Sin.PRIDE, listOf(Effect.ATT_UP)),
        resourceCost = mapOf(Sin.LUST to 2, Sin.PRIDE to 2),
        sinResistances = mapOf(
            Sin.LUST to EgoSinResistType.FATAL, Sin.GLUTTONY to EgoSinResistType.FATAL,
            Sin.PRIDE to EgoSinResistType.ENDURE
        )
    )

    private val secondEgo = generateEgo(
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
            Sin.WRATH to EgoSinResistType.INEFF, Sin.SLOTH to EgoSinResistType.FATAL,
            Sin.GLOOM to EgoSinResistType.FATAL, Sin.ENVY to EgoSinResistType.ENDURE
        )
    )

    private val thirdEgo = generateEgo(
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

    private val testData = listOf(firstEgo, secondEgo, thirdEgo)

    private val emptySkillArg = FilterSkillArg(FilterDamageTypeArg.Empty, FilterSinTypeArg.Empty)
    private val emptyResistArg = EgoFilterSinResistTypeArg(emptyList())
    private val emptyResourceArg = EgoFilterPriceSetArg(emptyList())
    private val emptyEffectsArg = emptyList<Effect>()
    private val emptySinnerArg = emptyList<Int>()

    @BeforeEach
    fun prepareTestData() {
        coEvery { repository.getAllEgo() } returns testData
    }


    @Test
    fun `empty filter returns full data`() {
        val expected = testData

        return testBase(expected)
    }

    @Test
    fun `filter with pierce dmg type returns empty list`() {
        val expected = emptyList<Ego>()

        val skillArg = FilterSkillArg(
            FilterDamageTypeArg.Type(DamageType.PIERCE),
            FilterSinTypeArg.Empty
        )

        return testBase(expected, skillArg)
    }

    @Test
    fun `filter with slash dmg type returns ego #2 #3`() {
        val expected = listOf(secondEgo, thirdEgo)

        val skillArg = FilterSkillArg(
            FilterDamageTypeArg.Type(DamageType.SLASH),
            FilterSinTypeArg.Empty
        )

        return testBase(expected, skillArg)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun testBase(
        expected: List<Ego>,
        skillArg: FilterSkillArg = emptySkillArg,
        resistArg: EgoFilterSinResistTypeArg = emptyResistArg,
        resourceArg: EgoFilterPriceSetArg = emptyResourceArg,
        effects: List<Effect> = emptyEffectsArg,
        sinners: List<Int> = emptySinnerArg
    ) {
        val useCase = GetFilteredEgoUseCase(repository)

        val filter = EgoFilter(skillArg, resistArg, resourceArg, effects, sinners)

        return runTest {
            val result = useCase.invoke(filter)

            coVerify(exactly = 1) { repository.getAllEgo() }
            confirmVerified(repository)
            assertEquals(expected, result)
        }
    }

    private fun generateEgo(
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

    private fun generateEgoSkill(dmgType: DamageType, sin: Sin, effects: List<Effect>) = EgoSkill(
        id = 0,
        name = "",
        dmgType = dmgType,
        sin = sin,
        dmg = 0,
        baseDie = 0,
        coinBonus = 0,
        coinCount = 0,
        sanityCost = 0,
        effects = effects
    )
}