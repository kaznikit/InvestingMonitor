package com.dotnet.nikit.investingmonitor.mappers

import com.dotnet.nikit.investingmonitor.db.models.ShareDto
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import com.dotnet.nikit.investingmonitor.models.Portfolio
import com.dotnet.nikit.investingmonitor.models.Share
import com.dotnet.nikit.investingmonitor.util.Utils
import kotlin.collections.ArrayList

object ShareMapper {

    fun mapShareDto(share: Share, portfolioId : Int): ShareDto {
        return ShareDto(
            null,
            share.name,
            share.ticker,
            Utils.formatDateToString(share.buyDate),
            share.buyPrice,
            share.currency.value,
            Utils.formatDateToString(share.sellDate),
            share.sellPrice,
            portfolioId,
            share.currentPrice
        )
    }

    fun mapShare(shareDto: ShareDto): Share {
        return Share(
            shareDto.id!!,
            shareDto.security!!,
            shareDto.name!!,
            shareDto.buyPrice!!,
            Utils.formatStringToDate(shareDto.buyDate)!!,
            CurrencyEnum.values().first { x -> x.value == shareDto.currency },
            shareDto.currentPrice!!,
            AssetTypeEnum.Share
        )
    }

    fun mapShareList(sharesDto: List<ShareDto>) : List<Share>{
        val tempSharesList = ArrayList<Share>()
        for(shDto in sharesDto){
            tempSharesList.add(mapShare(shDto))
        }
        return tempSharesList
    }
}