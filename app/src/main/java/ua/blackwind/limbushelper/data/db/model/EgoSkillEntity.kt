package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.parseStringToEffectsList
import ua.blackwind.limbushelper.domain.sinner.model.EgoSkill

private const val EFFECTS_SEPARATOR = ","

@Entity(tableName = "ego_skill")
data class EgoSkillEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dmgType: DamageType,
    val sin: Sin,
    val offense: Int,
    val basePower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val sanityCost: Int,
    val effects: String
)

fun EgoSkillEntity.toEgoSkill() =
    EgoSkill(
        id = id,
        name = name,
        dmgType = dmgType,
        sin = sin,
        offense = offense,
        basePower = basePower,
        coinPower = coinPower,
        coinCount = coinCount,
        sanityCost = sanityCost,
        effects = if (effects.isEmpty()) emptyList() else
            this.effects.split(EFFECTS_SEPARATOR).map {
                it.parseStringToEffectsList()
            }
    )
