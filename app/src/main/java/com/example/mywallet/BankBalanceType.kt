package com.example.mywallet

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import java.io.Serializable

@Entity(tableName = "BankBalanceType")
data class BankBalanceType(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "BankBalanceType_name") val BankBalanceType_name:String
) : Serializable {
}