package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.sinner.model.Identity

@Entity(tableName = "party_identity")
data class PartyIdentityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val partyId: Int,
    val identityId: Int
)