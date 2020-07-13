package com.dotnet.nikit.investingmonitor.view_models.main

import androidx.lifecycle.ViewModel
import com.dotnet.nikit.investingmonitor.views.main.navigation.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private var navListFragment : NavListFragment? = null
    private var navIdeaFragment : NavIdeaFragment? = null
    private var navRecommendationFragment : NavRecommendationFragment? = null
    private var navSettingsFragment : NavSettingsFragment? = null
    private var portoliosFragment : PortfoliosFragment? = null
    private var brokerSettingsFragment : BrokersSettingsFragment? = null
    private var assetInfoFragment : AssetInfoFragment? = null

    fun getPortfoliosFragment() : PortfoliosFragment{
        if(portoliosFragment == null){
            portoliosFragment = PortfoliosFragment()
        }
        return portoliosFragment!!
    }

    fun getNavListFragment() : NavListFragment {
        if(navListFragment == null){
            navListFragment = NavListFragment()
        }
        return navListFragment!!
    }

    fun getNavIdeaFragment() : NavIdeaFragment {
        if(navIdeaFragment == null){
            navIdeaFragment = NavIdeaFragment()
        }
        return navIdeaFragment!!
    }

    fun getNavRecommendationFragment() : NavRecommendationFragment {
        if(navRecommendationFragment == null){
            navRecommendationFragment = NavRecommendationFragment()
        }
        return navRecommendationFragment!!
    }

    fun getNavSettingsFragment() : NavSettingsFragment {
        if(navSettingsFragment == null){
            navSettingsFragment = NavSettingsFragment()
        }
        return navSettingsFragment!!
    }

    fun getBrokerSettingsFragment() : BrokersSettingsFragment{
        if(brokerSettingsFragment == null){
            brokerSettingsFragment = BrokersSettingsFragment()
        }
        return brokerSettingsFragment!!
    }

    fun getAssetInfoFragment() : AssetInfoFragment{
        if(assetInfoFragment == null){
            assetInfoFragment = AssetInfoFragment()
        }
        return assetInfoFragment!!
    }
}