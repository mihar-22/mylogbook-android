package com.mylb.mylogbook.data.database.entity

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
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
) : TripResource, Parcelable {

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

    @Ignore
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readSerializable() as DateTime,
            parcel.readSerializable() as DateTime,
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            Weather(parcel.readString()),
            Road(parcel.readString()),
            Traffic(parcel.readString()),
            Light(parcel.readString()),
            parcel.readSerializable() as DateTimeZone,
            (parcel.readInt() != 0),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readSerializable() as DateTime,
            parcel.readSerializable() as DateTime
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(odometer)
        dest.writeDouble(distance)
        dest.writeSerializable(startedAt)
        dest.writeSerializable(endedAt)
        dest.writeDouble(startLatitude)
        dest.writeDouble(endLatitude)
        dest.writeDouble(startLongitude)
        dest.writeDouble(endLongitude)
        dest.writeString(startLocation)
        dest.writeString(endLocation)
        dest.writeString(weather.toString())
        dest.writeString(road.toString())
        dest.writeString(traffic.toString())
        dest.writeString(light.toString())
        dest.writeSerializable(timeZone)
        dest.writeInt(id)
        dest.writeInt(carId)
        dest.writeInt(supervisorId)
        dest.writeInt(remoteId)
        dest.writeInt(remoteCarId)
        dest.writeInt(remoteSupervisorId)
        dest.writeSerializable(updatedAt)
        dest.writeSerializable(deletedAt)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Trip> {

        override fun createFromParcel(parcel: Parcel) = Trip(parcel)

        override fun newArray(size: Int): Array<Trip?> = arrayOfNulls(size)

    }

}