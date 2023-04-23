package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.blackwind.limbushelper.data.db.model.*

interface PartyDao {
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

    @Query("SELECT * FROM party_ego WHERE partyId = :id")
    fun getEgoListByPartyId(id: Int): Flow<List<PartyEgoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEgoToParty(partyEgoEntity: PartyEgoEntity)

    @Delete
    suspend fun removeEgoFromParty(partyEgoEntity: PartyEgoEntity)

    @Query("DELETE FROM party_identity WHERE partyId = :partyId")
    suspend fun removeAllIdentityFromParty(partyId: Int)

    @Query("DELETE FROM party_ego WHERE partyId = :partyId")
    suspend fun removeAllEgoFromParty(partyId: Int)

    @Query("SELECT * FROM party_selected_ego WHERE partyId = :partyId")
    fun getSelectedEgoRiskLevelsForParty(partyId: Int): Flow<List<PartySelectedEgoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun changeSelectedEgoRiskLevelForSinner(entity: PartySelectedEgoEntity)
}