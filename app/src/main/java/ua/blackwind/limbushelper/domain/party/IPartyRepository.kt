package ua.blackwind.limbushelper.domain.party


import ua.blackwind.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbushelper.domain.sinner.model.Identity

interface IPartyRepository {

    fun getParty(id: Int): Flow<Party>

    //fun getActiveIdentityListForParty(partyId: Int): Flow<List<Pair<Int, Int>>>

    suspend fun addIdentityToParty(partyId: Int, identity: Identity)

    suspend fun deleteIdentityFromParty(partyId: Int, identity: Identity)

    suspend fun getActiveIdentityIdForPartyAndSinner(partyId: Int, sinnerId: Int): Int

    suspend fun changeSinnerActiveIdentityForParty(partyId: Int, sinnerId: Int, identityId: Int)
}