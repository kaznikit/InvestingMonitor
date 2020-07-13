package com.dotnet.nikit.investingmonitor.models

import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import java.util.*

data class Bond(
    override var id: Int?,
    override var ticker: String,
    override var name: String,
    override var buyPrice: Float,
    override var buyDate: Date,
    override var currency: CurrencyEnum,
    override var currentPrice: Float?,
    override var type: AssetTypeEnum
) : BaseAsset() {
    var sellPrice : Float? = null
    var sellDate : Date? = null
    var maturityDate : Date? = null
    var couponRate : Float? = null
}