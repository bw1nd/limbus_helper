package com.example.limbushelper.domain.party

import com.example.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow

interface IPartyRepository {

    fun getParty(): Flow<Party>

    /**
     * @param identity represents party identity by <id, isActive> pair.
     */
    suspend fun addIdentityToParty(identity: Pair<Int, Boolean>)

    /**
     * @param identity represents party identity by <id, isActive> pair.
     */
    suspend fun deleteIdentityFromParty(identity: Pair<Int, Boolean>)
}