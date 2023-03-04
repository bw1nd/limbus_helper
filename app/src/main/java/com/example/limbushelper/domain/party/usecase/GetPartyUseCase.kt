package com.example.limbushelper.domain.party.usecase

import com.example.limbushelper.data.PartyRepository
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: PartyRepository) {
    operator fun invoke() = repository.getParty()
}