package com.mylb.mylogbook.data.database.converter

import android.arch.persistence.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class JodaConverters {

    @TypeConverter
    fun stringToDateTime(date: String?): DateTime? = if(date != null) DateTime.parse(date) else null

    @TypeConverter
    fun dateTimeToString(dateTime: DateTime?) = dateTime?.toString()

    @TypeConverter
    fun stringToDateTimeZone(id: String) = DateTimeZone.forID(id)

    @TypeConverter
    fun dateTimeZoneToString(dateTimeZone: DateTimeZone) = dateTimeZone.id

}