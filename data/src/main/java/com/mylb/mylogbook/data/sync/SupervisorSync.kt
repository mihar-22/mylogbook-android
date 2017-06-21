package com.mylb.mylogbook.data.sync

import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteSupervisorRepository
import org.joda.time.DateTime

class SupervisorSync(
        lastSyncedAt: DateTime?,
        localRepository: LocalSupervisorRepository<Supervisor>,
        remoteRepository: RemoteSupervisorRepository<Supervisor>
) : BaseSync<Supervisor>(lastSyncedAt, localRepository, remoteRepository)
