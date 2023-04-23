package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.parseStringToEffectsList
import ua.blackwind.limbushelper.domain.sinner.model.Skill

@Entity(tableName = "skill")
data class SkillEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dmgType: DamageType,
    val sin: Sin,
    val copiesCount: Int,
    val basePower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val effects: String
)

fun SkillEntity.toSkill() = Skill(
    id = this.id,
    name = this.name,
    dmgType = this.dmgType,
    sin = this.sin,
    copiesCount = this.copiesCount,
    basePower = this.basePower,
    coinPower = this.coinPower,
    coinCount = this.coinCount,
    effects = if (this.effects.isEmpty()) emptyList() else
        this.effects.split(EFFECTS_SEPARATOR).map {
            it.parseStringToEffectsList()
        }
)

private const val EFFECTS_SEPARATOR = ","
