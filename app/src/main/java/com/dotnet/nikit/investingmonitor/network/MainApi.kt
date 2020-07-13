package com.dotnet.nikit.investingmonitor.network

import com.dotnet.nikit.investingmonitor.network.models.MarketData
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {

/*    @GET("iss/engines/stock/markets/shares/boards/{board}/securities/{security}.json")
    fun getInfoByBoardAndSecurity(@Path("board") board : String, @Path("security") security : String) : Flowable<MarketData>*/

    @GET("iss/engines/stock/markets/shares/boards/{board}/securities/{security}.json?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LCURRENTPRICE")
    fun getCurrentPriceByBoardAndSecurity(@Path("board") board : String, @Path("security") security : String) : Observable<MarketData>

}