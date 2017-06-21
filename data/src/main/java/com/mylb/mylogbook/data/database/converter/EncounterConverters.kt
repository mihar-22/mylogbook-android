package com.mylb.mylogbook.data.database.converter

import android.arch.persistence.room.TypeConverter
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather

class EncounterConverters {

    @TypeConverter
    fun stringToWeather(conditions: String) = Weather(conditions)

    @TypeConverter
    fun weatherToString(weather: Weather) = weather.toString()

    @TypeConverter
    fun stringToRoad(conditions: String) = Road(conditions)

    @TypeConverter
    fun roadToString(road: Road) = road.toString()

    @TypeConverter
    fun stringToTraffic(conditions: String) = Traffic(conditions)

    @TypeConverter
    fun trafficToString(traffic: Traffic) = traffic.toString()

    @TypeConverter
    fun stringToLight(conditions: String) = Light(conditions)

    @TypeConverter
    fun lightToString(light: Light) = light.toString()

}