package com.mylb.mylogbook.data.sync

import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteTripRepository
import org.joda.time.DateTime

class TripSync(
        lastSyncedAt: DateTime?,
        localRepository: LocalTripRepository<Trip>,
        remoteRepository: RemoteTripRepository<Trip>
) : BaseSync<Trip>(lastSyncedAt, localRepository, remoteRepository)