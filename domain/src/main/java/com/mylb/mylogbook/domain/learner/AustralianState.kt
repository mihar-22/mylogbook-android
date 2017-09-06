package com.mylb.mylogbook.domain.learner

import org.joda.time.Duration

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

    val bonusTimeAvailable: Duration
        get() = when (this) {
            NEW_SOUTH_WHALES, QUEENSLAND -> Duration.standardSeconds(36_000)
            else -> Duration.standardSeconds(0)
        }

    val bonusMultiplier: Int
        get() = when (this) {
            NEW_SOUTH_WHALES, QUEENSLAND -> 2
            else -> 0
        }

    val bonusTimeAvailableWithMultiplier: Duration
        get() = (bonusTimeAvailable.multipliedBy(bonusMultiplier.toLong()))

    fun monthsRequired(age: Int): Int = when (this) {
        VICTORIA -> if (age < 21) 12 else if (age in 21..25) 6 else 3
        QUEENSLAND -> 12
        TASMANIA -> 12
        NEW_SOUTH_WHALES -> 12
        SOUTH_AUSTRALIA -> if (age < 25) 12 else 6
        WESTERN_AUSTRALIA -> 6
    }

}

class LoggedTimeRequired(private val daySeconds: Long, private val nightSeconds: Long? = null) {

    val day: Duration
        get() = Duration.standardSeconds(daySeconds)

    val night: Duration?
        get() = if (nightSeconds != null) Duration.standardSeconds(nightSeconds) else null

    val total: Duration
        get() = (day.plus(night))

}

object NewSouthWhalesRequirements {

    val SAFER_DRIVERS_REQUIRED_TIME = Duration.standardSeconds(180_000)
    val SAFER_DRIVERS_BONUS = Duration.standardSeconds(72_000)

}

enum class TasmaniaRequirements {

    L1,
    L2;

    val loggedTimeRequired: Duration
        get() = when (this) {
            L1 -> Duration.standardSeconds(108_000)
            L2 -> Duration.standardSeconds(180_000)
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

    val loggedTimeRequired: Duration
        get() = when (this) {
            S1 -> Duration.standardSeconds(90_000)
            S2 -> Duration.standardSeconds(90_000)
        }

    val monthsRequired: Int
        get() = when (this) {
            S1 -> 0
            S2 -> 6
        }

}