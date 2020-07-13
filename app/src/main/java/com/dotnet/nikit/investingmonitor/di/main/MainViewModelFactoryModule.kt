package com.dotnet.nikit.investingmonitor.di.main

import androidx.lifecycle.ViewModel
import com.dotnet.nikit.investingmonitor.di.ViewModelKey
import com.dotnet.nikit.investingmonitor.view_models.main.MainViewModel
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavSettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavListViewModel::class)
    abstract fun bindNavListViewModel(navListViewModel : NavListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavSettingsViewModel::class)
    abstract fun bindNavSettingsViewModel(navSettingsViewModel: NavSettingsViewModel) : ViewModel
}