package com.example.mywallet

import androidx.room.*
import java.util.Date

@Entity(
    tableName = "Operation",
    foreignKeys = [
        ForeignKey(
            entity = OperationType::class,
            parentColumns = ["OperationType_id"],
            childColumns = ["OperationType_id"],
            onDelete = ForeignKey.CASCADE // Удаление операций при удалении типов операций
        ),
        ForeignKey(
            entity = BankBalanceType::class,
            parentColumns = ["BalanceType_id"],
            childColumns = ["BalanceType_id"],
            onDelete = ForeignKey.CASCADE // Удаление операций при удалении типов операций
        )
    ]
)
data class Operation(
    @PrimaryKey(autoGenerate = true) var Operation_id : Int,
    @ColumnInfo(index = true) var OperationType_id : Int,
    @ColumnInfo(name = "Operation_description") var Operation_description: String,
    @ColumnInfo(name = "Operation_amount") var Operation_amount: Double,
    @ColumnInfo(name = "Operation_date") var Operation_date: Date,
    @ColumnInfo(index = true) var BankBalance_id : Int){
}