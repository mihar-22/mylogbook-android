package com.mylb.mylogbook.domain.resource.trip.encounter

abstract class BaseEncounter<T> constructor() where T : Enum<T>, T : EncounterCondition {

    protected val conditions = ArrayList<T>()

    constructor(clazz: Class<T>, string: String) : this() {
        val enumValues = clazz.enumConstants

        string.split(',')
                .filter { code -> enumValues.any { condition -> condition.code == code } }
                .map { code -> enumValues.first { condition -> condition.code == code } }
                .map { add(it) }
    }

    fun add(conditions: List<T>) = conditions.forEach { add(it) }

    fun add(condition: T) {
        if (contains(condition)) return

        conditions.add(condition)
    }

    fun contains(condition: T) = conditions.contains(condition)

    fun containsAll(conditions: List<T>) = conditions.containsAll(conditions)

    fun remove(condition: T) = conditions.remove(condition)

    override fun toString() = conditions.map { it.code }.joinToString(",")

}