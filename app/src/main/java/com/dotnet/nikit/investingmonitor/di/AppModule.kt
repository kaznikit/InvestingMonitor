package com.dotnet.nikit.investingmonitor.di

import android.app.Application
import androidx.room.Room
import com.dotnet.nikit.investingmonitor.db.InvestingDb
import com.dotnet.nikit.investingmonitor.db.daos.*
import com.dotnet.nikit.investingmonitor.network.models.MarketData
import com.dotnet.nikit.investingmonitor.network.deserializers.MoexResponseDeserializer
import com.dotnet.nikit.investingmonitor.util.Constants
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.PortfolioAddFragment
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.android.support.DaggerFragment
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryModule::class])
class AppModule {
    /*@Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(GithubService::class.java)
    }*/

    @Singleton
    @Provides
    fun provideRetrofitInstance() : Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(MarketData::class.java, MoexResponseDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_MOEX_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): InvestingDb {
        return Room
            .databaseBuilder(app, InvestingDb::class.java, "investingMonitor.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBrokerDao(dataBase : InvestingDb) : BrokerDao{
        return dataBase.brokersDao()
    }

    @Singleton
    @Provides
    fun providePortfolioDao(dataBase : InvestingDb) : PortfolioDao{
        return dataBase.portfoliosDao()
    }

    @Singleton
    @Provides
    fun provideShareDao(dataBase : InvestingDb) : ShareDao{
        return dataBase.sharesDao()
    }

    @Singleton
    @Provides
    fun provideBondDao(dataBase: InvestingDb) : BondDao{
        return dataBase.bondsDao()
    }

    @Singleton
    @Provides
    fun provideDividendDao(dataBase: InvestingDb) : DividendDao {
        return dataBase.dividendsDao()
    }
}