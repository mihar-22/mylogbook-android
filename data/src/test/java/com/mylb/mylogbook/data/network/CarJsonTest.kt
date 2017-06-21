package com.mylb.mylogbook.data.network

import com.google.gson.JsonObject
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.util.toDateTime
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.junit.Test

class CarJsonTest {

    @Test
    fun JsonSerializer_Car_CorrectJson() {
        val car = Car()

        car.remoteId = Json.ID
        car.name = Json.NAME
        car.registration = Json.REGISTRATION
        car.type = Json.TYPE
        car.updatedAt = Json.UPDATED_AT.toDateTime()

        val json = Network.converter.toJsonTree(car).asJsonObject
        val expectedJson = Json.tree()

        expectedJson.entrySet().forEach { (key, elm) -> json[key]!!.shouldEqual(elm) }
    }

    @Test
    fun JsonDeserializer_Json_CorrectCar() {
        val car = Network.converter.fromJson(Json.tree(), Car::class.java)

        car.id.shouldEqual(0)
        car.remoteId.shouldEqual(Json.ID)
        car.name.shouldEqual(Json.NAME)
        car.registration.shouldEqual(Json.REGISTRATION)
        car.type.shouldEqual(Json.TYPE)
        car.updatedAt.toString(Network.dateTimeFormat).shouldEqual(Json.UPDATED_AT)
        car.deletedAt.shouldBeNull()
    }

    private object Json {

        const val ID = 1
        const val NAME = "Car"
        const val REGISTRATION = "ABC123"
        const val TYPE = "Sedan"
        const val UPDATED_AT = "2017-06-20 09:00:00"

        fun tree(): JsonObject {
            val root = JsonObject()

            root.addProperty("id", ID)
            root.addProperty("name", NAME)
            root.addProperty("registration", REGISTRATION)
            root.addProperty("type", TYPE)
            root.addProperty("updated_at", UPDATED_AT)

            return root
        }

    }

}
