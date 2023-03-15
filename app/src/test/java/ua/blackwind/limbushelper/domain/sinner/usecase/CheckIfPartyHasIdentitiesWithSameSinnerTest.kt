package ua.blackwind.limbushelper.domain.sinner.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckIfPartyHasIdentitiesWithSameSinnerTest {

//    private val testParty = Party(
//        1,
//        listOf(
//            1 to false,
//            2 to true,
//            3 to true,
//            5 to true,
//            7 to false
//        )
//    )
//    private val repository = mockk<SinnerRepository>()
//
////    @Test
////    fun `party has no identities with same sinner id returns false`() {
////        val expected = false
////
////        val testIdentitiesBySinnerId = listOf(
////            generateIdentity(8, 5),
////            generateIdentity(9, 5),
////            generateIdentity(10, 5),
////        )
////
////        val useCase = CheckIfPartyHasIdentitiesWithSameSinner(repository)
////        coEvery { repository.getIdentityBySinnerId(any()) } returns testIdentitiesBySinnerId
////
////        val testIdentity = generateIdentity(8, 5)
////        runTest {
////            val result = useCase.invoke(party = testParty, testIdentity)
////            assertEquals(expected, result)
////        }
////        coVerify(exactly = 1) { repository.getIdentityBySinnerId(5) }
////        confirmVerified(repository)
////    }
////
////    @Test
////    fun `party has identities with same sinner id returns true`() {
////        val expected = true
////        val testSinnerId = 2
////        val testIdentity = generateIdentity(2, testSinnerId)
////
////        val testIdentitiesBySinnerId = listOf(
////            generateIdentity(1, testSinnerId),
////            testIdentity,
////            generateIdentity(13, testSinnerId),
////        )
////
////        val useCase = CheckIfPartyHasIdentitiesWithSameSinner(repository)
////
////        coEvery { repository.getIdentityBySinnerId(any()) } returns testIdentitiesBySinnerId
////
////        runTest {
////            val result = useCase.invoke(party = testParty, testIdentity)
////            assertEquals(expected, result)
////        }
////        coVerify(exactly = 1) { repository.getIdentityBySinnerId(testSinnerId) }
////        confirmVerified(repository)
////    }
//
//    private fun generateIdentity(id: Int, sinnerId: Int) = Identity(
//        id = id,
//        name = "test",
//        sinnerId = sinnerId,
//        rarity = 0,
//        slashRes = IdentityDamageResistType.INEFFECTIVE,
//        pierceRes = IdentityDamageResistType.FATAL,
//        bluntRes = IdentityDamageResistType.NORMAL,
//        maxHp = 0,
//        maxArmor = 0,
//        maxDamage = 0,
//        speed = 0 to 0,
//        firstSkill = getDummySkill(),
//        secondSkill = getDummySkill(),
//        thirdSkill = getDummySkill(),
//        passive = Passive(0, 0, SinCost(emptyList()), ""),
//        support = Support(0, 0, SinCost(emptyList()), ""),
//        imageUrl = ""
//    )

    private fun getDummySkill() =
        Skill(1, "test", 0, DamageType.BLUNT, Sin.LUST, 3, 0, 0, 0, emptyList())
}