package com.example.mywallet

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "BankBalanceType")
data class BankBalanceType(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "BankBalanceType_name") val BankBalanceType_name:String
) {
}