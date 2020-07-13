package com.dotnet.nikit.investingmonitor.views.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.interfaces.OnSelectListViewItem
import com.dotnet.nikit.investingmonitor.listview_adapters.AssetListViewItem
import com.dotnet.nikit.investingmonitor.models.Portfolio
import com.dotnet.nikit.investingmonitor.view_models.main.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), OnSelectListViewItem<Any> {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val mainViewModel : MainViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        loadFragment(mainViewModel.getPortfoliosFragment(), "portfolios")

        bottom_navigation_view.setOnNavigationItemSelectedListener {

            if(supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStack()
            }

            when(it.itemId){
                R.id.navigation_list -> {
                    loadFragment(mainViewModel.getPortfoliosFragment(), "portfolios")
                }
                R.id.navigation_idea -> {
                    loadFragment(mainViewModel.getNavIdeaFragment(), "idea")
                }
                R.id.navigation_recommendation -> {
                    loadFragment(mainViewModel.getNavRecommendationFragment(), "recommend")
                }
                R.id.navigation_settings -> {
                    loadFragment(mainViewModel.getNavSettingsFragment(), "settings")
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun loadFragment(fragment : DaggerFragment, tag : String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        supportFragmentManager.popBackStack()
        return super.onOptionsItemSelected(item)
    }

    override fun onSelectListViewItem(data: Any) {
        when(data){
            is Portfolio -> {
                val bundle = Bundle()
                bundle.putInt("id", data.id!!)
                bundle.putString("name", data.name)
                mainViewModel.getNavListFragment().arguments = bundle
                loadFragment(mainViewModel.getNavListFragment(), "navListFragment")
            }
            is AssetListViewItem -> {
                val bundle = Bundle()
                when(data.assetType){
                    AssetTypeEnum.Share -> {
                        bundle.putInt("id", data.id)
                        bundle.putString("name", data.name)
                        bundle.putString("buyPrice", data.buyPrice)
                        bundle.putString("currentPrice", data.currentPrice)
                        bundle.putString("priceChange", data.priceChange)
                        mainViewModel.getAssetInfoFragment().arguments = bundle
                        loadFragment(mainViewModel.getAssetInfoFragment(), "assetInfoFragment")
                    }
                    AssetTypeEnum.Bond -> {

                    }
                    AssetTypeEnum.ETF -> {

                    }
                }
            }
            is String -> {
                if(data == "Брокеры"){
                    loadFragment(mainViewModel.getBrokerSettingsFragment(), "brokerFragment")
                }
            }
        }
    }
}
