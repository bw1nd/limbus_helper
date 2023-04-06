package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.SinnerEntity

interface SinnerDao {
    @Query("SELECT * FROM sinner ORDER BY id ASC")
    suspend fun getAllSinners(): List<SinnerEntity>

    @Query("SELECT * FROM sinner WHERE id = :id")
    suspend fun getSinnerById(id: Int): SinnerEntity
}