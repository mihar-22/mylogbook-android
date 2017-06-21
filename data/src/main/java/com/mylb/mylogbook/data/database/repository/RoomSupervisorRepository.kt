package com.mylb.mylogbook.data.database.repository

import com.mylb.mylogbook.data.database.dao.SupervisorDao
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import io.reactivex.Flowable
import timber.log.Timber

class RoomSupervisorRepository(private val dao: SupervisorDao) : LocalSupervisorRepository<Supervisor> {

    override fun insert(resources: List<Supervisor>) {
        Timber.d("Inserting %d supervisors into local database", resources.size)

        if (resources.isEmpty()) return

        dao.insert(*resources.toTypedArray())
    }

    override fun update(resources: List<Supervisor>) {
        Timber.d("Updating %d supervisors in local database", resources.size)

        if (resources.isEmpty()) return

        resources.forEach {
            Timber.d("Updating (id: %d, remoteId: %d)", it.id, it.remoteId)

            it.updatedAt = Network.now
        }

        dao.update(*resources.toTypedArray())
    }

    override fun delete(resources: List<Supervisor>) {
        Timber.d("Deleting %d supervisors in local database", resources.size)

        if (resources.isEmpty()) return

        resources.forEach {
            Timber.d("Deleting (id: %d, remoteId: %d)", it.id, it.remoteId)

            it.deletedAt = Network.now
        }

        dao.update(*resources.toTypedArray())
    }

    override fun all(): Flowable<List<Supervisor>> = dao.all()
            .doOnNext { Timber.d("Loaded %d supervisors from local database", it.size) }

    override fun setRemoteId(id: Int, remoteId: Int) {
        Timber.d("Setting (remoteId: %d) for supervisor (id: %d)", remoteId, id)

        dao.updateRemoteId(id, remoteId)
        dao.updateTripsRemoteSupervisorId(id, remoteId)
    }

}
