package com.mylb.mylogbook.domain.resource

import ca.rmen.sunrisesunset.SunriseSunset
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import java.util.*

interface Trip : Resource {

    var odometer: Int
    var distance: Double
    var startedAt: DateTime
    var endedAt: DateTime
    var startLatitude: Double
    var endLatitude: Double
    var startLongitude: Double
    var endLongitude: Double
    var startLocation: String
    var endLocation: String
    var weather: Weather
    var road: Road
    var traffic: Traffic
    var light: Light
    var timeZone: DateTimeZone
    var isAccumulated: Boolean
    val carId: Int
    val supervisorId: Int
    val remoteCarId: Int
    val remoteSupervisorId: Int

    fun updateLightConditions() {
        val twilight = calculateTwilight()

        val trip = Interval(startedAt.toDateTime(timeZone), endedAt.toDateTime(timeZone))

        for (d in 0 .. trip.toDuration().standardDays.toInt()) {
            val dawn = Interval(twilight.astronomicalDawn.plusDays(d), twilight.civilDawn.plusDays(d))
            val day = Interval(twilight.civilDawn.plusDays(d), twilight.civilDusk.plusDays(d))
            val dusk = Interval(twilight.civilDusk.plusDays(d), twilight.astronomicalDusk.plusDays(d))
            val night = Interval(twilight.astronomicalDusk.plusDays(d), twilight.astronomicalDawn.plusDays(d+1))

            if (trip.overlaps(dawn)) light.add(Light.Condition.DAWN)
            if (trip.overlaps(day)) light.add(Light.Condition.DAY)
            if (trip.overlaps(dusk)) light.add(Light.Condition.DUSK)
            if (trip.overlaps(night)) light.add(Light.Condition.NIGHT)
        }
    }

    fun calculateTwilight(): Twilight {
        val calendar = Calendar.getInstance(timeZone.toTimeZone())
        val latitude = startLatitude
        val longitude = startLongitude

        val civilTwilight = SunriseSunset.getCivilTwilight(calendar, latitude, longitude)
        val nauticalTwilight = SunriseSunset.getNauticalTwilight(calendar, latitude, longitude)
        val astronomicalTwilight = SunriseSunset.getAstronomicalTwilight(calendar, latitude, longitude)

        return Twilight(
                DateTime(civilTwilight[0].timeInMillis, timeZone),
                DateTime(civilTwilight[1].timeInMillis, timeZone),
                DateTime(nauticalTwilight[0].timeInMillis, timeZone),
                DateTime(nauticalTwilight[1].timeInMillis, timeZone),
                DateTime(astronomicalTwilight[0].timeInMillis, timeZone),
                DateTime(astronomicalTwilight[1].timeInMillis, timeZone)
        )
    }

    data class Twilight(
            val civilDawn: DateTime,
            val civilDusk: DateTime,
            val nauticalDawn: DateTime,
            val nauticalDusk: DateTime,
            val astronomicalDawn: DateTime,
            val astronomicalDusk: DateTime
    )

}


