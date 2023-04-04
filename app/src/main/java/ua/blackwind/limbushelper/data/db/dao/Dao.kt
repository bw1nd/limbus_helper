package ua.blackwind.limbushelper.data.db.dao

import androidx.room.Dao

@Dao
interface Dao: SinnerDao, IdentityDao, EgoDao, SkillDao, EgoSkillDao, PartyDao