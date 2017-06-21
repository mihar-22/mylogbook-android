package com.mylb.mylogbook.domain.resource

import com.mylb.mylogbook.domain.resource.trip.encounter.Light
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

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

}


