package ua.blackwind.limbushelper.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.blackwind.limbushelper.data.PartyRepository
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindPartyRepository(partyRepository: PartyRepository): IPartyRepository

    @Binds
    fun bindSinnerRepository(sinnerRepository: SinnerRepository): ISinnerRepository
}