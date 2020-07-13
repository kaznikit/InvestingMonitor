package com.dotnet.nikit.investingmonitor.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BrokerDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var name : String? = null,
    var serviceFee : Float = 0f,
    var transactionFee : Float = 0f,
    var isIIS : Boolean = false
)