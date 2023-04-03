package ua.blackwind.limbushelper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.blackwind.limbushelper.data.db.dao.Dao
import ua.blackwind.limbushelper.data.db.model.*

@Database(
    entities = [
        SinnerEntity::class,
        IdentityEntity::class,
        SkillEntity::class,
        PartyEntity::class,
        PartyIdentityEntity::class,
        PartyActiveIdentityEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract val dao: Dao

    companion object {
        const val DB_NAME = "app"
    }
}