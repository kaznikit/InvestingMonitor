package com.dotnet.nikit.investingmonitor.di.main

import com.dotnet.nikit.investingmonitor.db.daos.ShareDao
import com.dotnet.nikit.investingmonitor.network.MainApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainActivityModule {

    @Provides
    fun provideMainApi(retrofit: Retrofit) : MainApi{
        return retrofit.create(MainApi::class.java)
    }
}