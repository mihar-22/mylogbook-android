package com.mylb.mylogbook.domain.learner

enum class AustralianState(val abbreviation: String, val displayName: String) {

    VICTORIA("VIC", "Victoria"),
    QUEENSLAND("QLD", "Queensland"),
    SOUTH_AUSTRALIA("SA", "South Australia"),
    WESTERN_AUSTRALIA("WA", "Western Australia"),
    TASMANIA("TAS", "Tasmania"),
    NEW_SOUTH_WHALES("NSW", "New South Whales");

    val isBonusCreditsAvailable: Boolean
            get() = when (this) {
                NEW_SOUTH_WHALES, QUEENSLAND -> true
                else -> false
            }

    val isTestsAvailable: Boolean
        get() = when (this) {
            TASMANIA, WESTERN_AUSTRALIA -> true
            else -> false
        }

    val loggedTimeRequired: LoggedTimeRequired
        get() = when (this) {
            VICTORIA -> LoggedTimeRequired(396_000, 36_000)
            QUEENSLAND -> LoggedTimeRequired(324_000, 36_000)
            TASMANIA -> LoggedTimeRequired(288_000)
            NEW_SOUTH_WHALES -> LoggedTimeRequired(360_000, 72_000)
            SOUTH_AUSTRALIA -> LoggedTimeRequired(216_000, 54_000)
            WESTERN_AUSTRALIA -> LoggedTimeRequired(180_000)
        }

    val bonusTimeAvailable: Int
        get() = when (this) {
            NEW_SOUTH_WHALES, QUEENSLAND -> 36_000
            else -> 0
        }

    val bonusMultiplier: Int
        get() = when (this) {
            NEW_SOUTH_WHALES, QUEENSLAND -> 2
            else -> 0
        }

    val bonusTimeAvailableWithMultiplier: Int
        get() = (bonusTimeAvailable * bonusMultiplier)

    fun monthsRequired(age: Int): Int = when (this) {
        VICTORIA -> if (age < 21) 12 else if (age in 21..25) 6 else 3
        QUEENSLAND -> 12
        TASMANIA -> 12
        NEW_SOUTH_WHALES -> 12
        SOUTH_AUSTRALIA -> if (age < 25) 12 else 6
        WESTERN_AUSTRALIA -> 6
    }

}

class LoggedTimeRequired(val day: Int, val night: Int? = null) {

    val total: Int
        get() = (day + (night ?: 0))

}

object NewSouthWhalesRequirements {

    const val SAFER_DRIVERS_REQUIRED_TIME = 180_000
    const val SAFER_DRIVERS_BONUS = 72_000

}

enum class TasmaniaRequirements {

    L1,
    L2;

    val loggedTimeRequired: Int
        get() = when (this) {
            L1 -> 108_000
            L2 -> 180_000
        }

    val monthsRequired: Int
        get() = when (this) {
            L1 -> 3
            L2 -> 9
        }

}

enum class WesternAustraliaRequirements {

    S1,
    S2;

    val loggedTimeRequired: Int
        get() = when (this) {
            S1 -> 90_000
            S2 -> 90_000
        }

    val monthsRequired: Int
        get() = when (this) {
            S1 -> 0
            S2 -> 6
        }

}