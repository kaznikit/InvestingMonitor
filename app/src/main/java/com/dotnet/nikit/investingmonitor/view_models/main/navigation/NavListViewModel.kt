package com.dotnet.nikit.investingmonitor.view_models.main.navigation

import android.util.Log
import android.view.ScaleGestureDetector
import androidx.lifecycle.*
import com.dotnet.nikit.investingmonitor.db.models.*
import com.dotnet.nikit.investingmonitor.mappers.BrokerMapper
import com.dotnet.nikit.investingmonitor.mappers.DividendMapper
import com.dotnet.nikit.investingmonitor.mappers.PortfolioMapper
import com.dotnet.nikit.investingmonitor.mappers.ShareMapper
import com.dotnet.nikit.investingmonitor.models.Broker
import com.dotnet.nikit.investingmonitor.models.Dividend
import com.dotnet.nikit.investingmonitor.models.Portfolio
import com.dotnet.nikit.investingmonitor.models.Share
import com.dotnet.nikit.investingmonitor.repositories.NavListRepository
import com.dotnet.nikit.investingmonitor.views.main.navigation.NavListFragment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavListViewModel
@Inject constructor(var navListRepository: NavListRepository) : ViewModel() {

    private val needsToUpdateBrokersList = MutableLiveData<Boolean>()
    private val brokersList = MediatorLiveData<List<BrokerDto>>()
    private val portfoliosList = MediatorLiveData<List<PortfolioDto>>()

    private val portfolioIdLiveData = MutableLiveData<Int>()
    private val sharesList = MediatorLiveData<List<ShareDto>>()
    private val bondsList = MediatorLiveData<List<BondDto>>()
    private val sharesWithPricesList = MediatorLiveData<List<ShareDto>>()

    private val getSharePricesLiveData = MutableLiveData<Boolean>()

    private val dividendsList = MediatorLiveData<List<DividendDto>>()
    private val dividendsLiveData = MutableLiveData<Int>()

    private var navListFragment: NavListFragment? = null


    private val shareAndPricesList = MutableLiveData<List<ShareDto>>()

    init {
        needsToUpdateBrokersList.postValue(true)
        val liveBrokersList = Transformations.switchMap(needsToUpdateBrokersList) {
            navListRepository.getBrokers()
        }

        val livePortfoliosList = Transformations.switchMap(needsToUpdateBrokersList) {
            navListRepository.getPortfolios()
        }

        brokersList.addSource(liveBrokersList, brokersList::setValue)
        portfoliosList.addSource(livePortfoliosList, portfoliosList::setValue)

        val liveSharesList = Transformations.switchMap(portfolioIdLiveData) {
            navListRepository.getSharesByPortfolioId(portfolioIdLiveData.value!!)
        }

        val liveBondsList = Transformations.switchMap(portfolioIdLiveData) {
            navListRepository.getBondsByPortfolioId(portfolioIdLiveData.value!!)
        }

        val liveSharesWithPricesList = Transformations.switchMap(getSharePricesLiveData) {
            navListRepository.getCurrentPrices(sharesList.value!!)
        }

        val liveDividendsList = Transformations.switchMap(dividendsLiveData) {
            navListRepository.getDividendsByAssetId(dividendsLiveData.value!!)
        }

        dividendsList.addSource(liveDividendsList, dividendsList::setValue)

        sharesList.addSource(liveSharesList) {
            sharesList.value = it
            //getSharesWithPrices()
            requestForPrices()
        }

        /**
         * когда получили текущие цены через апи, меняем их в sharesList
         */
        sharesWithPricesList.addSource(liveSharesWithPricesList, sharesWithPricesList::setValue)
        bondsList.addSource(liveBondsList, bondsList::setValue)
    }

    /**
     * получаем записанные в БД активы, уведомляем об этом активити и после этого подгружаем текущие цены с сервера
     */
    fun getSharesWithPrices() {
        getSharePricesLiveData.postValue(true)
    }

    /*fun getSharesCombined() : CombinedLiveData<Share, Share>{

    }

    fun combineSharesData(T : LiveData<List<Share>>, K : LiveData<List<Share>>) : MediatorLiveData<Share>{
        for(value in T.value!!.iterator()){

        }
    }*/

    fun getNavListFragment(): NavListFragment {
        if (navListFragment == null) {
            navListFragment = NavListFragment()
        }
        return navListFragment!!
    }

    fun getCurrentPrices(): LiveData<List<ShareDto>> {
        /*sharesList.value = Event.loading()
        sharesList.postValue(navListRepository.getCurrentPrices(sharesList.value!!.data!!).value)*/
        return sharesWithPricesList
    }

    fun addPortfolio(portfolio: Portfolio) {
        GlobalScope.launch {
            navListRepository.insertPortfolio(PortfolioMapper.mapPortfolioDto(portfolio))
        }
    }

    fun getPortfolios(): LiveData<List<PortfolioDto>> {
        return portfoliosList
    }

    fun getBrokers(): LiveData<List<BrokerDto>> {
        return brokersList
    }

    fun addBroker(broker: Broker) {
        GlobalScope.launch {
            navListRepository.insertBroker(BrokerMapper.mapBrokerDto(broker))
        }
    }

    fun addShare(share: Share, portfolioId: Int) {
        GlobalScope.launch {
            navListRepository.insertShare(ShareMapper.mapShareDto(share, portfolioId))
        }
    }

    fun getAssetsByPortfolioId(portfolioId: Int) {
        portfolioIdLiveData.postValue(portfolioId)
    }

    fun getSharesList(): LiveData<List<ShareDto>> {
        return sharesList
    }

    fun getBondsList(): LiveData<List<BondDto>> {
        return bondsList
    }

    fun getDividends(): LiveData<List<DividendDto>> {
        return dividendsList
    }

    fun getDividendsByAssetId(assetId: Int) {
        dividendsLiveData.postValue(assetId)
    }

    fun addDividend(dividend: Dividend) {
        GlobalScope.launch {
            navListRepository.insertDividend(DividendMapper.mapDividendDto(dividend))
        }
    }

 /*   fun getPrices(): LiveData<List<ShareDto>> {
        var tempList = ArrayList<ShareDto>()
        for (share in sharesList.value!!) {
            var res = navListRepository.getPrice(share)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tempList.add(it)
                },
                    {
                        Log.e("navListViewModel", it?.message!!)
                    })
        }
        shareAndPricesList.postValue(tempList)
        return shareAndPricesList
    }*/

    fun requestForPrices(){
      /*  val tempList = ArrayList<ShareDto>()
        for (share in sharesList.value!!) {
            var res = navListRepository.getPrice(share)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tempList.add(it)
                },
                    {
                        Log.e("navListViewModel", it?.message!!)
                    })
        }
        shareAndPricesList.postValue(tempList)*/

        Observable.fromArray(sharesList.value!!).flatMap {
            var tempList = ArrayList<ShareDto>()
            for(share in it){
                navListRepository.getPrice(share).subscribeOn(Schedulers.io()).subscribe {
                    tempList.add(it)
                }
            }
        }.toList().subscribeOn(Schedulers.io()).map {
            shareAndPricesList.postValue(it!!)
        }


        Observable.zip(sharesList.value!!){

        }
    }

    fun getPrices() : LiveData<List<ShareDto>>{
        return shareAndPricesList
    }
}