package com.mylb.mylogbook.data.network

import com.google.gson.JsonObject
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.util.toDateTime
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.junit.Test

class SupervisorJsonTest {

    @Test
    fun JsonSerializer_Supervisor_CorrectJson() {
        val supervisor = Supervisor()

        supervisor.remoteId = Json.ID
        supervisor.name = Json.NAME
        supervisor.gender = Json.GENDER
        supervisor.isAccredited = Json.IS_ACCREDITED
        supervisor.updatedAt = Json.UPDATED_AT.toDateTime()

        val json = Network.converter.toJsonTree(supervisor).asJsonObject
        val expectedJson = Json.tree()

        expectedJson.entrySet().forEach { (key, elm) -> json[key]!!.shouldEqual(elm) }
    }

    @Test
    fun JsonDeserializer_Json_CorrectSupervisor() {
        val supervisor = Network.converter.fromJson(Json.tree(), Supervisor::class.java)

        supervisor.id.shouldEqual(0)
        supervisor.remoteId.shouldEqual(Json.ID)
        supervisor.name.shouldEqual(Json.NAME)
        supervisor.gender.shouldEqual(Json.GENDER)
        supervisor.isAccredited.shouldEqual(Json.IS_ACCREDITED)
        supervisor.updatedAt.toString(Network.dateTimeFormat).shouldEqual(Json.UPDATED_AT)
        supervisor.deletedAt.shouldBeNull()
    }

    private object Json {

        const val ID = 1
        const val NAME = "John Doe"
        const val GENDER = "M"
        const val IS_ACCREDITED = true
        const val UPDATED_AT = "2017-06-20 09:00:00"

        fun tree(): JsonObject {
            val root = JsonObject()

            root.addProperty("id", ID)
            root.addProperty("name", NAME)
            root.addProperty("gender", GENDER)
            root.addProperty("is_accredited", IS_ACCREDITED)
            root.addProperty("updated_at", UPDATED_AT)

            return root
        }

    }

}
