package com.mylb.mylogbook.data.sync

import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteCarRepository
import org.joda.time.DateTime

class CarSync(
        lastSyncedAt: DateTime?,
        localRepository: LocalCarRepository<Car>,
        remoteRepository: RemoteCarRepository<Car>
) : BaseSync<Car>(lastSyncedAt, localRepository, remoteRepository)