package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.parseStringToEffectsList
import ua.blackwind.limbushelper.domain.sinner.model.Skill

@Entity(tableName = "skill")
data class SkillEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val identityId: Int,
    val dmgType: DamageType,
    val sin: Sin,
    val copiesCount: Int,
    val baseDie: Int,
    val coinBonus: Int,
    val coinCount: Int,
    val effects: String
)

fun SkillEntity.toSkill() = Skill(
    id = this.id,
    name = this.name,
    identityId = this.identityId,
    dmgType = this.dmgType,
    sin = this.sin,
    copiesCount = this.copiesCount,
    baseDie = this.baseDie,
    coinBonus = this.coinBonus,
    coinCount = this.coinCount,
    effects = this.effects.split(EFFECTS_SEPARATOR).map { it.parseStringToEffectsList() }
)

private const val EFFECTS_SEPARATOR = ","
