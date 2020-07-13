package com.dotnet.nikit.investingmonitor.models

data class Broker(
    var id: Int? = null,
    var name : String? = null,
    var serviceFee : Float = 0f,
    var transactionFee : Float = 0f,
    var isIIS : Boolean = false)