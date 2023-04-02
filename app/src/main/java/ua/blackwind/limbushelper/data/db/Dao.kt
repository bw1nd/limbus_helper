package ua.blackwind.limbushelper.data.db

import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbushelper.data.db.model.*

// TODO : Refactor this global DAO into several other DAOs (e.g: `SinnerDao`, `PartyDao`)
@Dao
interface Dao {
    @Query("SELECT * FROM party WHERE id = :id")
    suspend fun getParty(id: Int): PartyEntity

    @Query("SELECT * FROM party_identity WHERE partyId = :id")
    fun getIdentityListByPartyId(id: Int): Flow<List<PartyIdentityEntity>>

    @Insert
    suspend fun addIdentityToParty(entity: PartyIdentityEntity)

    @Delete
    suspend fun deleteIdentityFromParty(entity: PartyIdentityEntity)

    @Query("SELECT * FROM party_identity WHERE identityId = :identityId AND partyId = :partyId")
    suspend fun getPartyIdentityByIdentityId(identityId: Int, partyId: Int): PartyIdentityEntity

    @Query("SELECT * FROM party_active WHERE partyId = :partyId AND sinnerId = :sinnerId")
    suspend fun getActiveIdentityBySinnerAndPartyId(
        partyId: Int,
        sinnerId: Int
    ): PartyActiveIdentityEntity

    @Query("SELECT * FROM party_active WHERE partyId = :partyId")
    fun getActiveIdentityListForParty(
        partyId: Int
    ): Flow<List<PartyActiveIdentityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun changeActiveIdentity(active: PartyActiveIdentityEntity)

    @Query("SELECT * FROM sinner ORDER BY id ASC")
    suspend fun getAllSinners(): List<SinnerEntity>

    @Query("SELECT * FROM sinner WHERE id = :id")
    suspend fun getSinnerById(id: Int): SinnerEntity

    @Query("SELECT * FROM identity")
    suspend fun getAllIdentities(): List<IdentityEntity>

    @Query("SELECT * FROM identity WHERE id = :id")
    suspend fun getIdentityById(id: Int): IdentityEntity

    @Query("SELECT * FROM identity WHERE sinnerId = :id")
    suspend fun getIdentityBySinnerId(id: Int): List<IdentityEntity>

    @Query("SELECT * FROM skill WHERE id = :id")
    suspend fun getSkillById(id: Int): SkillEntity

    @Query("SELECT * FROM skill WHERE identityId = :id")
    suspend fun getSkillsByIdentityId(id: Int): List<SkillEntity>
}