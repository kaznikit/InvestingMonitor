package com.dotnet.nikit.investingmonitor.network

import com.dotnet.nikit.investingmonitor.network.models.MarketData
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {

    /**
     * boards:
     * TQBR - shares
     * TQCB - corporate bonds
     * TQOB - ofz
     * TQTF - etf
     */

    @GET("iss/engines/stock/markets/bonds/boards/{board}/securities/{security}.json?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LCURRENTPRICE")
    fun getCurrentPriceByBoardAndSecurity(@Path("board") board : String, @Path("security") security : String) : Observable<MarketData>
}