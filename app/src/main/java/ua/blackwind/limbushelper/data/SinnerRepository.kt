package ua.blackwind.limbushelper.data

import ua.blackwind.limbushelper.data.db.AppDatabase
import ua.blackwind.limbushelper.data.db.model.toIdentity
import ua.blackwind.limbushelper.data.db.model.toSinner
import ua.blackwind.limbushelper.data.db.model.toSkill
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.model.Skill
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
        return dao.getAllIdentities().map { it.toIdentity() }
    }

    override suspend fun getIdentityById(id: Int): Identity {
        return dao.getIdentityById(id).toIdentity()
    }

    override suspend fun getIdentityBySinnerId(id: Int): Identity {
        return dao.getIdentityBySinnerId(id).toIdentity()
    }

    override suspend fun getSkillById(id: Int): Skill {
        return dao.getSkillById(id).toSkill()
    }

    override suspend fun getSkillsByIdentityId(id: Int): List<Skill> {
        return dao.getSkillsByIdentityId(id).map { it.toSkill() }
    }
}