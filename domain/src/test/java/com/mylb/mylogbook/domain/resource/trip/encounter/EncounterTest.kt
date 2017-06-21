package com.mylb.mylogbook.domain.resource.trip.encounter

import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.junit.Test

class EncounterTest {

    @Test
    fun Constructor_String_CorrectConditions() {
        val codes = "1,2,3,C,L,K"

        val encounter = TestEncounter(codes)

        encounter.containsAll(arrayListOf(
                TestEncounter.Condition.CONDITION_ONE,
                TestEncounter.Condition.CONDITION_TWO,
                TestEncounter.Condition.CONDITION_THREE
        )).shouldBeTrue()

        encounter.toString().shouldEqual("1,2,3")
    }

    @Test
    fun Add_SameConditionTwice_ConditionOnlyAddedOnce() {
        val encounter = TestEncounter()

        encounter.add(TestEncounter.Condition.CONDITION_ONE)
        encounter.add(TestEncounter.Condition.CONDITION_ONE)

        encounter.toString().shouldEqual("1")
    }

    private class TestEncounter : BaseEncounter<TestEncounter.Condition> {

        constructor(): super()
        constructor(string: String): super(Condition::class.java, string)

        enum class Condition(override val code: String) : EncounterCondition {
            CONDITION_ONE("1"),
            CONDITION_TWO("2"),
            CONDITION_THREE("3")
        }

    }

}
