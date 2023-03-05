package ua.blackwind.limbushelper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.blackwind.limbushelper.data.db.model.IdentityEntity
import ua.blackwind.limbushelper.data.db.model.PartyEntity
import ua.blackwind.limbushelper.data.db.model.SinnerEntity
import ua.blackwind.limbushelper.data.db.model.SkillEntity

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

    companion object{
        const val DB_NAME = "app"
    }
}