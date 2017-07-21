package com.mylb.mylogbook.domain.resource

import com.mylb.mylogbook.domain.resource.car.CarBody

interface Car : Resource {

    var name: String
    var registration: String
    var type: String

    val body: CarBody
        get() = CarBody.values().first { it.type == type }

}