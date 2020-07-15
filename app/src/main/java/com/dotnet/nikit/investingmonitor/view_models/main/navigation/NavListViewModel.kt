package com.dotnet.nikit.investingmonitor.view_models.main.navigation

import android.util.Log
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
import kotlinx.coroutines.*
import javax.inject.Inject

class NavListViewModel
@Inject constructor(var navListRepository: NavListRepository) : ViewModel() {

    private val needsToUpdateBrokersList = MutableLiveData<Boolean>()
    private val brokersList = MediatorLiveData<List<BrokerDto>>()
    private val portfoliosList = MediatorLiveData<List<PortfolioDto>>()

    private val forcedUpdate = MutableLiveData<Boolean>(false)
    private val sharesListForPortfolio = MediatorLiveData<List<ShareDto>>()

    private val portfolioIdLiveData = MutableLiveData<Int>()
    private val bondsList = MediatorLiveData<List<BondDto>>()
    private val sharesWithPricesList = MediatorLiveData<List<ShareDto>>()

    private val getSharePricesLiveData = MutableLiveData<Boolean>()

    private val dividendsList = MediatorLiveData<List<DividendDto>>()
    private val dividendsLiveData = MutableLiveData<Int>()

    private var navListFragment: NavListFragment? = null


    private val shareAndPricesList = MutableLiveData<List<ShareDto>>()


    private var portfolioId: Int? = null


    private var allSharesList : List<ShareDto>? = null

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val sharesByPortfolioIdLiveData : MutableLiveData<List<ShareDto>> = MutableLiveData()

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

      /*  val liveSharesWithPricesList = Transformations.switchMap(getSharePricesLiveData) {
            navListRepository.getCurrentPrices(sharesList.value!!)
        }*/

        val liveDividendsList = Transformations.switchMap(dividendsLiveData) {
            navListRepository.getDividendsByAssetId(dividendsLiveData.value!!)
        }

        GlobalScope.launch {
            allSharesList = navListRepository.getShares()
            getAssetsPrices()
        }

        /**
         * Загружаем из БД все активы
         */
        loadAssets(true)


        val sharesForPortfolioLiveData = Transformations.switchMap(portfolioIdLiveData) {
            getSharesByPortfolioId(portfolioIdLiveData.value!!)
        }

        sharesListForPortfolio.addSource(sharesForPortfolioLiveData) {
            sharesListForPortfolio.value = it
        }


        dividendsList.addSource(liveDividendsList, dividendsList::setValue)


        /**
         * когда получили текущие цены через апи, меняем их в sharesList
         */
        //sharesWithPricesList.addSource(liveSharesWithPricesList, sharesWithPricesList::setValue)
        bondsList.addSource(liveBondsList, bondsList::setValue)
    }


    fun getNavListFragment(): NavListFragment {
        if (navListFragment == null) {
            navListFragment = NavListFragment()
        }
        return navListFragment!!
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
        this.portfolioId = portfolioId
    }

    private fun postPortfolioValue() {
        portfolioIdLiveData.postValue(portfolioId)
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

    private fun getAssetsPrices() {
        val tempList = ArrayList<ShareDto>()

        Observable.fromIterable(allSharesList).flatMap {
            navListRepository.getPrice(it).doOnNext {
                tempList.add(it)
            }.subscribeOn(Schedulers.io())
        }
            .subscribe({
                allSharesList = tempList
                postPortfolioValue()
                GlobalScope.launch {
                    updateShares()
                }
            },
                {
                    Log.e("Network", "Ошибка при получении цен.")
                    postPortfolioValue()
                })
    }

    private fun loadAssets(forcedUpdate: Boolean) {
        this.forcedUpdate.postValue(forcedUpdate)
    }

    private fun getSharesByPortfolioId(portfolioId: Int): LiveData<List<ShareDto>> {
        sharesByPortfolioIdLiveData.value = allSharesList!!.filter { x -> x.portfolioId == portfolioId }
        return sharesByPortfolioIdLiveData
    }

    fun getSharesForPortfolio(): LiveData<List<ShareDto>> {
        return sharesListForPortfolio
    }

    private suspend fun updateShares() = withContext(Dispatchers.IO){
        for(share in allSharesList!!) {
            navListRepository.updateShare(share)
        }
    }
}