package com.example.mywallet

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface BalanceTypeDao {
    @Insert
    suspend fun insert(balanceType: BankBalanceType)

    @Query("SELECT * FROM BankBalanceType")
    suspend fun getAllBalanceType(): List<BankBalanceType>
}

@Dao
interface BankBalanceDao {
    @Insert(entity = BankBalance::class)
    fun insertNewBankBalance(bankBalance: BankBalance)

    @Query("SELECT * FROM BankBalance")
    suspend fun getAllBankBalance(): List<BankBalance>
}



@Database(entities = [BankBalanceType::class, BankBalance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceTypeDao(): BalanceTypeDao
    abstract fun bankBalanceDao(): BankBalanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bank_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}