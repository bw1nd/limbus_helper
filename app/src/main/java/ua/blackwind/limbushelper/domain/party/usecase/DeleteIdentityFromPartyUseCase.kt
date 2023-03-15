package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class DeleteIdentityFromPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(identity: Identity, party: Party) {
        val activeIdentity =
            repository.getActiveIdentityIdForPartyAndSinner(party.id, identity.sinnerId)
        val otherIdentitiesForThisSinner =
            party.identityList.filter { it.sinnerId == identity.sinnerId && it.id != identity.id }

        if (activeIdentity == identity.id) {
            repository.changeSinnerActiveIdentityForParty(
                partyId = party.id,
                sinnerId = identity.sinnerId,
                identityId = if (otherIdentitiesForThisSinner.isNotEmpty())
                    otherIdentitiesForThisSinner.first().id else NO_ACTIVE_SINNER_IDENTITY_IN_PARTY
            )
        }

        repository.deleteIdentityFromParty(party.id, identity)
    }

    companion object {
        private const val NO_ACTIVE_SINNER_IDENTITY_IN_PARTY = 0
    }
}