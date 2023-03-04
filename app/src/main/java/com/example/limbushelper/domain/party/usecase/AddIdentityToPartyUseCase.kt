package com.example.limbushelper.domain.party.usecase

import com.example.limbushelper.data.PartyRepository
import com.example.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class AddIdentityToPartyUseCase @Inject constructor(
    private val repository: PartyRepository
) {
    suspend operator fun invoke(identity: Identity, isActive: Boolean) {
        repository.addIdentityToParty(
            Pair(identity.id, isActive)
        )
    }
}