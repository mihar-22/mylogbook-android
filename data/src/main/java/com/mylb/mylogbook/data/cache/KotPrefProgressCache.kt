package com.mylb.mylogbook.data.cache

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.bulk
import com.google.gson.reflect.TypeToken
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.cache.ProgressCache
import org.joda.time.DateTime
import org.joda.time.Duration
import timber.log.Timber

class KotPrefProgressCache constructor(context: Context) : KotprefModel(), ProgressCache {

    override val kotprefName = "${context.packageName}_statistics"
    override val kotprefMode = Context.MODE_PRIVATE

    private var _dayLogged by longPref(default = 0)
    private var _nightLogged by longPref(default = 0)
    private var _dayBonus by longPref(default = 0)
    private var _nightBonus by longPref(default = 0)
    private var _conditionOccurrences by nullableStringPref()
    private var _assessmentCompletedOn by stringPref(default = DateTime.now().toString(Network.dateFormat))

    override var numberOfTrips by intPref(default = 0)
    override var isHazardsComplete by booleanPref(default = false)
    override var isDrivingTestComplete by booleanPref(default = false)
    override var isSaferDriversComplete by booleanPref(default = false)
    override var isAssessmentComplete by booleanPref(default = false)

    override var dayLogged: Duration
        get() = Duration.millis(_dayLogged)
        set(duration) { _dayLogged = duration.millis }

    override var nightLogged: Duration
        get() = Duration.millis(_nightLogged)
        set(duration) { _nightLogged = duration.millis }

    override var dayBonus: Duration
        get() = Duration.millis(_dayBonus)
        set(duration) { _dayBonus = duration.millis }

    override var nightBonus: Duration
        get() = Duration.millis(_nightBonus)
        set(duration) { _nightBonus = duration.millis }

    override var conditionOccurrences: HashMap<String, Int>
        get() = if (_conditionOccurrences != null) {
            Network.converter.fromJson(_conditionOccurrences, object : TypeToken<HashMap<String, Int>>() {}.type)
        } else HashMap()
        set(conditionOccurrences) { _conditionOccurrences = Network.converter.toJson(conditionOccurrences) }

    override var assessmentCompletedOn: DateTime
        get() = DateTime.parse(_assessmentCompletedOn, Network.dateFormat)
        set(date) { _assessmentCompletedOn = date.toString(Network.dateFormat) }

    override fun resetTime() {
        Timber.d("Resetting time")

        bulk {
            _dayLogged = 0
            _nightLogged = 0
            _dayBonus = 0
            _nightBonus = 0
        }
    }

    override fun addTime(day: Duration, night: Duration, dayBonus: Duration, nightBonus: Duration) {
        Timber.d("Adding time")

        bulk {
            this@KotPrefProgressCache.dayLogged += day
            this@KotPrefProgressCache.nightLogged += night
            this@KotPrefProgressCache.dayBonus += dayBonus
            this@KotPrefProgressCache.nightBonus += nightBonus
        }
    }

}