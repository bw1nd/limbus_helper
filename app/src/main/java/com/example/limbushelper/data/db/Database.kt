package com.example.limbushelper.data.db

import androidx.room.Database
import com.example.limbushelper.data.db.model.IdentityEntity
import com.example.limbushelper.data.db.model.PartyEntity
import com.example.limbushelper.data.db.model.SinnerEntity
import com.example.limbushelper.data.db.model.SkillEntity

@Database(
    entities = [
        PartyEntity::class,
        SinnerEntity::class,
        IdentityEntity::class,
        SkillEntity::class
    ],
    version = 1
)
abstract class Database {
    abstract val dao: Dao
}