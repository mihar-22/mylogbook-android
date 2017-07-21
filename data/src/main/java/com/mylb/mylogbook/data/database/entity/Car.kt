package com.mylb.mylogbook.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
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
) : CarResource, Parcelable {

        @Ignore
        constructor() : this("", "", "", 0, 0, Network.now, null)

        @Ignore
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readSerializable() as DateTime,
                parcel.readSerializable() as DateTime
        )

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(name)
            dest.writeString(registration)
            dest.writeString(type)
            dest.writeInt(id)
            dest.writeInt(remoteId)
            dest.writeString(remoteId.toString())
            dest.writeSerializable(updatedAt)
            dest.writeSerializable(deletedAt)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<Car> {

            override fun createFromParcel(parcel: Parcel) = Car(parcel)

            override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)

        }

}