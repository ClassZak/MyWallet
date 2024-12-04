package com.example.mywallet

import androidx.room.*

@Entity(
    tableName = "BankBalance",
            foreignKeys = [
        ForeignKey(
            entity = BankBalanceType::class,
            parentColumns = ["BankBalanceType_id"],
            childColumns = ["BankBalanceType_id"],
            onDelete = ForeignKey.CASCADE // Удаление счетов при удалении типов счетов
        )
    ]
)
data class BankBalance(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "BankBalance_name") var name : String,
    @ColumnInfo(name = "BankBalance_description") var descption:String,
    @ColumnInfo(index=true) var BankBalanceType_id: Int,
    @ColumnInfo(name = "BankBalance_start_balance") var BankBalance_start_balance:Double){
}