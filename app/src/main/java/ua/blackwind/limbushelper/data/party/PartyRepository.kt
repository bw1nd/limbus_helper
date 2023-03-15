package ua.blackwind.limbushelper.data.party

import ua.blackwind.limbushelper.data.db.AppDatabase
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.blackwind.limbushelper.data.db.model.*
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class PartyRepository @Inject constructor(
    db: AppDatabase
): IPartyRepository {
    private val dao = db.dao

    override fun getParty(id: Int): Flow<Party> {
        return dao.getIdentityListByPartyId(DEFAULT_PARTY_ID).map { identities ->
            val party = dao.getParty(DEFAULT_PARTY_ID)
            Party(DEFAULT_PARTY_ID, party.name, identities.map {
                val identity = dao.getIdentityById(it.identityId)
                identityEntityToIdentity(identity)
            })
        }
    }

    override suspend fun addIdentityToParty(partyId: Int, identity: Identity) {
        val entity = PartyIdentityEntity(DEFAULT_PARTY_ID, partyId, identity.id)
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

    private suspend fun identityEntityToIdentity(identityEntity: IdentityEntity): Identity {
        //TODO this function uses dummy data, must add passive and support db tables to implement it
        return identityEntity.toIdentity(
            getSkill = ::getSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
            getSupport = { Support(0, 0, SinCost(listOf(2 to Sin.LUST)), "support") }
        )
    }

    private suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    companion object {
        private const val DEFAULT_PARTY_ID = 1
    }
}