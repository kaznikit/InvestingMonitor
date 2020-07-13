package com.dotnet.nikit.investingmonitor.mappers

import com.dotnet.nikit.investingmonitor.db.models.DividendDto
import com.dotnet.nikit.investingmonitor.models.Dividend
import com.dotnet.nikit.investingmonitor.util.Utils

object DividendMapper {

    fun mapDividendDto(dividend : Dividend) : DividendDto {
        return DividendDto(null, Utils.formatDateToString(dividend.date), dividend.payment, dividend.assetId)
    }

    fun mapDividend(dividendDto: DividendDto) : Dividend{
        return Dividend(Utils.formatStringToDate(dividendDto.date)!!, dividendDto.paymentValue!!, dividendDto.assetId!!)
    }

    fun mapDividendsList(dividendDtos: List<DividendDto>) : List<Dividend>{
        val tempDividendsList = ArrayList<Dividend>()
        for(divDto in dividendDtos){
            tempDividendsList.add(mapDividend(divDto))
        }
        return tempDividendsList
    }
}