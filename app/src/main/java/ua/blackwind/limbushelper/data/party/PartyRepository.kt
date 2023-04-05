package ua.blackwind.limbushelper.data.party

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ua.blackwind.limbushelper.data.db.dao.Dao
import ua.blackwind.limbushelper.data.db.model.*
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class PartyRepository @Inject constructor(
    private val dao: Dao,
): IPartyRepository {

    override fun getParty(id: Int): Flow<Party> {
        return dao.getIdentityListByPartyId(DEFAULT_PARTY_ID)
            .combine(
                dao.getActiveIdentityListForParty(
                    DEFAULT_PARTY_ID
                )
            ) { identities, activeIdentities ->
                identities to activeIdentities
            }.combine(dao.getEgoListByPartyId(DEFAULT_PARTY_ID)) { identities, ego ->
                val party = dao.getParty(DEFAULT_PARTY_ID)

                Party(party.id, party.name, identities.first.map { entity ->
                    val identity = dao.getIdentityById(entity.identityId)
                    PartyIdentity(
                        identityEntityToIdentity(identity),
                        identity.id in identities.second.map { it.identityId }
                    )
                },
                    ego.filter { it.egoId != NO_EGO_FOR_THIS_RISK_SINNER_ADDED }.map { entity ->
                        egoEntityToEgo(dao.getEgoById(entity.egoId))
                    })
            }
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

    private suspend fun identityEntityToIdentity(identityEntity: IdentityEntity): Identity {
        //TODO this function uses dummy data, must add passive and support db tables to implement it
        return identityEntity.toIdentity(
            getSkill = ::getSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
            getSupport = { Support(0, 0, SinCost(listOf(2 to Sin.LUST)), "support") }
        )
    }

    private suspend fun egoEntityToEgo(egoEntity: EgoEntity): Ego {
        return egoEntity.toEgo(
            ::getEgoSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
        )
    }

    private suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    private suspend fun getEgoSkillById(id: Int): EgoSkill? {
        return dao.getEgoSkillById(id)?.toEgoSkill()
    }

    companion object {
        private const val NEW_VALUE_ID = 0
        private const val NO_EGO_FOR_THIS_RISK_SINNER_ADDED = 0
    }
}