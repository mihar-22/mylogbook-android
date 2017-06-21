package com.mylb.mylogbook.domain.resource.trip.encounter

class Traffic : BaseEncounter<Traffic.Condition> {

    constructor(): super()
    constructor(string: String): super(Condition::class.java, string)

    enum class Condition(override val code: String) : EncounterCondition {
        LIGHT("L"),
        MODERATE("M"),
        HEAVY("H")
    }

}
