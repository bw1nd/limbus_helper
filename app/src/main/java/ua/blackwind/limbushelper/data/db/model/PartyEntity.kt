package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.data.party.PartyItemType

@Entity(tableName = "party")
data class PartyEntity(
    @PrimaryKey
    val id: Int,
    val type: PartyItemType,
    val isActive: Boolean
)

fun PartyEntity.toPartyIdentityPair() = Pair(this.id, this.isActive)
