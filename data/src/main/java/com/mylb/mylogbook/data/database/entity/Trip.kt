package com.mylb.mylogbook.data.database.entity

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import com.mylb.mylogbook.domain.resource.Trip as TripResource

@Entity(
        indices = arrayOf(Index("carId"), Index("supervisorId"), Index("remoteId")),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Car::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("carId")
                ),
                ForeignKey(
                        entity = Supervisor::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("supervisorId")
                )
        )
)
class Trip(
        override var odometer: Int,
        override var distance: Double,
        override var startedAt: DateTime,
        override var endedAt: DateTime,
        override var startLatitude: Double,
        override var endLatitude: Double,
        override var startLongitude: Double,
        override var endLongitude: Double,
        @SerializedName(value = "start_location", alternate = arrayOf("start"))
        override var startLocation: String,
        @SerializedName(value = "end_location", alternate = arrayOf("end"))
        override var endLocation: String,
        override var weather: Weather,
        @SerializedName("roads")
        override var road: Road,
        override var traffic: Traffic,
        override var light: Light,
        @SerializedName("timezone")
        override var timeZone: DateTimeZone,
        override var isAccumulated: Boolean,
        @PrimaryKey(autoGenerate = true)
        @SerializedName("_id")
        override var id: Int,
        @SerializedName("_car_id")
        override var carId: Int,
        @SerializedName("_supervisor_id")
        override var supervisorId: Int,
        @SerializedName("id")
        override var remoteId: Int,
        @SerializedName("car_id")
        override var remoteCarId: Int,
        @SerializedName("supervisor_id")
        override var remoteSupervisorId: Int,
        override var updatedAt: DateTime,
        override var deletedAt: DateTime?
) : TripResource {

    @Ignore
    constructor() : this(
            odometer = 0,
            distance = 0.0,
            startedAt = Network.now,
            endedAt = Network.now,
            startLatitude = 0.0,
            endLatitude = 0.0,
            startLongitude = 0.0,
            endLongitude = 0.0,
            startLocation = "",
            endLocation = "",
            weather = Weather(),
            road = Road(),
            traffic = Traffic(),
            light = Light(),
            timeZone = DateTimeZone.getDefault(),
            isAccumulated = false,
            id = 0,
            carId = 0,
            supervisorId = 0,
            remoteId = 0,
            remoteCarId = 0,
            remoteSupervisorId = 0,
            updatedAt = Network.now,
            deletedAt = null
    )

}