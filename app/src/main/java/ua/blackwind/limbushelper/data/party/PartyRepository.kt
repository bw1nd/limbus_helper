package ua.blackwind.limbushelper.data.party

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.data.db.dao.Dao
import ua.blackwind.limbushelper.data.db.model.*
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.party.model.PartySinner
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class PartyRepository @Inject constructor(
    private val dao: Dao,
    private val sinnerRepository: SinnerRepository
): IPartyRepository {

    override fun getParty(id: Int): Flow<Party> {
        return combine(
            dao.getIdentityListByPartyId(DEFAULT_PARTY_ID),
            dao.getActiveIdentityListForParty(DEFAULT_PARTY_ID),
            dao.getEgoListByPartyId(DEFAULT_PARTY_ID),
            dao.getSelectedEgoRiskLevelsForParty(DEFAULT_PARTY_ID)
        ) { identities, active, egos, selected ->
            val party = dao.getParty(DEFAULT_PARTY_ID)
            val sinners = dao.getAllSinners()
            val partyIdentities = identities.map { identityEntity ->
                val identity = sinnerRepository.getIdentityById(identityEntity.identityId)
                PartyIdentity(
                    identity,
                    identity.id in active.map { it.identityId })
            }
            val partyEgo = egos.map { dao.getEgoById(it.egoId) }

            Party(party.id, party.name,
                sinners.map { sinnerEntity ->
                    PartySinner(
                        sinnerEntity.toSinner(),
                        partyIdentities.filter { it.identity.sinnerId == sinnerEntity.id },
                        partyEgo.filter { it.sinnerId == sinnerEntity.id }
                            .map { egoEntityToEgo(it) },
                        selected
                            .find { it.sinnerId == sinnerEntity.id }?.risk
                    )
                }
            )
        }
    }

    override suspend fun changeSinnerSelectedEgoRisk(
        partyId: Int,
        sinnerId: Int,
        riskLevel: RiskLevel?
    ) {
        dao.changeSelectedEgoRiskLevelForSinner(
            PartySelectedEgoEntity(
                partyId, sinnerId, riskLevel
            )
        )
    }

    override suspend fun addIdentityToParty(partyId: Int, identity: Identity) {
        val entity = PartyIdentityEntity(NEW_VALUE_ID, partyId, identity.id)
        dao.addIdentityToParty(entity)
    }

    override suspend fun deleteIdentityFromParty(partyId: Int, identity: Identity) {
        val entity = dao.getPartyIdentityByIdentityId(identity.id, partyId)
        dao.deleteIdentityFromParty(
            entity
        )
    }

    override suspend fun changeSinnerActiveIdentityForParty(
        partyId: Int,
        sinnerId: Int,
        identityId: Int
    ) {
        val current = dao.getActiveIdentityBySinnerAndPartyId(partyId, sinnerId)
        dao.changeActiveIdentity(
            current.copy(identityId = identityId)
        )
    }

    override suspend fun addEgoToParty(partyId: Int, ego: Ego) {
        val entity = PartyEgoEntity(partyId, ego.sinnerId, ego.risk, ego.id)
        dao.addEgoToParty(entity)
    }

    override suspend fun removeEgoFromParty(partyId: Int, ego: Ego) {
        val entity = PartyEgoEntity(
            partyId = partyId,
            sinnerId = ego.sinnerId,
            risk = ego.risk,
            egoId = ego.id
        )
        dao.removeEgoFromParty(entity)
    }

    override suspend fun getActiveIdentityIdForPartyAndSinner(partyId: Int, sinnerId: Int): Int {
        return dao.getActiveIdentityBySinnerAndPartyId(partyId, sinnerId).identityId
    }

    override suspend fun removeAllIdentityFromParty(partyId: Int) {
        dao.removeAllIdentityFromParty(partyId)
    }

    override suspend fun removeAllEgoFromParty(partyId: Int) {
        dao.removeAllEgoFromParty(partyId)
    }

    private suspend fun egoEntityToEgo(egoEntity: EgoEntity): Ego {
        return egoEntity.toEgo(
            ::getEgoSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
        )
    }

    private suspend fun getEgoSkillById(id: Int): EgoSkill? {
        return dao.getEgoSkillById(id)?.toEgoSkill()
    }

    companion object {
        private const val NEW_VALUE_ID = 0
    }
}