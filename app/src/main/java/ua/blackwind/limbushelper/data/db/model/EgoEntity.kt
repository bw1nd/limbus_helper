package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Passive
import ua.blackwind.limbushelper.domain.sinner.model.Skill

private const val MAP_CONVERTER_PAIR_DIVIDER = "_"
private const val MAP_CONVERTER_ITEM_DIVIDER = ","

@Entity(tableName = "ego")
data class EgoEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val sinnerId: Int,
    val risk: RiskLevel,
    val awakeningSkillId: Int,
    val corrosionSkillId: Int,
    val passiveId: Int,
    val resourceCost: Map<Sin, Int>,
    val sanityCost: Int,
    val sinResistances: Map<Sin, EgoSinResistType>,
    val imageUrl: String
)

suspend fun EgoEntity.toEgo(
    getSkill: suspend (id: Int) -> Skill,
    getPassive: suspend (id: Int) -> Passive,
): Ego {
    val awakeningSkill = getSkill(awakeningSkillId)
    val corrosionSkill = getSkill(corrosionSkillId)
    val passive = getPassive(passiveId)
    return Ego(
        id = id,
        name = name,
        sinnerId = sinnerId,
        risk = risk,
        awakeningSkill = awakeningSkill,
        corrosionSkill = corrosionSkill,
        passive = passive,
        resourceCost = resourceCost,
        sanityCost = sanityCost,
        sinResistances = sinResistances,
        imageUrl = imageUrl
    )
}

class RoomTypeConverters {
    @TypeConverter
    fun fromResourceCostMap(value: Map<Sin, Int>): String {
        throw NotImplementedError("Normally this method should never be used")
    }

    @TypeConverter
    fun toResourceCostMap(value: String): Map<Sin, Int> {
        return value.split(MAP_CONVERTER_ITEM_DIVIDER).let { list ->
            try {
                list.associate { string ->
                    val split = string.split(MAP_CONVERTER_PAIR_DIVIDER)

                    Sin.valueOf(split.first()) to split.last().toInt()
                }
            } catch (e: Exception) {
                throw Exception("Database Ego data corrupted for: $value", e)
            }
        }
    }

    @TypeConverter
    fun fromSinResistancesMap(value: Map<Sin, EgoSinResistType>): String {
        throw NotImplementedError("Normally this method should never be used")
    }

    @TypeConverter
    fun toSinResistancesMap(value: String): Map<Sin, EgoSinResistType> {
        return value.split(MAP_CONVERTER_ITEM_DIVIDER).let { list ->
            try {
                list.associate { string ->
                    val split = string.split(MAP_CONVERTER_PAIR_DIVIDER)
                    Sin.valueOf(split.first()) to EgoSinResistType.valueOf(split.last())
                }
            } catch (e: Exception) {
                throw Exception("Database Ego data corrupted for: $value", e)
            }
        }
    }
}