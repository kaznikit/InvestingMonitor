package com.dotnet.nikit.investingmonitor.mappers

import com.dotnet.nikit.investingmonitor.db.models.BrokerDto
import com.dotnet.nikit.investingmonitor.models.Broker

object BrokerMapper {
    fun mapBrokerDto(broker: Broker) : BrokerDto{
        return BrokerDto(null, broker.name, broker.serviceFee, broker.transactionFee, broker.isIIS)
    }

    fun mapBroker(brokerDto: BrokerDto) : Broker{
        return Broker(brokerDto.id, brokerDto.name, brokerDto.serviceFee, brokerDto.transactionFee, brokerDto.isIIS)
    }

    fun mapBrokersList(brokerDtos: List<BrokerDto>) : List<Broker>{
        val tempBrokersList = ArrayList<Broker>()
        for(brokerDto in brokerDtos){
            tempBrokersList.add(mapBroker(brokerDto))
        }
        return tempBrokersList
    }
}