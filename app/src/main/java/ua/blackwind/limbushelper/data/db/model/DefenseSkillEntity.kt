package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.DefenseSkill
import ua.blackwind.limbushelper.domain.sinner.model.DefenseSkillType

@Entity(tableName = "defense_skill")
data class DefenseSkillEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: DefenseSkillType,
    val basePower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val sin: Sin?
)

fun DefenseSkillEntity.toDefenceSkill() =
    DefenseSkill(
        id, name, type, basePower, coinPower, coinCount, sin
    )
