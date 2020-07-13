package com.dotnet.nikit.investingmonitor.di

import com.dotnet.nikit.investingmonitor.di.main.MainActivityModule
import com.dotnet.nikit.investingmonitor.di.main.MainFragmentsBuilderModule
import com.dotnet.nikit.investingmonitor.di.main.MainViewModelFactoryModule
import com.dotnet.nikit.investingmonitor.views.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector(modules = [//MainActivityModule::class,
        MainFragmentsBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity

}