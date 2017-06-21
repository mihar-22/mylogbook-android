package com.mylb.mylogbook.data.database.repository

import com.mylb.mylogbook.data.database.dao.TripDao
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import io.reactivex.Flowable
import timber.log.Timber

class RoomTripRepository(private val dao: TripDao) : LocalTripRepository<Trip> {

    override fun insert(resources: List<Trip>) {
        Timber.d("Inserting %d trips into local database", resources.size)

        resources.filter { it.carId == 0 }
                .map { it.carId = dao.findCarId(it.remoteCarId) }

        resources.filter { it.supervisorId == 0 }
                .map { it.supervisorId = dao.findSupervisorId(it.remoteSupervisorId) }

        dao.insert(*resources.toTypedArray())
    }

    override fun all(): Flowable<List<Trip>> = dao.all()
            .doOnNext { Timber.d("Loaded %d trips from local database", it.size) }

    override fun allNotAccumulated(): Flowable<List<Trip>> = dao.allNotAccumulated()
                .doOnNext { Timber.d("Loaded %d trips to be accumulated from local database", it.size) }

    override fun setRemoteId(id: Int, remoteId: Int) {
        Timber.d("Setting (remoteId: %d) for trip (id: %d)", remoteId, id)

        dao.updateRemoteId(id, remoteId)
    }

}
