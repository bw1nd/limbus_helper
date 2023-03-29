package ua.blackwind.limbushelper.data.db.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Passive
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.domain.sinner.model.Support

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
    val maxDamage: Int,
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
suspend fun IdentityEntity.toIdentity(
    getSkill: suspend (id: Int) -> Skill,
    getPassive: suspend (id: Int) -> Passive,
    getSupport: suspend (id: Int) -> Support
) = Identity(
    id = this.id,
    name = this.name,
    sinnerId = this.sinnerId,
    rarity = this.rarity,
    slashRes = this.slashRes,
    pierceRes = this.pierceRes,
    bluntRes = this.bluntRes,
    maxHp = this.maxHp,
    maxArmor = this.maxArmor,
    maxDamage = this.maxDamage,
    speed = this.speed.split(SPEED_VALUE_SEPARATOR)
        .let { Pair(it.first().toInt(), it.last().toInt()) },
    firstSkill = getSkill(firstSkillId),
    secondSkill = getSkill(secondSkillId),
    thirdSkill = getSkill(thirdSkillId),
    passive = getPassive(supportId),
    support = getSupport(passiveId),
    imageUrl = this.imageUrl
)

private const val SPEED_VALUE_SEPARATOR = "-"
