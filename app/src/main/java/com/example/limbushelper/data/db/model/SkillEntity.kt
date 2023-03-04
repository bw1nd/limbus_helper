package com.example.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.limbushelper.domain.DamageType
import com.example.limbushelper.domain.Effect
import com.example.limbushelper.domain.Sin
import com.example.limbushelper.domain.parseStringToEffectsList
import com.example.limbushelper.domain.sinner.model.Skill

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
