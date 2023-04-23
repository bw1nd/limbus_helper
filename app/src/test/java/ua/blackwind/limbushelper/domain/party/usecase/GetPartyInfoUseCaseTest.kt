package ua.blackwind.limbushelper.domain.party.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.party.model.PartyInfoData
import ua.blackwind.limbushelper.domain.party.model.PartySinner
import ua.blackwind.limbushelper.domain.sinner.model.*
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.InfoPanelDamageResist
import util.*

internal class GetPartyInfoUseCaseTest {
    private val entityGenerator = TestEntitiesCreator()
    private val repository = mockk<SinnerRepository>()

    private val firstIdentity = entityGenerator.firstIdentity
    private val secondIdentity = entityGenerator.secondIdentity
    private val thirdIdentity = entityGenerator.thirdIdentity
    private val fourthIdentity = entityGenerator.fourthIdentity
    private val fifthIdentity = entityGenerator.fifthIdentity

    private val firstEgo = entityGenerator.firstEgo
    private val secondEgo = entityGenerator.secondEgo
    private val thirdEgo = entityGenerator.thirdEgo
    private val fourthEgo = entityGenerator.fourthEgo
    private val fifthEgo = entityGenerator.fifthEgo

    @BeforeEach
    fun prepareTestData() {
        coEvery { repository.getAllSinners() } returns listOf(
            Sinner(1, "TEST SINNER #1"),
            Sinner(2, "TEST SINNER #2"),
            Sinner(3, "TEST SINNER #3"),
            Sinner(4, "TEST SINNER #4"),
        )

        coEvery { repository.getEgoById(1) } returns fifthEgo
        coEvery { repository.getEgoById(2) } returns firstEgo
        coEvery { repository.getEgoById(3) } returns fourthEgo
        coEvery { repository.getEgoById(4) } returns secondEgo
    }

    @Test
    fun `empty party returns empty info`() {
        val expected = generatePartyInfoData(
            listOf(0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0),
            listOf(InfoPanelDamageResist.NA, InfoPanelDamageResist.NA, InfoPanelDamageResist.NA),
            listOf(
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
            )
        )

        val party = generateParty()

        testBase(expected, party)
    }

    @Test
    fun `test with one inactive identity returns empty info`() {
        val expected = generatePartyInfoData(
            skillCountByDamage = listOf(0, 0, 0),
            skillCountBySin = listOf(0, 0, 0, 0, 0, 0, 0),
            resistByDamagePotency = listOf(
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA
            ),
            resistBySinPotency = listOf(
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
                InfoPanelDamageResist.NA,
            ),
            activeIdentityCount = 0,
            totalIdentityCount = 1
        )

        val party =
            generateParty(
                generatePartySinner(
                    1,
                    listOf(PartyIdentity(firstIdentity, false))
                )
            )

        testBase(expected, party)
    }

    @Test
    fun `test with one active identity`() {
        val expected = generatePartyInfoData(
            skillCountByDamage = listOf(6, 0, 3),
            skillCountBySin = listOf(3, 3, 0, 0, 0, 0, 3),
            resistByDamagePotency = listOf(
                InfoPanelDamageResist.Perfect,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Bad
            ),
            resistBySinPotency = listOf(
                InfoPanelDamageResist.Bad,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Bad,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Perfect,
            ),
            activeIdentityCount = 1,
            totalIdentityCount = 1
        )

        val party =
            generateParty(
                generatePartySinner(
                    1,
                    listOf(PartyIdentity(firstIdentity, true))
                )
            )

        testBase(expected, party)
    }

    @Test
    fun `test with four active identities and no ego`() {
        val expected = generatePartyInfoData(
            skillCountByDamage = listOf(9, 12, 15),
            skillCountBySin = listOf(3, 9, 6, 3, 3, 3, 9),
            resistByDamagePotency = listOf(
                InfoPanelDamageResist.Good,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Bad
            ),
            resistBySinPotency = listOf(
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Good,
            ),
            activeIdentityCount = 4,
            totalIdentityCount = 4
        )

        val party = generateParty(
            generatePartySinner(1, listOf(PartyIdentity(firstIdentity, true))),
            generatePartySinner(2, listOf(PartyIdentity(secondIdentity, true))),
            generatePartySinner(3, listOf(PartyIdentity(thirdIdentity, true))),
            generatePartySinner(4, listOf(PartyIdentity(fifthIdentity, true))),
        )

        testBase(expected, party)
    }

