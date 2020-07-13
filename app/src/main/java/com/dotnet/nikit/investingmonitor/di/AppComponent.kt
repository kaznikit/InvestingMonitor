package com.dotnet.nikit.investingmonitor.di

import android.app.Application
import com.dotnet.nikit.investingmonitor.InvestingMonitorApp
import com.dotnet.nikit.investingmonitor.di.main.MainActivityModule
import com.dotnet.nikit.investingmonitor.di.main.MainViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivitiesBuilderModule::class,
        AppModule::class,
        ViewModelFactoryModule::class]
)
interface AppComponent : AndroidInjector<InvestingMonitorApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}