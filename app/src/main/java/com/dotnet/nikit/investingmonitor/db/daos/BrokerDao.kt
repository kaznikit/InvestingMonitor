package com.dotnet.nikit.investingmonitor.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dotnet.nikit.investingmonitor.db.models.BrokerDto

@Dao
interface BrokerDao {

    @Query("SELECT * FROM BrokerDto")
    fun getBrokersList() : LiveData<List<BrokerDto>>

    @Query("SELECT * FROM BrokerDto")
    fun getBrokers():List<BrokerDto>

    @Insert
    fun insertBroker(broker : BrokerDto)

    @Delete
    fun removeBroker(broker : BrokerDto)
}