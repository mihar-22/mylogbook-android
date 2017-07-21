package com.mylb.mylogbook.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.network.converter.JodaDateTimeDeserializer
import com.mylb.mylogbook.data.network.converter.JodaDateTimeSerializer
import com.mylb.mylogbook.data.network.converter.TripDeserializer
import com.mylb.mylogbook.data.network.converter.TripSerializer
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

object Network {

    val timeZone = DateTimeZone.UTC
    val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
    val dateTimeFormat =  DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    val now
        get() = DateTime.now(timeZone)

    val converter: Gson = buildConverter()
            .registerTypeAdapter(DateTime::class.java, JodaDateTimeSerializer())
            .registerTypeAdapter(DateTime::class.java, JodaDateTimeDeserializer())
            .registerTypeAdapter(Trip::class.java, TripSerializer())
            .registerTypeAdapter(Trip::class.java, TripDeserializer())
            .create()

    fun buildConverter(): GsonBuilder = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

}