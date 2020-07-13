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
    fun getSharesList() : LiveData<List<ShareDto>>

    @Query("SELECT * FROM ShareDto where portfolioId = :portfolioId")
    fun getSharesByPortfolioId(portfolioId : Int) : LiveData<List<ShareDto>>

    @Insert
    fun insertShare(share : ShareDto)

    @Delete
    fun removeShare(share : ShareDto)
}