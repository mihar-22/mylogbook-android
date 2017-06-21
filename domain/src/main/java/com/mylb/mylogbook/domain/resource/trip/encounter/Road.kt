package com.mylb.mylogbook.domain.resource.trip.encounter

class Road : BaseEncounter<Road.Condition> {

    constructor(): super()
    constructor(string: String): super(Condition::class.java, string)

    enum class Condition(override val code: String) : EncounterCondition {
        LOCAL_STREET("L"),
        MAIN_ROAD("M"),
        INNER_CITY("I"),
        FREEWAY("F"),
        RURAL_ROAD("R"),
        GRAVEL("G")
    }

}
