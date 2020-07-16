package com.dotnet.nikit.investingmonitor.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dotnet.nikit.investingmonitor.db.daos.*
import com.dotnet.nikit.investingmonitor.db.models.*
import com.dotnet.nikit.investingmonitor.network.MainApi
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableCache
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class NavListRepository @Inject constructor(
    mainApi: MainApi,
    sharesDao: ShareDao,
    brokersDao: BrokerDao,
    portfolioDao: PortfolioDao,
    bondsDao: BondDao,
    dividendsDao: DividendDao
) {

    private val mainApi: MainApi = mainApi
    private val sharesDao: ShareDao = sharesDao
    private val brokersDao: BrokerDao = brokersDao
    private val portfolioDao: PortfolioDao = portfolioDao
    private val bondsDao: BondDao = bondsDao
    private val dividendsDao: DividendDao = dividendsDao

    //region Shares

    suspend fun getShares(): List<ShareDto> = withContext(Dispatchers.IO) {
        return@withContext sharesDao.getSharesList()
    }

    fun getSharesByPortfolioId(portfolioId: Int): LiveData<List<ShareDto>> {
        return sharesDao.getSharesByPortfolioId(portfolioId)
    }

    suspend fun insertShare(share: ShareDto) = withContext(Dispatchers.IO) {
        sharesDao.insertShare(share)
    }

    suspend fun updateShare(share: ShareDto) = withContext(Dispatchers.IO) {
        sharesDao.updateShare(
            share.id!!,
            if (share.currentPrice != null) share.currentPrice!! else 0f,
            if(share.sellPrice != null) share.currentPrice!! else 0f,
            if(share.sellDate != null) share.sellDate!! else "")
    }

    fun removeShare(share: ShareDto) {
        sharesDao.removeShare(share)
    }

    //endregion

    //region Bonds

    suspend fun getBonds(): List<BondDto> = withContext(Dispatchers.IO) {
        return@withContext bondsDao.getBondsList()
    }

    fun getBondsByPortfolioId(portfolioId: Int): LiveData<List<BondDto>> {
        return bondsDao.getBondsByPortfolioId(portfolioId)
    }

    suspend fun insertBond(bond: BondDto) = withContext(Dispatchers.IO) {
        bondsDao.insertBond(bond)
    }

    fun removeBond(bond: BondDto) {
        bondsDao.removeBond(bond)
    }

    //endregion

    //region Portfolio

    fun getPortfolios(): LiveData<List<PortfolioDto>> {
        return portfolioDao.getPortoliosList()
    }

    fun getPortfoliosByBrokerId(brokerId: Int): LiveData<List<PortfolioDto>> {
        return portfolioDao.getPortfoliosByBrokerId(brokerId)
    }

    suspend fun insertPortfolio(portfolio: PortfolioDto) = withContext(Dispatchers.IO) {
        portfolioDao.insertPortfolio(portfolio)
    }

    fun removePortfolio(portfolio: PortfolioDto) {
        portfolioDao.removePortfolio(portfolio)
    }

    //endregion

    //region Broker

    fun getBrokers(): LiveData<List<BrokerDto>> {
        return brokersDao.getBrokersList()
    }

    fun getBrokersSync(): List<BrokerDto> {
        return brokersDao.getBrokers()
    }

    suspend fun insertBroker(broker: BrokerDto) = withContext(Dispatchers.IO) {
        brokersDao.insertBroker(broker)
    }

    fun removeBroker(broker: BrokerDto) {
        brokersDao.removeBroker(broker)
    }

    //endregion

    //region Dividends

    fun getDividends(): LiveData<List<DividendDto>> {
        return dividendsDao.getDividendsList()
    }

    fun getDividendsByAssetId(assetId: Int): LiveData<List<DividendDto>> {
        return dividendsDao.getDividendsByAssetId(assetId)
    }

    suspend fun insertDividend(dividend: DividendDto) = withContext(Dispatchers.IO) {
        dividendsDao.insertDividend(dividend)
    }

    fun removeDividend(dividend: DividendDto) {
        dividendsDao.removeDividend(dividend)
    }

    //endregion

    /**
     * Получить текущие цены для сохраненных в БД активов
     */
    fun getCurrentPrices(shares: List<ShareDto>): LiveData<List<ShareDto>> {
        val res = MutableLiveData<List<ShareDto>>()
        val sharesList = ArrayList<ShareDto>()
        for (share in shares) {
            mainApi.getCurrentPriceByBoardAndSecurity("TQBR", share.security!!)
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result != null && result.data.size() > 0) {
                        var list = result.data[0]
                        var lis =
                            GsonBuilder().create().fromJson(list, Array<String>::class.java)
                        share.currentPrice = lis[1].toFloat()
                        sharesList.add(share)
                    }
                },
                    {

                    })
        }
        res.postValue(sharesList)
        return res
    }
/*
    fun getPrices(shares : List<ShareDto>) : Observable<List<ShareDto>> {
        return io.reactivex.Observable.fromIterable(shares).flatMap {
            mainApi.getCurrentPriceByBoardAndSecurity("TQBR", it.security!!)
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    if (result != null && result.data.size() > 0) {
                        var list = result.data[0]
                        var lis =
                            GsonBuilder().create().fromJson(list, Array<String>::class.java)
                        share.currentPrice = lis[1].toFloat()
                        sharesList.add(share)
                    }
                }
        }
    }*/

    fun getPrice(share: ShareDto): Observable<ShareDto> {
        return mainApi.getCurrentPriceByBoardAndSecurity("TQBR", share.security!!).flatMap {
            val list = it.data[0]
            val lis = GsonBuilder().create().fromJson(list, Array<String>::class.java)
            share.currentPrice = lis[1].toFloat()
            Log.d("", "Finished")
            Observable.just(share)
        }
    }
}