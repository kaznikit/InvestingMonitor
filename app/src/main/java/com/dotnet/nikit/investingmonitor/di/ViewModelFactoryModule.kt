package com.dotnet.nikit.investingmonitor.di

import androidx.lifecycle.ViewModelProvider
import com.dotnet.nikit.investingmonitor.di.main.MainActivityModule
import com.dotnet.nikit.investingmonitor.di.main.MainViewModelFactoryModule
import com.dotnet.nikit.investingmonitor.view_models.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module (includes = [MainViewModelFactoryModule::class, MainActivityModule::class])
abstract class ViewModelFactoryModule {

    /*@Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: RepoViewModel): ViewModel*/

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}