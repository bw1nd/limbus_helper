package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "party")
data class PartyEntity(
    @PrimaryKey
    val id: Int,
    val isActive: Boolean
)

fun PartyEntity.toPartyIdentityPair() = Pair(this.id, this.isActive)
