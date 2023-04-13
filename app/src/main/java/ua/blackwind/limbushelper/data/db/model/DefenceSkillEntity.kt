package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.DefenceSkill
import ua.blackwind.limbushelper.domain.sinner.model.DefenceSkillType

@Entity(tableName = "defence_skill")
data class DefenceSkillEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: DefenceSkillType,
    val skillPower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val sin: Sin?
)

fun DefenceSkillEntity.toDefenceSkill() =
    DefenceSkill(
        id, name, type, skillPower, coinPower, coinCount, sin
    )
