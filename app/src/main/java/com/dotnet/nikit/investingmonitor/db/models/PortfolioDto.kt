package com.dotnet.nikit.investingmonitor.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PortfolioDto (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    var name : String? = null,
    var brokerId : Int? = null
)