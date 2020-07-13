package com.dotnet.nikit.investingmonitor.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dotnet.nikit.investingmonitor.db.models.PortfolioDto
import dagger.Provides

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM PortfolioDto")
    fun getPortoliosList() : LiveData<List<PortfolioDto>>

    @Query("SELECT * FROM PortfolioDto where brokerId = :brokerId")
    fun getPortfoliosByBrokerId(brokerId : Int) : LiveData<List<PortfolioDto>>

    @Insert
    fun insertPortfolio(portfolio: PortfolioDto)

    @Delete
    fun removePortfolio(portfolio: PortfolioDto)
}