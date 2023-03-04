package com.example.limbushelper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
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
abstract class AppDatabase: RoomDatabase() {
    abstract val dao: Dao
}