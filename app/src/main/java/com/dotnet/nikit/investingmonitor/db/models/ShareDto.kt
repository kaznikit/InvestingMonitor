package com.dotnet.nikit.investingmonitor.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShareDto (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var name : String? = null,
    var security : String? = null,
    var buyDate : String? = null,
    var buyPrice : Float? = null,
    var currency : Int? = null,
    var sellDate : String? = null,
    var sellPrice : Float? = null,
    var portfolioId : Int? = null,
    var currentPrice : Float? = null
)