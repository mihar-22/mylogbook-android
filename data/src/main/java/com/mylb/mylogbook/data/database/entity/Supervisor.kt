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
import com.mylb.mylogbook.domain.resource.Supervisor as SupervisorResource

@Entity(indices = arrayOf(Index("remoteId")))
class Supervisor(
        override var name: String,
        override var gender: String,
        override var isAccredited: Boolean,
        @PrimaryKey(autoGenerate = true)
        @SerializedName("_id")
        override var id: Int,
        @SerializedName("id")
        override var remoteId: Int,
        override var updatedAt: DateTime,
        override var deletedAt: DateTime?
) : SupervisorResource, Parcelable {

        @Ignore
        constructor() : this("", "" ,false, 0, 0, Network.now, null)

        @Ignore
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                (parcel.readInt() != 0),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readSerializable() as DateTime,
                parcel.readSerializable() as DateTime
        )

        override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeString(name)
                dest.writeString(gender)
                dest.writeInt(if (isAccredited) 1 else 0 )
                dest.writeInt(id)
                dest.writeInt(remoteId)
                dest.writeSerializable(updatedAt)
                dest.writeSerializable(deletedAt)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<Supervisor> {

                override fun createFromParcel(parcel: Parcel) = Supervisor(parcel)

                override fun newArray(size: Int): Array<Supervisor?> = arrayOfNulls(size)

        }

}
