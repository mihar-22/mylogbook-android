package com.mylb.mylogbook.domain.resource

import com.mylb.mylogbook.domain.resource.supervisor.SupervisorGender

interface Supervisor : Resource {

    var name: String
    var gender: String
    var isAccredited: Boolean

    val fullGender: SupervisorGender
        get() = SupervisorGender.values().first { it.code == gender }

}