package ua.blackwind.limbushelper.domain.party

import android.provider.ContactsContract.CommonDataKinds.Identity
import ua.blackwind.limbushelper.domain.party.model.Party
import kotlinx.coroutines.flow.Flow

interface IPartyRepository {

    fun getParty(id: Int): Flow<Party>

    /**
     * @param identity represents party identity by <id, isActive> pair.
     */
    suspend fun addIdentityToParty(partyId: Int, identity: Identity)

    /**
     * @param identity represents party identity by <id, isActive> pair.
     */
    suspend fun deleteIdentityFromParty(partyId: Int, identity: Identity)

    suspend fun changeSinnerActiveIdentityForParty(partyId: Int, sinnerId: Int, identityId: Int)
}