package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "party")
data class PartyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
