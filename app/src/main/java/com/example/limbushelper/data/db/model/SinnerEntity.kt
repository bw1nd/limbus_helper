package com.example.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sinner")
data class SinnerEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)
