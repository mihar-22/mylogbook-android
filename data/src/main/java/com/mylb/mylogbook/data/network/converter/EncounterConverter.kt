package com.mylb.mylogbook.data.network.converter

import com.google.gson.*
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import java.lang.reflect.Type

class WeatherSerializer : JsonSerializer<Weather> {

    override fun serialize(
            src: Weather,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString())

}

class WeatherDeserializer : JsonDeserializer<Weather> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = Weather(json.asJsonPrimitive.asString)

}

class RoadSerializer : JsonSerializer<Road> {

    override fun serialize(
            src: Road,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString())

}

class RoadDeserializer : JsonDeserializer<Road> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = Road(json.asJsonPrimitive.asString)

}

class TrafficSerializer : JsonSerializer<Traffic> {

    override fun serialize(
            src: Traffic,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString())

}

class TrafficDeserializer : JsonDeserializer<Traffic> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = Traffic(json.asJsonPrimitive.asString)

}

class LightSerializer : JsonSerializer<Light> {

    override fun serialize(
            src: Light,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ) = JsonPrimitive(src.toString())

}

class LightDeserializer : JsonDeserializer<Light> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ) = Light(json.asJsonPrimitive.asString)

}
