package com.example.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.limbushelper.domain.DamageType
import com.example.limbushelper.domain.Effect
import com.example.limbushelper.domain.Sin

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
    val effects: List<Effect>
)
