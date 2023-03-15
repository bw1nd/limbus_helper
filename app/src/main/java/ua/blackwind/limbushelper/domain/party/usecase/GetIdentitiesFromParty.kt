package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class GetIdentitiesFromParty @Inject constructor(
    private val identityRepository: SinnerRepository
) {
    suspend operator fun invoke(party: Party): List<Identity> {
//        val identities = identityRepository.getAllIdentities()
//        return identities
//            .filter { identity -> party.identityList.any { it.first == identity.id } }
        return emptyList()
    }
}