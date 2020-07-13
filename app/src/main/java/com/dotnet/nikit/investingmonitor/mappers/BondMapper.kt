package com.dotnet.nikit.investingmonitor.mappers

import com.dotnet.nikit.investingmonitor.db.models.BondDto
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import com.dotnet.nikit.investingmonitor.models.Bond
import com.dotnet.nikit.investingmonitor.util.Utils

object BondMapper {

    fun mapBondDto(bond: Bond, portfolioId : Int): BondDto {
    return BondDto(
        null,
        bond.name,
        bond.ticker,
        Utils.formatDateToString(bond.buyDate),
        bond.buyPrice,
        bond.currency.value,
        Utils.formatDateToString(bond.sellDate),
        bond.sellPrice,
        portfolioId,
        bond.currentPrice,
        Utils.formatDateToString(bond.maturityDate),
        bond.couponRate
    )
}

    fun mapBond(bondDto: BondDto): Bond {
        return Bond(
            bondDto.id!!,
            bondDto.security!!,
            bondDto.name!!,
            bondDto.buyPrice!!,
            Utils.formatStringToDate(bondDto.buyDate)!!,
            CurrencyEnum.values().first { x -> x.value == bondDto.currency },
            bondDto.currentPrice!!,
            AssetTypeEnum.Share
        )
    }

    fun mapBondList(bondsDto: List<BondDto>) : List<Bond>{
        val tempBondsList = ArrayList<Bond>()
        for(bondDto in bondsDto){
            tempBondsList.add(mapBond(bondDto))
        }
        return tempBondsList
    }
}