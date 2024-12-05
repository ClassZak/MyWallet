package com.example.mywallet

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update



@Entity(tableName = "BankBalanceTypes")
data class BankBalanceBalance(
    @PrimaryKey(autoGenerate = true) val BankBalanceType_id:Int,
    @ColumnInfo(name = "BankBalanceType_name") var BankBalanceType_name:String
) {
}

@Dao
interface BankBalanceTypeDao {
    @Query("SELECT * FROM BankBalanceTypes")
    suspend fun getAllBankBalanceTypes(): List<Any>
}
/*
@Dao
interface BankBalanceDao {
    @Insert(entity = BankBalance::class)
    fun insertNewBankBalance(bankBalance: BankBalance)

    @Query("SELECT * FROM BankBalance")
    suspend fun getAllBankBalance(): List<BankBalance>
}


*/
@Database(entities = [BankBalanceType::class, BankBalance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceTypeDao(): BankBalanceTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
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