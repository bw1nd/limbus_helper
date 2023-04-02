package ua.blackwind.limbushelper.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.blackwind.limbushelper.domain.sinner.model.Sinner

@Entity(tableName = "sinner")
data class SinnerEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)

fun SinnerEntity.toSinner() = Sinner(
    id = this.id,
    name = this.name
)
