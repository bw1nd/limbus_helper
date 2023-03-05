package ua.blackwind.limbushelper.data

import ua.blackwind.limbushelper.data.db.AppDatabase
import ua.blackwind.limbushelper.data.db.model.IdentityEntity
import ua.blackwind.limbushelper.data.db.model.toIdentity
import ua.blackwind.limbushelper.data.db.model.toSinner
import ua.blackwind.limbushelper.data.db.model.toSkill
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class SinnerRepository @Inject constructor(
    db: AppDatabase
): ISinnerRepository {

    private val dao = db.dao

    override suspend fun getAllSinners(): List<Sinner> {
        return dao.getAllSinners().map { it.toSinner() }
    }

    override suspend fun getSinnerById(id: Int): Sinner {
        return dao.getSinnerById(id).toSinner()
    }

    override suspend fun getAllIdentities(): List<Identity> {
        return dao.getAllIdentities().map {
            identityEntityToIdentity(it)
        }
    }

    override suspend fun getIdentityById(id: Int): Identity {
        return identityEntityToIdentity(dao.getIdentityById(id))
    }

    override suspend fun getIdentityBySinnerId(id: Int): Identity {
        return identityEntityToIdentity(dao.getIdentityById(id))
    }

    override suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    override suspend fun getSkillsByIdentityId(id: Int): List<Skill> {
        return dao.getSkillsByIdentityId(id).map { it.toSkill() }
    }

    private suspend fun identityEntityToIdentity(identityEntity: IdentityEntity): Identity {
        //TODO this function uses dummy data, must add passive and support db tables to implement it
        return identityEntity.toIdentity(
            getSkill = ::getSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
            getSupport = { Support(0, 0, SinCost(listOf(2 to Sin.LUST)), "support") }
        )
    }
}