package ua.blackwind.limbushelper.data

import ua.blackwind.limbushelper.data.db.dao.Dao
import ua.blackwind.limbushelper.data.db.model.*
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.*
import javax.inject.Inject

class SinnerRepository @Inject constructor(
    private val dao: Dao,
): ISinnerRepository {

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

    override suspend fun getIdentityBySinnerId(id: Int): List<Identity> {
        return dao.getIdentityBySinnerId(id).map { identityEntityToIdentity(it) }
    }

    override suspend fun getAllEgo(): List<Ego> {
        //TODO this function uses dummy data, must add passive db table to implement it
        return dao.getAllEgo().map {
            it.toEgo(
                getSkill = ::getEgoSkillById,
                getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") },
            )
        }
    }

    override suspend fun getEgoById(id: Int): Ego {
        //TODO this function uses dummy data, must add passive db table to implement it
        return dao.getEgoById(id).toEgo(
            getSkill = ::getEgoSkillById,
            getPassive = { Passive(0, 0, SinCost(listOf(1 to Sin.WRATH)), "passive") })
    }

    override suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    override suspend fun getSkillsByIdentityId(id: Int): List<Skill> {
        return dao.getSkillsByIdentityId(id).map { it.toSkill() }
    }

    override suspend fun getEgoSkillById(id: Int): EgoSkill? {
        return dao.getEgoSkillById(id)?.toEgoSkill()
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