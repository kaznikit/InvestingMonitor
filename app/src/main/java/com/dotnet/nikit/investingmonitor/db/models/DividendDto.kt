package com.dotnet.nikit.investingmonitor.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DividendDto (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var date : String? = null,
    var paymentValue : Float? = null,
    var assetId : Int? = null
)