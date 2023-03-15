package ua.blackwind.limbushelper.domain.sinner.usecase

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

/**
 * Checks if Party has any identities with same sinner as argument Identity.
 */
class CheckIfPartyHasIdentitiesWithSameSinner @Inject constructor(
    private val sinnerRepository: SinnerRepository
) {
    suspend operator fun invoke(party: Party, identity: Identity): Boolean {
        val identitiesBySinner = sinnerRepository.getIdentityBySinnerId(identity.sinnerId)
            .filter { filteredIdentity -> party.identityList.any { it.first == filteredIdentity.id } }

        return identitiesBySinner.isNotEmpty()
    }
}