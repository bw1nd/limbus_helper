package ua.blackwind.limbushelper.data.db.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.sinner.model.*

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
    val hp: Int,
    val defense: Int,
    val offense: Int,
    val speed: String,
    val firstSkillId: Int,
    val secondSkillId: Int,
    val thirdSkillId: Int,
    val defenceSkillId: Int,
    val passiveId: Int,
    val supportId: Int,
    val imageUrl: String
)

/**
 * @throws NumberFormatException
 */
suspend fun IdentityEntity.toIdentity(
    getSkill: suspend (id: Int) -> Skill,
    getDefenseSkill: suspend (id: Int) -> DefenseSkill,
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
    hp = this.hp,
    defense = this.defense,
    offense = this.offense,
    speed = this.speed.split(SPEED_VALUE_SEPARATOR)
        .let { Pair(it.first().toInt(), it.last().toInt()) },
    firstSkill = getSkill(firstSkillId),
    secondSkill = getSkill(secondSkillId),
    thirdSkill = getSkill(thirdSkillId),
    defenseSkill = getDefenseSkill(defenceSkillId),
    passive = getPassive(supportId),
    support = getSupport(passiveId),
    imageUrl = this.imageUrl
)

private const val SPEED_VALUE_SEPARATOR = "-"
