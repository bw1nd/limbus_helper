package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.EgoSkillEntity

interface EgoSkillDao {
    @Query("SELECT * FROM ego_skill WHERE id = :id")
    suspend fun getEgoSkillById(id: Int): EgoSkillEntity?
}