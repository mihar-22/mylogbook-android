package com.mylb.mylogbook.data.network.repository

import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.network.endpoint.CarEndPoint
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteCarRepository
import io.reactivex.Observable
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class RetrofitCarRepository @Inject constructor(
        private val endPoint: CarEndPoint,
        private val localRepository: LocalCarRepository<Car>
) : RemoteCarRepository<Car> {

    override fun all(): Observable<Response<List<Car>>> = endPoint.all()
            .doOnNext {
                Timber.d("Loaded %d cars from remote database", it.data!!.size)
            }
            .doOnError {
                Timber.d("Failed to load all cars from remote database with: %s", it.message)
            }

    override fun all(updatedAfter: DateTime): Observable<Response<List<Car>>> {
        val since = updatedAfter.toString(Network.dateTimeFormat)

        return endPoint.all(since)
                .doOnNext {
                    Timber.d(
                            "Loaded %d cars from remote database updated after %s",
                            it.data!!.size, since
                    )
                }
                .doOnError {
                    Timber.d(
                            "Failed to load cars since %s from remote database with: %s",
                            since, it.message
                    )
                }
    }

    override fun create(resource: Car): Observable<Response<Car>> = endPoint.create(resource)
            .doOnNext {
                Timber.d(
                        "Created car (id: %d | remoteId: %d) in remote database",
                        resource.id, it.data!!.remoteId
                )
            }
            .doOnNext { localRepository.setRemoteId(resource.id, it.data!!.remoteId) }
            .doOnError {
                Timber.d(
                        "Failed to create car (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

    override fun update(resource: Car): Observable<Response<Unit>> = endPoint.update(resource.remoteId, resource)
            .doOnNext {
                Timber.d(
                        "Updated car (id: %d | remoteId: %d) in remote database",
                        resource.id, resource.remoteId
                )
            }
            .doOnError {
                Timber.d(
                        "Failed to update car (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

    override fun delete(resource: Car): Observable<Response<Unit>> = endPoint.delete(resource.remoteId)
            .doOnNext {
                Timber.d(
                        "Deleted car (id: %d | remoteId: %d) in remote database",
                        resource.id, resource.remoteId
                )
            }
            .doOnError {
                Timber.d(
                        "Failed to delete car (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

}
