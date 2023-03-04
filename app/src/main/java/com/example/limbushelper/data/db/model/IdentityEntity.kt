package com.example.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.limbushelper.domain.IdentityDamageResistType

@Entity(tableName = "identity")
data class IdentityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val sinnerId: Int,
    val rarity: Int,
    val slashRes: IdentityDamageResistType,
    val pierceRes: IdentityDamageResistType,
    val bluntRes: IdentityDamageResistType,
    val maxHp: Int,
    val maxArmor: Int,
    val speed: IntRange,
    val firstSkillId: Int,
    val secondSkillId: Int,
    val thirdSkillId: Int,
    val passiveId: Int,
    val supportId: Int
)
