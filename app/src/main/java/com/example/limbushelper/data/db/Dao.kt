package com.example.limbushelper.data.db

import androidx.room.*
import androidx.room.Dao
import com.example.limbushelper.data.db.model.IdentityEntity
import com.example.limbushelper.data.db.model.PartyEntity
import com.example.limbushelper.data.db.model.SinnerEntity
import com.example.limbushelper.data.db.model.SkillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM party")
    fun getParty(): Flow<List<PartyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIdentityToParty(partyEntity: PartyEntity)

    @Delete
    fun deleteIdentityFromParty(partyEntity: PartyEntity)

    @Query("SELECT * FROM sinner ORDER BY id ASC")
    suspend fun getAllSinners(): List<SinnerEntity>

    @Query("SELECT * FROM sinner WHERE id = :id")
    suspend fun getSinnerById(id: Int): SinnerEntity

    @Query("SELECT * FROM identity")
    suspend fun getAllIdentities(): List<IdentityEntity>

    @Query("SELECT * FROM identity WHERE id = :id")
    suspend fun getIdentityById(id: Int): IdentityEntity

    @Query("SELECT * FROM identity WHERE sinnerId = :id")
    suspend fun getIdentityBySinnerId(id: Int): IdentityEntity

    @Query("SELECT * FROM skill WHERE id = :id")
    suspend fun getSkillById(id: Int): SkillEntity

    @Query("SELECT * FROM skill WHERE identityId = :id")
    suspend fun getSkillsByIdentityId(id: Int): List<SkillEntity>
}