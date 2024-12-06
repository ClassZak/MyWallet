package com.example.mywallet

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase


@Dao
interface BankBalanceDao{
    @Insert
    fun insert(bankBalance: BankBalance)

    @Query("SELECT * FROM BankBalance")
    fun getAll(): List<BankBalance>

    @Query("DELETE FROM BankBalance")
    fun deleteAll(): Unit

    @Delete
    fun delete(bankBalance: BankBalance)
}
@Dao
interface BankBalanceTypeDao {
    @Insert
    fun insert(bankBalanceType: BankBalanceType)

    @Query("SELECT * FROM BankBalanceType")
    fun getAll(): List<BankBalanceType>

    @Query("DELETE FROM BankBalanceType")
    fun deleteAll():Unit

    @Delete
    fun delete(bankBalanceType: BankBalanceType)

    @Query("SELECT * FROM BankBalanceType WHERE id = :id")
    fun getById(id:Long):BankBalanceType
}



@Dao
interface OperationDao{
    @Insert
    fun inert(operation: Operation)

    @Query("SELECT * FROM Operation")
    fun getAll():List<Operation>

    @Query("DELETE FROM Operation")
    fun deleteAll():Unit

    @Query("SELECT * FROM Operation WHERE Operation_date >= :startDate AND Operation_date < :endDate")
    fun getOperationsOfPeriod(startDate:String,endDate:String) : List<Operation>
}
@Dao
interface OperationTypeDao {
    @Insert
    fun insert(bankBalanceType: OperationType)

    @Query("SELECT * FROM OperationType")
    fun getAll(): List<OperationType>

    @Query("DELETE FROM OperationType")
    fun deleteAll():Unit

    @Delete
    fun delete(operationType: OperationType)
}





@Database(entities = [BankBalanceType::class, BankBalance::class, OperationType::class,Operation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bankBalanceTypeDao(): BankBalanceTypeDao
    abstract fun bankBalanceDao(): BankBalanceDao
    abstract fun operationTypeDao(): OperationTypeDao
    abstract fun operationDao(): OperationDao

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