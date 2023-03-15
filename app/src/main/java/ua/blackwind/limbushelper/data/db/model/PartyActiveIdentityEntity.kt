package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "party_active")
data class PartyActiveIdentityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val partyId: Int,
    val sinnerId: Int,
    val identityId: Int
)
