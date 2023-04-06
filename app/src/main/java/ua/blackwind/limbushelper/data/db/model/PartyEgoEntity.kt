package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import ua.blackwind.limbushelper.domain.common.RiskLevel

@Entity(tableName = "party_ego", primaryKeys = ["sinnerId","partyId","risk"])
data class PartyEgoEntity(
    val partyId: Int,
    val sinnerId: Int,
    val risk: RiskLevel,
    val egoId: Int
)
