package com.example.limbushelper.data.db.model

import androidx.room.Entity

@Entity(tableName = "party")
data class PartyEntity(
    val id: Int,
    val identityId: Int,
    val isActive: Boolean
)
