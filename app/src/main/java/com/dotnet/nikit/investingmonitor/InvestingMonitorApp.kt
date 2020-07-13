package com.dotnet.nikit.investingmonitor

import com.dotnet.nikit.investingmonitor.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class InvestingMonitorApp : DaggerApplication()
{
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}