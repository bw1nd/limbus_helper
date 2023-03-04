package com.example.limbushelper.domain.sinner

import com.example.limbushelper.domain.sinner.model.Identity
import com.example.limbushelper.domain.sinner.model.Sinner
import com.example.limbushelper.domain.sinner.model.Skill

interface ISinnerRepository {
    suspend fun getAllSinners(): List<Sinner>

    suspend fun getSinnerById(): List<Sinner>

    suspend fun getAllIdentities(): List<Identity>

    suspend fun getIdentityById(id: Int): Identity

    suspend fun getIdentityBySinnerId(id: Int): Identity

    suspend fun getSkillById(id: Int): Skill

    suspend fun getSkillsByIdentityId(id: Int): List<Skill>
}