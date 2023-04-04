package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Query
import ua.blackwind.limbushelper.data.db.model.SkillEntity

interface SkillDao {
    @Query("SELECT * FROM skill WHERE id = :id")
    suspend fun getSkillById(id: Int): SkillEntity

    @Query("SELECT * FROM skill\n" +
            "WHERE id IN (\n" +
            "   SELECT firstSkillId FROM identity WHERE id = :id\n" +
            "   UNION\n" +
            "   SELECT secondSkillId FROM identity WHERE id = :id\n" +
            "   UNION\n" +
            "   SELECT thirdSkillId FROM identity WHERE id = :id\n" +
            ")")
    suspend fun getSkillsByIdentityId(id: Int): List<SkillEntity>
}