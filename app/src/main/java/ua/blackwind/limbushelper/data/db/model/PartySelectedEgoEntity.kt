package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import ua.blackwind.limbushelper.domain.common.RiskLevel

@Entity(tableName = "party_selected_ego", primaryKeys = ["partyId", "sinnerId"])
data class PartySelectedEgoEntity(
    val partyId: Int,
    val sinnerId: Int,
    val risk: RiskLevel?
)
