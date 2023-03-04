package com.example.limbushelper.domain.party

import com.example.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow

interface IPartyRepository {

    fun getParty(): Flow<Party>

    suspend fun addIdentityToParty(identityId: Int, isActive: Boolean)

    suspend fun deleteIdentityFromParty(identityId: Int)

    /**
     * Changes identity with provided Id isActive status to true and previous active identity
     * (if there was one) to false.
     */
    suspend fun changeIdentityActiveStatus(identityId: Int)
}