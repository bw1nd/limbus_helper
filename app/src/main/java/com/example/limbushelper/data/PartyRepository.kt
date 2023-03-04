package com.example.limbushelper.data

import com.example.limbushelper.data.db.AppDatabase
import com.example.limbushelper.data.db.model.PartyEntity
import com.example.limbushelper.data.db.model.toPartyIdentityPair
import com.example.limbushelper.domain.party.IPartyRepository
import com.example.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartyRepository @Inject constructor(
    db: AppDatabase
): IPartyRepository {
    private val dao = db.dao

    override fun getParty(): Flow<Party> {
        return dao.getParty().map { list ->
            Party(PLACEHOLDER_PARTY_ID, list.map { it.toPartyIdentityPair() })
        }
    }

    /**
     * this method is used for both adding new identities and changing status of old with replace.
     */
    override suspend fun addIdentityToParty(identity: Pair<Int, Boolean>) {
        dao.addIdentityToParty(PartyEntity(identity.first, identity.second))
    }

    override suspend fun deleteIdentityFromParty(identity: Pair<Int, Boolean>) {
        dao.deleteIdentityFromParty(PartyEntity(identity.first, identity.second))
    }

    companion object {
        private const val PLACEHOLDER_PARTY_ID = 0
    }
}