package com.dotnet.nikit.investingmonitor.models

import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import java.util.*

abstract class BaseAsset {
    abstract var id : Int?
    abstract var ticker : String
    abstract var name : String
    abstract var buyPrice : Float
    abstract var buyDate : Date
    abstract var currency : CurrencyEnum
    abstract var currentPrice : Float?
    abstract var type : AssetTypeEnum
}