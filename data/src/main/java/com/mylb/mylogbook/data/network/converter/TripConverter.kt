package com.mylb.mylogbook.data.network.converter

import com.google.gson.*
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.reflect.Type

class TripSerializer : JsonSerializer<Trip> {

    override fun serialize(
            src: Trip,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ): JsonElement = gson.toJsonTree(src)

    private companion object {
        val gson: Gson = Network.buildConverter()
                .registerTypeAdapter(DateTime::class.java, JodaDateTimeSerializer())
                .registerTypeAdapter(DateTimeZone::class.java, JodaTimeZoneSerializer())
                .registerTypeAdapter(Weather::class.java, WeatherSerializer())
                .registerTypeAdapter(Road::class.java, RoadSerializer())
                .registerTypeAdapter(Traffic::class.java, TrafficSerializer())
                .registerTypeAdapter(Light::class.java, LightSerializer())
                .create()
    }

}

class TripDeserializer : JsonDeserializer<Trip> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ): Trip? {
        if (json !is JsonObject) return null

        val flatTree = JsonObject()

        json.entrySet().forEach { (key, elm) ->
            if (elm is JsonObject) {
                elm.entrySet().forEach { (key, elm) -> flatTree.add(key, elm) }
            } else {
                flatTree.add(key, elm)
            }
        }

        return gson.fromJson(flatTree, Trip::class.java)
    }

    private companion object {
        val gson: Gson = Network.buildConverter()
                .registerTypeAdapter(DateTime::class.java, JodaDateTimeDeserializer())
                .registerTypeAdapter(DateTimeZone::class.java, JodaTimeZoneDeserializer())
                .registerTypeAdapter(Weather::class.java, WeatherDeserializer())
                .registerTypeAdapter(Road::class.java, RoadDeserializer())
                .registerTypeAdapter(Traffic::class.java, TrafficDeserializer())
                .registerTypeAdapter(Light::class.java, LightDeserializer())
                .create()
    }

}
