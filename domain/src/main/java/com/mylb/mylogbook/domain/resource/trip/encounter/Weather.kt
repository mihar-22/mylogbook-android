package com.mylb.mylogbook.domain.resource.trip.encounter

class Weather : BaseEncounter<Weather.Condition> {

    constructor(): super()
    constructor(string: String): super(Condition::class.java, string)

    enum class Condition(override val code: String) : EncounterCondition {
        CLEAR("C"),
        RAIN("R"),
        THUNDER("T"),
        FOG("F"),
        HAIL("H"),
        SNOW("S")
    }

}
