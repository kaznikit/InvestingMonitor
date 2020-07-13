package com.dotnet.nikit.investingmonitor.network.models

import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MarketData(
    @Expose
    @SerializedName("data") val data: JsonArray
)

class Data(val data: JsonArray)