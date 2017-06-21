package com.mylb.mylogbook.data.database.repository

import com.mylb.mylogbook.data.database.dao.CarDao
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import io.reactivex.Flowable
import timber.log.Timber

class RoomCarRepository(private val dao: CarDao) : LocalCarRepository<Car> {

    override fun insert(resources: List<Car>) {
        Timber.d("Inserting %d cars into local database", resources.size)

        if (resources.isEmpty()) return

        dao.insert(*resources.toTypedArray())
    }

    override fun update(resources: List<Car>) {
        Timber.d("Updating %d cars in local database", resources.size)

        if (resources.isEmpty()) return

        resources.forEach {
            Timber.d("Updating (id: %d, remoteId: %d)", it.id, it.remoteId)

            it.updatedAt = Network.now
        }

        dao.update(*resources.toTypedArray())
    }

    override fun delete(resources: List<Car>) {
        Timber.d("Deleting %d cars in local database", resources.size)

        if (resources.isEmpty()) return

        resources.forEach {
            Timber.d("Deleting (id: %d, remoteId: %d)", it.id, it.remoteId)

            it.deletedAt = Network.now
        }

        dao.update(*resources.toTypedArray())
    }

    override fun all(): Flowable<List<Car>> = dao.all()
            .doOnNext { Timber.d("Loaded %d cars from local database", it.size) }

    override fun setRemoteId(id: Int, remoteId: Int) {
        Timber.d("Setting (remoteId: %d) for car (id: %d)", remoteId, id)

        dao.updateRemoteId(id, remoteId)
        dao.updateTripsRemoteCarId(id, remoteId)
    }

}
