package com.example.limbushelper.data.db.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.limbushelper.domain.IdentityDamageResistType
import com.example.limbushelper.domain.sinner.model.Identity

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
    val speed: String,
    val firstSkillId: Int,
    val secondSkillId: Int,
    val thirdSkillId: Int,
    val passiveId: Int,
    val supportId: Int,
    val imageUrl: String
)

/**
 * @throws NumberFormatException
 */
fun IdentityEntity.toIdentity() = Identity(
    id = this.id,
    name = this.name,
    sinnerId = this.sinnerId,
    rarity = this.rarity,
    slashRes = this.slashRes,
    pierceRes = this.pierceRes,
    bluntRes = this.bluntRes,
    maxHp = this.maxHp,
    maxArmor = this.maxArmor,
    speed = this.speed.split(SPEED_VALUE_SEPARATOR)
        .let { Pair(it.first().toInt(), it.last().toInt()) },
    firstSkillId = this.firstSkillId,
    secondSkillId = this.secondSkillId,
    thirdSkillId = this.thirdSkillId,
    passiveId = this.passiveId,
    supportId = this.supportId,
    imageUrl = this.imageUrl
)

private const val SPEED_VALUE_SEPARATOR = "-"
