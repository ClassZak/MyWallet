package com.example.mywallet

import androidx.room.*

@Entity(tableName = "BankBalanceType")
data class BankBalanceType(
    @PrimaryKey(autoGenerate = true) val BankBalanceType_id:Int,
    @ColumnInfo(name = "BankBalanceType_name") var BankBalanceType_name:String
) {
}