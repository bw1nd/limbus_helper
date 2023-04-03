package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin

@Entity(tableName = "ego")
data class EgoEntity(
    @PrimaryKey
    val id: Int,
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
