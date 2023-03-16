package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import javax.inject.Inject

class ChangeSinnerActiveIdentityForParty @Inject constructor(private val repository: PartyRepository) {
    suspend operator fun invoke(partyId: Int, sinnerId: Int, identityId: Int) {
        repository.changeSinnerActiveIdentityForParty(
            partyId = partyId,
            sinnerId = sinnerId,
            identityId = identityId
        )
    }
}