package com.dotnet.nikit.investingmonitor.network.deserializers

import com.dotnet.nikit.investingmonitor.network.models.MarketData
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MoexResponseDeserializer : JsonDeserializer<Any> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Any {
        if(json == null){
            return Gson()
        }
        val marketData = json.asJsonObject.get("marketdata")
        return Gson().fromJson(marketData, MarketData::class.java)
    }
}