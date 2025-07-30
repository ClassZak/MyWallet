package com.example.mywallet

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "BankBalance",
            foreignKeys = [
        ForeignKey(
            entity = BankBalanceType::class,
            parentColumns = ["id"],
            childColumns = ["BankBalanceType_id"],
            onDelete = ForeignKey.CASCADE // Удаление счетов при удалении типов счетов
        )
    ]
)
data class BankBalance(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "BankBalance_name") var BankBalance_name : String,
    @ColumnInfo(name = "BankBalance_description") var BankBalance_descption:String,
    @ColumnInfo(name = "BankBalance_balance") var BankBalance_balance:Double,
    @ColumnInfo(index=true) var BankBalanceType_id: Int,
    @ColumnInfo(name = "BankBalance_start_balance") var BankBalance_start_balance:Double):Serializable{
}