package ua.blackwind.limbushelper.data.db.model

import android.database.sqlite.SQLiteDatabaseCorruptException
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Passive
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import java.util.*

private const val MAP_CONVERTER_DIVIDER = "_"

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
        val sortedMap = TreeMap(value)
        return sortedMap.keys.joinToString(separator = ",").plus(MAP_CONVERTER_DIVIDER)
            .plus(sortedMap.values.joinToString(separator = ","))
    }

    @TypeConverter
    fun toResourceCostMap(value: String): Map<Sin, Int> {
        return value.split(MAP_CONVERTER_DIVIDER).run {
            val keys = getOrNull(0)?.split(",")?.map { it }
            val values = getOrNull(1)?.split(",")?.map { it }

            val res = hashMapOf<Sin, Int>()
            keys?.forEachIndexed { index, s ->
                res[Sin.valueOf(s)] = values?.getOrNull(index)?.toInt()
                    ?: throw SQLiteDatabaseCorruptException("Database data for Ego ResourceCost has been corrupted.")
            }
            res
        }
    }

    @TypeConverter
    fun fromSinResistancesMap(value: Map<Sin, EgoSinResistType>): String {
        val sortedMap = TreeMap(value)
        return sortedMap.keys.joinToString(separator = ",").plus(MAP_CONVERTER_DIVIDER)
            .plus(sortedMap.values.joinToString(separator = ","))
    }

    @TypeConverter
    fun toSinResistancesMap(value: String): Map<Sin, EgoSinResistType> {
        return value.split(MAP_CONVERTER_DIVIDER).run {
            val keys = getOrNull(0)?.split(",")?.map { it }
            val values = getOrNull(1)?.split(",")?.map { it }

            val res = hashMapOf<Sin, EgoSinResistType>()
            keys?.forEachIndexed { index, s ->
                res[Sin.valueOf(s)] = EgoSinResistType.valueOf(
                    values?.getOrNull(index)
                        ?: throw SQLiteDatabaseCorruptException("Database data for Ego SinResistances has been corrupted.")
                )
            }
            res
        }
    }
}
