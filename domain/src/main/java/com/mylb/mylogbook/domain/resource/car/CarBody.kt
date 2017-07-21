package com.mylb.mylogbook.domain.resource.car

enum class CarBody(val type: String, val displayName: String) {

    SEDAN("sedan", "Sedan"),
    WAGON("wagon", "Wagon"),
    SUV("suv", "SUV"),
    OFF_ROAD("off road", "Off Road"),
    HATCHBACK("hatchback", "Hatchback"),
    COUPE("coupe", "Coupe"),
    CONVERTIBLE("convertible", "Convertible"),
    SPORTS("sports", "Sports"),
    UTE("ute", "Ute"),
    MICRO("micro", "Micro"),
    VAN("van", "Van");

}
