package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.IdentityEntity

interface IdentityDao {
    @Query("SELECT * FROM identity")
    suspend fun getAllIdentities(): List<IdentityEntity>

    @Query("SELECT * FROM identity WHERE id = :id")
    suspend fun getIdentityById(id: Int): IdentityEntity

    @Query("SELECT * FROM identity WHERE sinnerId = :id")
    suspend fun getIdentityBySinnerId(id: Int): List<IdentityEntity>
}