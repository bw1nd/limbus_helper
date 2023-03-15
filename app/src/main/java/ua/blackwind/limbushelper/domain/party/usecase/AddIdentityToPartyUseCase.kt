package ua.blackwind.limbushelper.domain.party.usecase


import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class AddIdentityToPartyUseCase @Inject constructor(
    private val partyRepository: IPartyRepository,
    private val sinnerRepository: SinnerRepository
) {
    suspend operator fun invoke(identity: Identity, party: Party) {
//        val isActive = sinnerRepository.getIdentityBySinnerId(identity.sinnerId)
//            .any { filteredIdentity -> party.identityList.any { it.first == filteredIdentity.id } }
//
//        partyRepository.addIdentityToParty(
//            Pair(identity.id, isActive)
//        )
    }
}