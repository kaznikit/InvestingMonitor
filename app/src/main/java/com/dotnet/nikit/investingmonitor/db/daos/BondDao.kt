package com.dotnet.nikit.investingmonitor.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dotnet.nikit.investingmonitor.db.models.BondDto

@Dao
interface BondDao {

    @Query("SELECT * FROM BondDto")
    fun getBondsList() : LiveData<List<BondDto>>

    @Query("SELECT * FROM BondDto where portfolioId = :portfolioId")
    fun getBondsByPortfolioId(portfolioId : Int) : LiveData<List<BondDto>>

    @Insert
    fun insertBond(bond : BondDto)

    @Delete
    fun removeBond(bond : BondDto)
}