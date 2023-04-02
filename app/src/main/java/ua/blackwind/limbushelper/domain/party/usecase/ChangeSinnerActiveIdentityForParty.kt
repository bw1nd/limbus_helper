package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import javax.inject.Inject

class ChangeSinnerActiveIdentityForParty @Inject constructor(private val repository: PartyRepository) {
    suspend operator fun invoke(partyId: Int, sinnerId: Int, identityId: Int) {
        val current = repository.getActiveIdentityIdForPartyAndSinner(partyId, sinnerId)
        repository.changeSinnerActiveIdentityForParty(
            partyId = partyId,
            sinnerId = sinnerId,
            identityId = if (current != identityId) identityId else 0
        )
    }
}