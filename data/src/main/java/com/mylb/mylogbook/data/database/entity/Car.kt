package com.mylb.mylogbook.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mylb.mylogbook.data.network.Network
import org.joda.time.DateTime
import com.mylb.mylogbook.domain.resource.Car as CarResource

@Entity(indices = arrayOf(Index("remoteId", unique = true)))
class Car(
        override var name: String,
        override var registration: String,
        override var type: String,
        @PrimaryKey(autoGenerate = true)
        @SerializedName("_id")
        override var id: Int,
        @SerializedName("id")
        override var remoteId: Int,
        override var updatedAt: DateTime,
        override var deletedAt: DateTime?
) : CarResource {

        @Ignore
        constructor() : this("", "", "", 0, 0, Network.now, null)

}