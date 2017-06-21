package com.mylb.mylogbook.data.network.repository

import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.network.endpoint.SupervisorEndPoint
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteSupervisorRepository
import io.reactivex.Observable
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class RetrofitSupervisorRepository @Inject constructor(
        private val endPoint: SupervisorEndPoint,
        private val localRepository: LocalSupervisorRepository<Supervisor>
) : RemoteSupervisorRepository<Supervisor> {

    override fun all(): Observable<Response<List<Supervisor>>> = endPoint.all()
            .doOnNext {
                Timber.d("Loaded %d supervisors from remote database", it.data!!.size)
            }
            .doOnError {
                Timber.d("Failed to load all supervisors from remote database with: %s", it.message)
            }

    override fun all(updatedAfter: DateTime): Observable<Response<List<Supervisor>>> {
        val since = updatedAfter.toString(Network.dateTimeFormat)

        return endPoint.all(since)
                .doOnNext {
                    Timber.d(
                            "Loaded %d supervisors from remote database updated after %s",
                            it.data!!.size, since
                    )
                }
                .doOnError {
                    Timber.d(
                            "Failed to load supervisors since %s from remote database with: %s",
                            since, it.message
                    )
                }
    }

    override fun create(resource: Supervisor): Observable<Response<Supervisor>> = endPoint.create(resource)
            .doOnNext {
                Timber.d(
                        "Created supervisor (id: %d | remoteId: %d) in remote database",
                        resource.id, it.data!!.remoteId
                )
            }
            .doOnNext { localRepository.setRemoteId(resource.id, it.data!!.remoteId) }
            .doOnError {
                Timber.d(
                        "Failed to create supervisor (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

    override fun update(resource: Supervisor): Observable<Response<Unit>> = endPoint.update(resource.remoteId, resource)
            .doOnNext {
                Timber.d(
                        "Updated supervisor (id: %d | remoteId: %d) in remote database",
                        resource.id, resource.remoteId
                )
            }
            .doOnError {
                Timber.d(
                        "Failed to update supervisor (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

    override fun delete(resource: Supervisor): Observable<Response<Unit>> = endPoint.delete(resource.remoteId)
            .doOnNext {
                Timber.d(
                        "Deleted supervisor (id: %d | remoteId: %d) in remote database",
                        resource.id, resource.remoteId
                )
            }
            .doOnError {
                Timber.d(
                        "Failed to delete supervisor (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

}

