package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.SkillEntity

interface SkillDao {
    @Query("SELECT * FROM skill WHERE id = :id")
    suspend fun getSkillById(id: Int): SkillEntity

    @Query("SELECT * FROM skill WHERE identityId = :id")
    suspend fun getSkillsByIdentityId(id: Int): List<SkillEntity>
}