package com.guymichael.componentapplicationexample.store.datatype

import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixGenreData
import com.guymichael.componentapplicationexample.persist.db.table.Table_NetflixGenre
import com.guymichael.componentapplicationexample.store.reducers.MainStoreDataType
import com.guymichael.reactiveapp.persist.db.DbLogic


object DataTypeNetflixGenre : MainStoreDataType<NetflixGenreData>() {

    override fun getSchemaId(d: NetflixGenreData): String = d.name //a unique identifier

    override fun persistOrThrow(data: List<NetflixGenreData>) {
        DbLogic.persist(data.map(Table_NetflixGenre.Companion::from))
    }

    override fun getPersistedData(): List<NetflixGenreData>? {
        return DbLogic.getAll(Table_NetflixGenre::class.java).map { it.toDataModel() }
    }

    override fun removeFromPersistOrThrow(data: List<NetflixGenreData>) {
        DbLogic.remove(data.map(Table_NetflixGenre.Companion::from))
    }

    override fun clearPersistOrThrow() {
        DbLogic.removeAll(Table_NetflixGenre::class)
    }
}