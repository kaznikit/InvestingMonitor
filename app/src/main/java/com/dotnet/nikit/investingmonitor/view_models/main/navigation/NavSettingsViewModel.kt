package com.dotnet.nikit.investingmonitor.view_models.main.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dotnet.nikit.investingmonitor.mappers.BrokerMapper
import com.dotnet.nikit.investingmonitor.models.Broker
import com.dotnet.nikit.investingmonitor.repositories.NavSettingsRepository
import com.dotnet.nikit.investingmonitor.view_models.main.Event
import com.dotnet.nikit.investingmonitor.views.main.navigation.BrokersSettingsFragment
import javax.inject.Inject

class NavSettingsViewModel @Inject constructor(var navSettingsRepository : NavSettingsRepository) :
    ViewModel() {

    private val settings : MutableLiveData<Event<List<String>>> = MutableLiveData()
    private val brokers : MutableLiveData<Event<List<Broker>>> = MutableLiveData()

    private var brokerSettingsFragment : BrokersSettingsFragment? = null

    fun getBrokerSettingsFragment() : BrokersSettingsFragment{
        if(brokerSettingsFragment == null){
            brokerSettingsFragment = BrokersSettingsFragment()
        }
        return brokerSettingsFragment!!
    }

    fun insertBroker(broker : Broker){
        navSettingsRepository.insertBroker(BrokerMapper.mapBrokerDto(broker))
    }

    fun getSettings() : LiveData<Event<List<String>>>{
        settings.postValue(Event.success(arrayListOf("Брокеры")))
        return settings
    }
}