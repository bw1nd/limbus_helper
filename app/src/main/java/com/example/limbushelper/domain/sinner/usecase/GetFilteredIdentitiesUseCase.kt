package com.example.limbushelper.domain.sinner.usecase

import com.example.limbushelper.domain.DamageType
import com.example.limbushelper.domain.Effect
import com.example.limbushelper.domain.Sin
import com.example.limbushelper.domain.sinner.ISinnerRepository
import com.example.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class GetFilteredIdentitiesUseCase @Inject constructor(private val repository: ISinnerRepository) {
    suspend operator fun invoke(filter: IdentityFilter): List<Identity> {
        //TODO this is dummy implementation, should be changed to actual filtering
        return repository.getAllIdentities()
    }
}

data class IdentityFilter(
    val resist: FilterResistSetArg,
    val skills: FilterSkillsSetArg,
    val effects: List<Effect>
)

data class FilterResistSetArg(
    val ineffective: FilterDamageTypeArg,
    val normal: FilterDamageTypeArg,
    val fatal: FilterDamageTypeArg
)

data class FilterSkillsSetArg(
    val first: FilterSkillArg,
    val second: FilterSkillArg,
    val third: FilterSkillArg
)

data class FilterSkillArg(
    val damageType: FilterDamageTypeArg,
    val sin: Sin
)

sealed class FilterDamageTypeArg {
    object Empty: FilterDamageTypeArg()
    data class Type(
        val type: DamageType
    ): FilterDamageTypeArg()
}