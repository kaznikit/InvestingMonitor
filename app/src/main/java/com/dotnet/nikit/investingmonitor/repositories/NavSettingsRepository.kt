package com.dotnet.nikit.investingmonitor.repositories

import androidx.lifecycle.LiveData
import com.dotnet.nikit.investingmonitor.db.daos.BrokerDao
import com.dotnet.nikit.investingmonitor.db.models.BrokerDto
import javax.inject.Inject

class NavSettingsRepository @Inject constructor(brokersDao : BrokerDao) {

    private val brokersDao: BrokerDao = brokersDao

    fun getBrokers() : LiveData<List<BrokerDto>>{
        return brokersDao.getBrokersList()
    }

    fun insertBroker(broker: BrokerDto) {
        brokersDao.insertBroker(broker)
    }
}