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

    private val repository = mockk<SinnerRepository>()

    private val firstEgo = generateEgo(
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
            Sin.WRATH to EgoSinResistType.INEFF,
            Sin.SLOTH to EgoSinResistType.FATAL,
            Sin.GLOOM to EgoSinResistType.FATAL,
            Sin.ENVY to EgoSinResistType.ENDURE
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

    private val emptySkillArg = FilterSkillArg(TypeHolder.Empty, TypeHolder.Empty)
    private val emptyResistArg = emptyMap<EgoSinResistType, Sin>()
    private val emptyResourceArg = emptyList<Sin>()
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
            TypeHolder.Value(DamageType.PIERCE),
            TypeHolder.Empty
        )

        return testBase(expected, skillArg)
    }

    @Test
    fun `filter with slash dmg type returns ego #2 #3`() {
        val expected = listOf(secondEgo, thirdEgo)

        val skillArg = FilterSkillArg(
            TypeHolder.Value(DamageType.SLASH),
            TypeHolder.Empty
        )

        return testBase(expected, skillArg)
    }

    @Test
    fun `filter with sloth skill sin type returns empty list`() {
        val expected = emptyList<Ego>()

        val skillArg = FilterSkillArg(
            TypeHolder.Empty,
            TypeHolder.Value(Sin.SLOTH)
        )

        return testBase(expected, skillArg)
    }

    @Test
    fun `filter with slash envy skill returns ego #3`() {
        val expected = listOf(thirdEgo)

        val skillArg = FilterSkillArg(
            TypeHolder.Value(DamageType.SLASH),
            TypeHolder.Value(Sin.ENVY)
        )

        return testBase(expected, skillArg)
    }

    @Test
    fun `filter with protect effect returns empty list`() {
        val expected = emptyList<Ego>()

        val effectsArg = listOf(Effect.PROTECT)

        return testBase(expected, effects = effectsArg)
    }

    @Test
    fun `filter with burn effect returns ego #2`() {
        val expected = listOf(secondEgo)

        val effectsArg = listOf(Effect.BURN)

        return testBase(expected, effects = effectsArg)
    }

    @Test
    fun `filter with gloom resource cost returns empty list`() {
        val expected = emptyList<Ego>()

        val resourceCostArg = listOf(Sin.GLOOM)

        return testBase(expected, resourceArg = resourceCostArg)
    }

    @Test
    fun `filter with pride resource cost returns ego #1 #2`() {
        val expected = listOf(firstEgo, secondEgo)

        val resourceCostArg = listOf(Sin.PRIDE)

        return testBase(expected, resourceArg = resourceCostArg)
    }

    @Test
    fun `filter with wrath innef gloom endure resist returns empty list`() {
        val expected = emptyList<Ego>()

        val resistArg =
            mapOf(EgoSinResistType.INEFF to Sin.WRATH, EgoSinResistType.ENDURE to Sin.GLOOM)

        return testBase(expected, resistArg = resistArg)
    }

    @Test
    fun `filter with lust fatal resist filter returns ego #1 #3`() {
        val expected = listOf(firstEgo, thirdEgo)

        val resistArg = mapOf(EgoSinResistType.FATAL to Sin.LUST)

        return testBase(expected, resistArg = resistArg)
    }

    @Test
    fun `filter with sinner id 5 returns empty list`() {
        val expected = emptyList<Ego>()

        val sinnerArg = listOf(5)

        return testBase(expected, sinners = sinnerArg)
    }

    @Test
    fun `filter with sinner id 2 returns ego #1 #3`() {
        val expected = listOf(firstEgo, thirdEgo)

        val sinnerArg = listOf(2)

        return testBase(expected, sinners = sinnerArg)
    }

    @Test
    fun `filter with skill blunt dng and pride endure resist returns ego #1`() {
        val expected = listOf(firstEgo)

        val skillArg =
            FilterSkillArg(TypeHolder.Value(DamageType.BLUNT), TypeHolder.Empty)
        val resistArg = mapOf(EgoSinResistType.ENDURE to Sin.PRIDE)

        return testBase(expected, skillArg, resistArg)
    }

    @Test
    fun `filter with skill sin type envy, fatal lust resist and sinner id 2 returns ego #3`() {
        val expected = listOf(thirdEgo)

        val skillArg =
            FilterSkillArg(TypeHolder.Empty, TypeHolder.Value(Sin.ENVY))
        val resistArg = mapOf(EgoSinResistType.FATAL to Sin.LUST)
        val sinnerArg = listOf(2)

        return testBase(expected, skillArg, resistArg, sinners = sinnerArg)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun testBase(
        expected: List<Ego>,
        skillArg: FilterSkillArg = emptySkillArg,
        resistArg: Map<EgoSinResistType, Sin> = emptyResistArg,
        resourceArg: List<Sin> = emptyResourceArg,
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