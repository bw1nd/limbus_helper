package ua.blackwind.limbushelper.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.data.db.AppDatabase
import ua.blackwind.limbushelper.data.db.Dao
import ua.blackwind.limbushelper.data.party.PartyRepository
import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    @Binds
    fun bindPartyRepository(partyRepository: PartyRepository): IPartyRepository

    @Binds
    fun bindSinnerRepository(sinnerRepository: SinnerRepository): ISinnerRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .createFromAsset("app.db")
                .build()

        @Provides
        @Singleton
        fun provideDao(database: AppDatabase): Dao =
            database.dao

    }


}