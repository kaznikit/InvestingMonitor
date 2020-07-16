package com.dotnet.nikit.investingmonitor.view_models.main.navigation

import android.util.Log
import androidx.lifecycle.*
import com.dotnet.nikit.investingmonitor.db.models.*
import com.dotnet.nikit.investingmonitor.mappers.*
import com.dotnet.nikit.investingmonitor.models.*
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
    private var allSharesList : List<ShareDto>? = null
    private var allBondsList : List<BondDto>? = null
    private val sharesListForPortfolio = MediatorLiveData<List<Share>>()
    private val bondsListForPortfolio = MediatorLiveData<List<Bond>>()

    private val portfolioIdLiveData = MutableLiveData<Int>()

    private val dividendsList = MediatorLiveData<List<DividendDto>>()
    private val dividendsLiveData = MutableLiveData<Int>()

    private var navListFragment: NavListFragment? = null

    private var portfolioId: Int? = null

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val sharesByPortfolioIdLiveData : MutableLiveData<List<ShareDto>> = MutableLiveData()
    private val bondsByPortfolioIdLiveData : MutableLiveData<List<BondDto>> = MutableLiveData()

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

        val liveDividendsList = Transformations.switchMap(dividendsLiveData) {
            navListRepository.getDividendsByAssetId(dividendsLiveData.value!!)
        }

        CoroutineScope(Dispatchers.IO).launch {
            getSharesList()
            getBondsList()
            getSharesPrices()
            getBondsPrices()
        }

        /**
         * Загружаем из БД все активы
         */
        loadAssets(true)


        val sharesForPortfolioLiveData = Transformations.switchMap(portfolioIdLiveData) {
            getSharesByPortfolioId(portfolioIdLiveData.value!!)
        }

        sharesListForPortfolio.addSource(sharesForPortfolioLiveData) {
            sharesListForPortfolio.value = ShareMapper.mapShareList(it)
        }

        val bondsForPortfolioLiveData = Transformations.switchMap(portfolioIdLiveData){
            getBondsByPortfolioId(portfolioIdLiveData.value!!)
        }

        bondsListForPortfolio.addSource(bondsForPortfolioLiveData){
            bondsListForPortfolio.value = BondMapper.mapBondList(it)
        }

        dividendsList.addSource(liveDividendsList, dividendsList::setValue)
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
        CoroutineScope(Dispatchers.IO).launch {
            navListRepository.insertBroker(BrokerMapper.mapBrokerDto(broker))
        }
    }

    fun addShare(share: Share, portfolioId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            navListRepository.insertShare(ShareMapper.mapShareDto(share, portfolioId))
            getSharesList()
        }
    }

    fun addBond(bond : Bond, portfolioId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            navListRepository.insertBond(BondMapper.mapBondDto(bond, portfolioId))
            getBondsList()
        }
    }

    fun getAssetsByPortfolioId(portfolioId: Int) {
        this.portfolioId = portfolioId
    }

    private fun postPortfolioValue() {
        portfolioIdLiveData.postValue(portfolioId)
    }

    private suspend fun getSharesList(){
        allSharesList = navListRepository.getShares()
        postPortfolioValue()
    }

    private suspend fun getBondsList() {
        allBondsList = navListRepository.getBonds()
        postPortfolioValue()
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

    private fun getSharesPrices() {
        val tempList = ArrayList<ShareDto>()

        Observable.fromIterable(allSharesList).flatMap {
            navListRepository.getSharePrice(it).doOnNext {
                tempList.add(it)
            }.subscribeOn(Schedulers.io())
        }
            .subscribe({
                allSharesList = tempList
                postPortfolioValue()
                viewModelScope.launch {
                    updateShares()
                }
            },
                {
                    Log.e("Network", "Ошибка при получении цен.")
                    postPortfolioValue()
                })
    }

    private fun getBondsPrices(){
        val tempList = ArrayList<BondDto>()
        Observable.fromIterable(allBondsList).flatMap {
            navListRepository.getBondPrice(it).doOnNext {
                tempList.add(it)
            }.subscribeOn(Schedulers.io())
        }
            .subscribe({
                allBondsList = tempList
                postPortfolioValue()
               /* viewModelScope.launch {
                    updateBonds()
                }*/
            },
                {
                    Log.e("Netweork", "Ошибка при получении цен облигаций.")
                    postPortfolioValue()
                })
    }

    private fun loadAssets(forcedUpdate : Boolean) {
        this.forcedUpdate.postValue(forcedUpdate)
    }

    private fun getSharesByPortfolioId(portfolioId: Int): LiveData<List<ShareDto>> {
        sharesByPortfolioIdLiveData.value = allSharesList!!.filter { x -> x.portfolioId == portfolioId }
        return sharesByPortfolioIdLiveData
    }

    private fun getBondsByPortfolioId(portfolioId: Int) : LiveData<List<BondDto>> {
        bondsByPortfolioIdLiveData.value = allBondsList!!.filter { x -> x.portfolioId == portfolioId }
        return bondsByPortfolioIdLiveData
    }

    fun getSharesForPortfolio(): LiveData<List<Share>> {
        return sharesListForPortfolio
    }

    fun getBondsForPortfolio() : LiveData<List<Bond>>{
        return bondsListForPortfolio
    }

    private suspend fun updateShares() = withContext(Dispatchers.IO){
        for(share in allSharesList!!) {
            navListRepository.updateShare(share)
        }
    }

    private suspend fun updateBonds() = withContext(Dispatchers.IO){
        for(bond in allBondsList!!){
            navListRepository.updateBond(bond)
        }
    }
}