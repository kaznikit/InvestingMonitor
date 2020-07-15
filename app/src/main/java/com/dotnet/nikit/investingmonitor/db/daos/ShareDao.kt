package com.dotnet.nikit.investingmonitor.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dotnet.nikit.investingmonitor.db.models.ShareDto

@Dao
interface ShareDao {

    @Query("SELECT * FROM ShareDto")
    fun getSharesList() : List<ShareDto>

    @Query("SELECT * FROM ShareDto where portfolioId = :portfolioId")
    fun getSharesByPortfolioId(portfolioId : Int) : LiveData<List<ShareDto>>

    @Insert
    fun insertShare(share : ShareDto)

    @Query("UPDATE ShareDto SET currentPrice = :currentPrice and sellPrice = :sellPrice and sellDate = :sellDate where id = :id")
    fun updateShare(id : Int, currentPrice : Float, sellPrice : Float, sellDate : String)

    @Delete
    fun removeShare(share : ShareDto)
}