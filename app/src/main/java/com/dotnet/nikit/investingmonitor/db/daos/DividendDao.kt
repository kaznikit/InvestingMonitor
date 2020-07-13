package com.dotnet.nikit.investingmonitor.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dotnet.nikit.investingmonitor.db.models.DividendDto

@Dao
interface DividendDao {

    @Query("SELECT * FROM DividendDto")
    fun getDividendsList() : LiveData<List<DividendDto>>

    @Query("SELECT * FROM DividendDto where assetId = :assetId")
    fun getDividendsByAssetId(assetId : Int) : LiveData<List<DividendDto>>

    @Insert
    fun insertDividend(dividend : DividendDto)

    @Delete
    fun removeDividend(dividend: DividendDto)

}