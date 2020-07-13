package com.dotnet.nikit.investingmonitor.di.main

import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.*
import com.dotnet.nikit.investingmonitor.views.main.navigation.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeNavIdeaFragment(): NavIdeaFragment

    @ContributesAndroidInjector
    abstract fun contributeNavListFragment(): NavListFragment

    @ContributesAndroidInjector
    abstract fun contributeNavRecommendationFragment(): NavRecommendationFragment

    @ContributesAndroidInjector
    abstract fun contributeNavSettingsFragment(): NavSettingsFragment

    @ContributesAndroidInjector
    abstract fun contributePortfoliosFragment() : PortfoliosFragment

    @ContributesAndroidInjector
    abstract fun contributePortfolioAddFragment() : PortfolioAddFragment

    @ContributesAndroidInjector
    abstract fun contributeBrokersSettingsFragment() : BrokersSettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeBrokerAddFragment() : BrokerAddFragment

    @ContributesAndroidInjector
    abstract fun contributeShareAddFragment() : ShareAddFragment

    @ContributesAndroidInjector
    abstract fun contributeBondAddFragment() : BondAddFragment

    @ContributesAndroidInjector
    abstract fun contributeAssetInfoFragment() : AssetInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeDividendAddFragment() : DividendAddFragment
}
