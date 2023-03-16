package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import javax.inject.Inject

class GetSinnerActiveIdentityIdForParty @Inject constructor(
    private val repository: PartyRepository
) {
    suspend operator fun invoke(sinnerId: Int, partyId: Int): Int {
        return repository.getActiveIdentityIdForPartyAndSinner(partyId, sinnerId)
    }
}