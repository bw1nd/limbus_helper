package ua.blackwind.limbushelper.data.party

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ua.blackwind.limbushelper.data.db.dao.Dao
import ua.blackwind.limbushelper.data.db.model.IdentityEntity
import ua.blackwind.limbushelper.data.db.model.PartyIdentityEntity
import ua.blackwind.limbushelper.data.db.model.toIdentity
import ua.blackwind.limbushelper.data.db.model.toSkill
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.DEFAULT_PARTY_ID
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class PartyRepository @Inject constructor(
    private val dao: Dao,
) : IPartyRepository {

    override fun getParty(id: Int): Flow<Party> {
        return dao.getIdentityListByPartyId(DEFAULT_PARTY_ID)
            .combine(
                dao.getActiveIdentityListForParty(
                    DEFAULT_PARTY_ID
                )
            ) { identities, activeIdentities ->
                val party = dao.getParty(DEFAULT_PARTY_ID)

                Party(party.id, party.name, identities.map { entity ->
                    val identity = dao.getIdentityById(entity.identityId)
                    PartyIdentity(
                        identityEntityToIdentity(identity),
                        identity.id in activeIdentities.map { it.identityId }
                    )
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
        Log.d("PARTY", "partyId: $partyId sinner: $sinnerId identity: $identityId")
        val current = dao.getActiveIdentityBySinnerAndPartyId(partyId, sinnerId)
        dao.changeActiveIdentity(
            current.copy(identityId = identityId)
        )
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

    private suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    companion object {
        private const val NEW_VALUE_ID = 0
    }
}