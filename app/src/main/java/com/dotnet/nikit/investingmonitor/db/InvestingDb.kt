package com.dotnet.nikit.investingmonitor.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dotnet.nikit.investingmonitor.db.daos.*
import com.dotnet.nikit.investingmonitor.db.models.*

/**
 * Main database description.
 */
@Database(
    entities = [
        BrokerDto::class,
        PortfolioDto::class,
        ShareDto::class,
        BondDto::class,
        DividendDto::class],
    version = 3,
    exportSchema = false
)
abstract class InvestingDb : RoomDatabase() {

    abstract fun sharesDao(): ShareDao

    abstract fun brokersDao(): BrokerDao

    abstract fun portfoliosDao(): PortfolioDao

    abstract fun bondsDao() : BondDao

    abstract fun dividendsDao() : DividendDao
}