    @Test
    fun `test with four active identities and one selected ego`() {
        val expected = generatePartyInfoData(
            skillCountByDamage = listOf(9, 12, 15),
            skillCountBySin = listOf(3, 9, 6, 3, 3, 3, 9),
            resistByDamagePotency = listOf(
                InfoPanelDamageResist.Good,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Bad
            ),
            resistBySinPotency = listOf(
                InfoPanelDamageResist.Bad,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Perfect,
            ),
            activeIdentityCount = 4,
            totalIdentityCount = 4
        )

        val party = generateParty(
            generatePartySinner(1, listOf(PartyIdentity(firstIdentity, true))),
            generatePartySinner(
                2,
                listOf(PartyIdentity(secondIdentity, true)),
                listOf(thirdEgo),
                selectedRiskLevel = RiskLevel.TETH
            ),
            generatePartySinner(3, listOf(PartyIdentity(thirdIdentity, true))),
            generatePartySinner(4, listOf(PartyIdentity(fifthIdentity, true))),
        )

        testBase(expected, party)
    }

    @Test
    fun `test with four active identities one inactive and one selected ego`() {
        val expected = generatePartyInfoData(
            skillCountByDamage = listOf(3, 15, 18),
            skillCountBySin = listOf(0, 9, 9, 3, 3, 3, 9),
            resistByDamagePotency = listOf(
                InfoPanelDamageResist.Good,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor
            ),
            resistBySinPotency = listOf(
                InfoPanelDamageResist.Bad,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Poor,
                InfoPanelDamageResist.Normal,
                InfoPanelDamageResist.Perfect,
            ),
            activeIdentityCount = 4,
            totalIdentityCount = 5
        )

        val party = generateParty(
            generatePartySinner(
                1, listOf(
                    PartyIdentity(firstIdentity, false),
                    PartyIdentity(fourthIdentity, true)
                )
            ),
            generatePartySinner(
                2,
                listOf(PartyIdentity(secondIdentity, true)),
                listOf(thirdEgo),
                selectedRiskLevel = RiskLevel.TETH
            ),
            generatePartySinner(3, listOf(PartyIdentity(thirdIdentity, true))),
            generatePartySinner(4, listOf(PartyIdentity(fifthIdentity, true))),
        )

        testBase(expected, party)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun testBase(expected: PartyInfoData, party: Party) {
        val useCase = GetPartyInfoUseCase(repository)


        return runTest {
            val result = useCase.invoke(party)

            assertEquals(expected, result)
        }
    }

    private fun generatePartyInfoData(
        skillCountByDamage: List<Int>,
        skillCountBySin: List<Int>,
        resistByDamagePotency: List<InfoPanelDamageResist>,
        resistBySinPotency: List<InfoPanelDamageResist>,
        activeIdentityCount: Int = 0,
        totalIdentityCount: Int = 0
    ) = PartyInfoData(
        skillCountByDamage = mapOf(
            DamageType.SLASH to skillCountByDamage[0],
            DamageType.PIERCE to skillCountByDamage[1],
            DamageType.BLUNT to skillCountByDamage[2]
        ),
        skillCountBySin = mapOf(
            Sin.WRATH to skillCountBySin[0],
            Sin.LUST to skillCountBySin[1],
            Sin.SLOTH to skillCountBySin[2],
            Sin.GLUTTONY to skillCountBySin[3],
            Sin.GLOOM to skillCountBySin[4],
            Sin.PRIDE to skillCountBySin[5],
            Sin.ENVY to skillCountBySin[6]
        ),
        resistByDamagePotency = mapOf(
            DamageType.SLASH to resistByDamagePotency[0],
            DamageType.PIERCE to resistByDamagePotency[1],
            DamageType.BLUNT to resistByDamagePotency[2]
        ),
        resistBySinPotency = mapOf(
            Sin.WRATH to resistBySinPotency[0],
            Sin.LUST to resistBySinPotency[1],
            Sin.SLOTH to resistBySinPotency[2],
            Sin.GLUTTONY to resistBySinPotency[3],
            Sin.GLOOM to resistBySinPotency[4],
            Sin.PRIDE to resistBySinPotency[5],
            Sin.ENVY to resistBySinPotency[6]
        ),
        activeIdentityCount, totalIdentityCount
    )

    private fun generateParty(vararg sinners: PartySinner) =
        Party(
            0,
            "TEST PARTY",
            sinners.toList()
        )

    private fun generatePartySinner(
        sinnerId: Int,
        identities: List<PartyIdentity> = emptyList(),
        ego: List<Ego> = emptyList(),
        selectedRiskLevel: RiskLevel? = null
    ) =
        PartySinner(
            Sinner(sinnerId, "TEST SINNER #$sinnerId"),
            identities,
            ego,
            selectedRiskLevel
        )
}