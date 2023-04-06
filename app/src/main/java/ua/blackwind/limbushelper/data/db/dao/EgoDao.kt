package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.EgoEntity

interface EgoDao {
    @Query("SELECT * FROM ego")
    suspend fun getAllEgo(): List<EgoEntity>

    @Query("SELECT * FROM ego WHERE id = :id")
    suspend fun getEgoById(id: Int): EgoEntity
}