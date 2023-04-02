package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class AddIdentityToPartyUseCase @Inject constructor(
    private val partyRepository: IPartyRepository,
) {
    suspend operator fun invoke(identity: Identity, party: Party) {
        val activeIdentity =
            partyRepository.getActiveIdentityIdForPartyAndSinner(party.id, identity.sinnerId)

        partyRepository.addIdentityToParty(
            party.id, identity
        )
        if (activeIdentity == 0) {
            partyRepository.changeSinnerActiveIdentityForParty(
                partyId = party.id,
                sinnerId = identity.sinnerId,
                identityId = identity.id
            )
        }
    }
}