package com.dotnet.nikit.investingmonitor.mappers

import com.dotnet.nikit.investingmonitor.db.models.PortfolioDto
import com.dotnet.nikit.investingmonitor.models.Portfolio

object PortfolioMapper {

    fun mapPortfolioDto(portfolio: Portfolio) : PortfolioDto{
        return PortfolioDto(null, portfolio.name, portfolio.brokerId)
    }

    fun mapPortfolio(portfolioDto: PortfolioDto) : Portfolio{
        return Portfolio(portfolioDto.id, portfolioDto.name, portfolioDto.brokerId)
    }

    fun mapPortfoliosList(portfolioDtos : List<PortfolioDto>) : List<Portfolio>{
        val tempPortfoliosList = ArrayList<Portfolio>()
        for(portDto in portfolioDtos){
            tempPortfoliosList.add(mapPortfolio(portDto))
        }
        return tempPortfoliosList
    }
}