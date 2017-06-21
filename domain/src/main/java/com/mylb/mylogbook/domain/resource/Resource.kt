package com.mylb.mylogbook.domain.resource

import org.joda.time.DateTime

interface Resource {

    var id: Int
    var remoteId: Int
    var updatedAt: DateTime
    var deletedAt: DateTime?

}
