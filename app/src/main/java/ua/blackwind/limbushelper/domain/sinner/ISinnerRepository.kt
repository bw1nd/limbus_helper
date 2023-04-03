package ua.blackwind.limbushelper.domain.sinner

import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.model.Skill

interface ISinnerRepository {
    suspend fun getAllSinners(): List<Sinner>

    suspend fun getSinnerById(id: Int): Sinner

    suspend fun getAllIdentities(): List<Identity>

    suspend fun getIdentityById(id: Int): Identity

    suspend fun getIdentityBySinnerId(id: Int): List<Identity>

    suspend fun getAllEgo(): List<Ego>

    suspend fun getEgoById(id: Int): Ego

    suspend fun getSkillById(id: Int): Skill

    suspend fun getSkillsByIdentityId(id: Int): List<Skill>
}