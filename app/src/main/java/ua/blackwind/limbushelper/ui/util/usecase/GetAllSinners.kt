package ua.blackwind.limbushelper.ui.util.usecase

import ua.blackwind.limbushelper.data.SinnerRepository
import javax.inject.Inject

class GetAllSinners @Inject constructor(private val repository: SinnerRepository) {
    suspend operator fun invoke() =
        repository.getAllSinners()

}