package com.guymichael.componentapplicationexample.persist.db.table

import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixGenreData
import com.guymichael.reactiveapp.persist.db.model.DoubleListPropertyConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class Table_NetflixGenre(
        //DB id, set as 0 to mark as new. It will auto-update upon persist
        @Id var id: Long = 0
        , @Unique val name: String
        , @Convert(converter = DoubleListPropertyConverter::class, dbType = String::class)
          val titleIds: List<Double>
    ) {



    fun toDataModel(): NetflixGenreData {
        return NetflixGenreData(
            name, titleIds.map { it.toLong() }
        )
    }

    companion object {
        fun from(data: NetflixGenreData): Table_NetflixGenre {
            return Table_NetflixGenre(
                name = data.name,
                titleIds = data.titleIds.map { it.toDouble() }
            )
        }
    }
}