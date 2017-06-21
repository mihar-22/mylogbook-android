package com.mylb.mylogbook.data.network

import com.google.gson.JsonObject
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.util.toDateTime
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.amshove.kluent.shouldEqual
import org.joda.time.DateTimeZone
import org.junit.Test

class TripJsonTest {

    @Test
    fun JsonSerializer_Trip_CorrectJson() {
        val trip = Trip()

        trip.remoteId = Json.ID
        trip.odometer = Json.ODOMETER
        trip.distance = Json.DISTANCE
        trip.startedAt = Json.STARTED_AT.toDateTime()
        trip.endedAt = Json.ENDED_AT.toDateTime()
        trip.startLatitude = Json.START_LATITUDE
        trip.endLatitude = Json.END_LATITUDE
        trip.startLongitude = Json.START_LONGITUDE
        trip.endLongitude = Json.END_LONGITUDE
        trip.startLocation = Json.START_LOCATION
        trip.endLocation = Json.END_LOCATION
        trip.weather = Weather(Json.WEATHER)
        trip.road = Road(Json.ROADS)
        trip.traffic = Traffic(Json.TRAFFIC)
        trip.light = Light(Json.LIGHT)
        trip.timeZone = DateTimeZone.forID(Json.TIME_ZONE)
        trip.remoteCarId = Json.CAR_ID
        trip.remoteSupervisorId = Json.SUPERVISOR_ID

        val json = Network.converter.toJsonTree(trip).asJsonObject
        val expectedJson = Json.tree(isFlat = true)

        expectedJson.entrySet().forEach { (key, elm) -> json[key]!!.shouldEqual(elm) }
    }

    @Test
    fun JsonDeserializer_Json_CorrectTrip() {
        val trip = Network.converter.fromJson(Json.tree(), Trip::class.java)

        trip.id.shouldEqual(0)
        trip.remoteId.shouldEqual(Json.ID)
        trip.remoteCarId.shouldEqual(Json.CAR_ID)
        trip.remoteSupervisorId.shouldEqual(Json.SUPERVISOR_ID)
        trip.startedAt.toString(Network.dateTimeFormat).shouldEqual(Json.STARTED_AT)
        trip.endedAt.toString(Network.dateTimeFormat).shouldEqual(Json.ENDED_AT)
        trip.odometer.shouldEqual(Json.ODOMETER)
        trip.distance.shouldEqual(Json.DISTANCE)
        trip.weather.toString().shouldEqual(Json.WEATHER)
        trip.road.toString().shouldEqual(Json.ROADS)
        trip.traffic.toString().shouldEqual(Json.TRAFFIC)
        trip.light.toString().shouldEqual(Json.LIGHT)
        trip.startLatitude.shouldEqual(Json.START_LATITUDE)
        trip.endLatitude.shouldEqual(Json.END_LATITUDE)
        trip.startLongitude.shouldEqual(Json.START_LONGITUDE)
        trip.endLongitude.shouldEqual(Json.END_LONGITUDE)
        trip.startLocation.shouldEqual(Json.START_LOCATION)
        trip.endLocation.shouldEqual(Json.END_LOCATION)
        trip.timeZone.toString().shouldEqual(Json.TIME_ZONE)
    }

    private object Json {

        const val ID = 1
        const val STARTED_AT = "2017-06-20 09:00:00"
        const val ENDED_AT = "2017-06-20 10:00:00"
        const val ODOMETER = 123000
        const val DISTANCE = 5000.0
        const val WEATHER = "R,F,S"
        const val TRAFFIC = "L,M"
        const val ROADS = "L,R,F"
        const val LIGHT = "D,N"
        const val START_LATITUDE = -37.98765
        const val END_LATITUDE = 38.56789
        const val START_LONGITUDE = 144.98765
        const val END_LONGITUDE = 144.56789
        const val START_LOCATION = "Dandenong"
        const val END_LOCATION = "Narre Warren"
        const val TIME_ZONE = "Australia/Melbourne"
        const val CAR_ID = 1
        const val SUPERVISOR_ID = 1

        fun tree(isFlat: Boolean = false): JsonObject {
            val root = JsonObject()

            root.addProperty("id", ID)
            root.addProperty("started_at", STARTED_AT)
            root.addProperty("ended_at", ENDED_AT)
            root.addProperty("odometer", ODOMETER)
            root.addProperty("distance", DISTANCE)

            val conditions = if (isFlat) root else JsonObject()
            conditions.addProperty("weather", WEATHER)
            conditions.addProperty("roads", ROADS)
            conditions.addProperty("traffic", TRAFFIC)
            conditions.addProperty("light", LIGHT)

            val coordinates = if (isFlat) root else JsonObject()
            coordinates.addProperty("start_latitude", START_LATITUDE)
            coordinates.addProperty("end_latitude", END_LATITUDE)
            coordinates.addProperty("start_longitude", START_LONGITUDE)
            coordinates.addProperty("end_longitude", END_LONGITUDE)

            val location = if (isFlat) root else JsonObject()
            location.addProperty((if (isFlat) "start_location" else "start"), START_LOCATION)
            location.addProperty((if (isFlat) "end_location" else "end"), END_LOCATION)
            location.addProperty("timezone", TIME_ZONE)

            val resources = if (isFlat) root else JsonObject()
            resources.addProperty("car_id", CAR_ID)
            resources.addProperty("supervisor_id", SUPERVISOR_ID)

            if (!isFlat) {
                root.add("conditions", conditions)
                root.add("coordinates", coordinates)
                root.add("location", location)
                root.add("resources", resources)
            }

            return root
        }

    }

}
