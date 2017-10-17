package com.mylb.mylogbook.domain.resource

import ca.rmen.sunrisesunset.SunriseSunset
import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.joda.time.*
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

    fun calculateLoggedTime(): LoggedTime {
        val start = startedAt.toDateTime(timeZone).secondOfDay
        val end = endedAt.toDateTime(timeZone).secondOfDay

        val twilight = calculateTwilight()
        val sunrise = twilight.civilDawn.secondOfDay
        val sunset = twilight.civilDusk.secondOfDay

        val secondsPerDay = 86_400
        val days = (start + (end - start)) / secondsPerDay

        var dayLogged = 0
        var nightLogged = 0

        for (day in 0 .. days) {
            val dayStart = if (day == 0) start else 0
            val dayEnd = if (day == days) end else secondsPerDay

            dayLogged += maxOf(0, (minOf(dayEnd, sunset) - maxOf(dayStart, sunrise)))
            nightLogged += maxOf(0, (dayEnd - maxOf(dayStart, sunset))) + maxOf(0, minOf(dayEnd, sunrise) - dayStart)
        }

        return LoggedTime(
                Duration.standardSeconds(dayLogged.toLong()),
                Duration.standardSeconds(nightLogged.toLong())
        )
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

    data class LoggedTime(
            val day: Duration,
            val night: Duration
    )

    data class Twilight(
            val civilDawn: DateTime,
            val civilDusk: DateTime,
            val nauticalDawn: DateTime,
            val nauticalDusk: DateTime,
            val astronomicalDawn: DateTime,
            val astronomicalDusk: DateTime
    )

    companion object {

        fun calculateBonus(duration: Duration, multiplier: Int, bonusRemaining: Duration): Duration {
            val noDuration = Duration(0)

            if (bonusRemaining <= noDuration) return noDuration

            return minOf(bonusRemaining, duration.multipliedBy(multiplier.toLong()))
        }

    }

}


