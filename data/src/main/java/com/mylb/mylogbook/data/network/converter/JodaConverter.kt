package com.mylb.mylogbook.data.network.converter

import com.google.gson.*
import com.mylb.mylogbook.data.network.Network
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.reflect.Type

class JodaDateTimeSerializer : JsonSerializer<DateTime> {

    override fun serialize(
            src: DateTime,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString(Network.dateTimeFormat))

}

class JodaDateTimeDeserializer : JsonDeserializer<DateTime> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = DateTime.parse(json.asJsonPrimitive.asString, Network.dateTimeFormat)
            .withZoneRetainFields(Network.timeZone)!!

}

class JodaTimeZoneSerializer : JsonSerializer<DateTimeZone> {

    override fun serialize(
            src: DateTimeZone,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString())

}

class JodaTimeZoneDeserializer : JsonDeserializer<DateTimeZone> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = DateTimeZone.forID(json.asJsonPrimitive.asString)

}