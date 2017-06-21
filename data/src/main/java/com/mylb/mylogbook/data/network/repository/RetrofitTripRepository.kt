package com.mylb.mylogbook.data.network.repository

import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.network.endpoint.TripEndPoint
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteTripRepository
import io.reactivex.Observable
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class RetrofitTripRepository @Inject constructor(
        private val endPoint: TripEndPoint,
        private val localRepository: LocalTripRepository<Trip>
) : RemoteTripRepository<Trip> {

    override fun all(): Observable<Response<List<Trip>>> = endPoint.all()
            .doOnNext {
                Timber.d("Loaded %d trips from remote database", it.data!!.size)
            }
            .doOnError {
                Timber.d("Failed to load all trips from remote database with: %s", it.message)
            }

    override fun all(updatedAfter: DateTime): Observable<Response<List<Trip>>> {
        val since = updatedAfter.toString(Network.dateTimeFormat)

        return endPoint.all(since)
                .doOnNext {
                    Timber.d(
                            "Loaded %d trips from remote database updated after %s",
                            it.data!!.size, since
                    )
                }
                .doOnError {
                    Timber.d(
                            "Failed to load trips since %s from remote database with: %s",
                            since, it.message
                    )
                }
    }

    override fun create(resource: Trip): Observable<Response<Trip>> = endPoint.create(resource)
            .doOnNext {
                Timber.d(
                        "Created trip (id: %d | remoteId: %d) in remote database",
                        resource.id, it.data!!.remoteId
                )
            }
            .doOnNext { localRepository.setRemoteId(resource.id, it.data!!.remoteId) }
            .doOnError {
                Timber.d(
                        "Failed to create trip (id: %d | remoteId: %d) in remote database with: %s",
                        resource.id, resource.remoteId, it.message
                )
            }

}
