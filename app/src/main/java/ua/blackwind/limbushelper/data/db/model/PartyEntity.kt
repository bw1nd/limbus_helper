package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.data.party.PartyItemType

@Entity(tableName = "party")
data class PartyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)

//fun PartyEntity.toPartyIdentityPair() = Pair(this.id, this.isActive)
