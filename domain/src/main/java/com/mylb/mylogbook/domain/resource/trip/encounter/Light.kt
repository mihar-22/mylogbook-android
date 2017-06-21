package com.mylb.mylogbook.domain.resource.trip.encounter

class Light : BaseEncounter<Light.Condition> {

    constructor(): super()
    constructor(string: String): super(Condition::class.java, string)

    enum class Condition(override val code: String) : EncounterCondition {
        DAY("D"),
        DAWN("W"),
        DUSK("K"),
        NIGHT("N")
    }

}
